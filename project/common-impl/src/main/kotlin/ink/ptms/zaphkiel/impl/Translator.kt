package ink.ptms.zaphkiel.impl

import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType
import java.util.regex.Pattern

object Translator {

    val regexShort = Pattern.compile("\\d+s")!!

    fun toNBTBase(obj: Any?): ItemTagData? {
        return when (obj) {
            is String -> if (regexShort.matcher(obj.toString()).matches()) {
                toNBTBase(java.lang.Short.valueOf(obj.toString().substring(0, obj.toString().length - 1)))
            } else {
                ItemTagData(obj as String?)
            }

            is Int -> ItemTagData(obj)
            is Double -> ItemTagData(obj)
            is Float -> ItemTagData(obj)
            is Short -> ItemTagData(obj)
            is Long -> ItemTagData(obj)
            is Byte -> ItemTagData(obj)
            is List<*> -> toNBTList(ItemTagList(), (obj as List<*>?)!!)
            is Map<*, *> -> {
                val nbtCompound = ItemTag()
                obj.forEach { (key, value) -> nbtCompound[key.toString()] = toNBTBase(value) }
                nbtCompound
            }

            is ConfigurationSection -> {
                val nbtCompound = ItemTag()
                obj.getValues(false).forEach { (key, value) -> nbtCompound[key] = toNBTBase(value) }
                nbtCompound
            }

            else -> ItemTagData("Error: " + obj!!)
        }
    }

    fun toNBTList(nbtList: ItemTagList, list: List<*>): ItemTagList {
        list.forEach { obj ->
            val base = toNBTBase(obj)
            if (base == null) {
                warning("Invalid Type: " + obj + " [" + obj!!.javaClass.simpleName + "]")
                return@forEach
            }
            nbtList.add(base)
        }
        return nbtList
    }

    fun toNBTCompound(nbt: ItemTag, section: ConfigurationSection): ItemTag {
        section.getKeys(false).forEach { key ->
            val obj = section[key]
            val base: ItemTagData?
            if (obj is ConfigurationSection) {
                base = toNBTCompound(ItemTag(), section.getConfigurationSection(key)!!)
            } else {
                base = toNBTBase(obj)
                if (base == null) {
                    warning("Invalid Type: " + obj + " [" + obj!!.javaClass.simpleName + "]")
                    return@forEach
                }
            }
            if (key.endsWith("!!")) {
                nbt[key.substring(0, key.length - 2)] = base
            } else {
                nbt[key] = base
            }
        }
        return nbt
    }

    fun toConfigSection(nbt: ItemTag, section: ConfigurationSection) {
        for (key in nbt.keys) {
            val itemTagData = nbt[key] ?: continue
            val translate = itemTagData.translate() ?: continue
            if (translate is ItemTagList) {
                val sec = section.createSection(key)
                translate.forEach {
                    val t = it.translate() ?: return@forEach
                    if (t is ItemTag) {
                        toConfigSection(t, sec)
                        return@forEach
                    }
                    if (t is ItemTagList) {
                        sec[key] = t.toString()
                    } else
                        sec[key] = t
                }
            } else if (translate is ItemTag) {
                toConfigSection(translate, section.createSection(key))
            } else
                section[key] = translate
        }
    }

    private fun ItemTagData.translate(): Any? = when (type) {
        ItemTagType.BYTE -> asByte()
        ItemTagType.SHORT -> asShort()
        ItemTagType.INT -> asInt()
        ItemTagType.LONG -> asLong()
        ItemTagType.FLOAT -> asFloat()
        ItemTagType.DOUBLE -> asDouble()
        ItemTagType.BYTE_ARRAY -> asByteArray()
        ItemTagType.INT_ARRAY -> asIntArray()
        ItemTagType.STRING -> asString()
        ItemTagType.LIST -> asList()
        ItemTagType.COMPOUND -> asCompound()
        else -> null
    }
}