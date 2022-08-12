package ink.ptms.zaphkiel.impl

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import ink.ptms.zaphkiel.Zaphkiel
import ink.ptms.zaphkiel.api.ItemKey
import ink.ptms.zaphkiel.api.ItemSerializer
import ink.ptms.zaphkiel.api.ItemStream
import ink.ptms.zaphkiel.api.SerializedItem
import ink.ptms.zaphkiel.impl.item.DefaultItemStream
import ink.ptms.zaphkiel.impl.item.DefaultSerializedItem
import ink.ptms.zaphkiel.impl.meta.MetaUnique
import org.apache.commons.lang3.time.DateFormatUtils
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.getDataFolder
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.parseToItemStack
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagSerializer
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import java.io.File

/**
 * Zaphkiel
 * ink.ptms.zaphkiel.impl.DefaultItemSerializer
 *
 * @author 坏黑
 * @since 2022/7/23 16:28
 */
class DefaultItemSerializer : ItemSerializer {

    val air = DefaultSerializedItem("minecraft:air", 1, null, null)

    override fun serialize(itemStack: ItemStack): SerializedItem {
        return if (itemStack.isAir()) air else serialize(Zaphkiel.api().getItemHandler().read(itemStack))
    }

    override fun serialize(itemStream: ItemStream): SerializedItem {
        return if (itemStream.isVanilla()) {
            DefaultSerializedItem(
                "minecraft:${XMaterial.matchXMaterial(itemStream.sourceItem).name.lowercase()}",
                itemStream.sourceItem.amount,
                null,
                null
            )
        } else {
            DefaultSerializedItem(
                itemStream.getZaphkielName(),
                itemStream.sourceItem.amount,
                itemStream.getZaphkielData().takeIf { it.isNotEmpty() }
                    ?.let { ItemTagSerializer.serializeData(it).asJsonObject },
                itemStream.getZaphkielUniqueData()?.let {
                    DefaultSerializedItem.UniqueData(
                        it["player"]?.asString(),
                        it["date"]!!.asLong(),
                        it["uuid"]!!.asString()
                    )
                }
            )
        }
    }

    override fun serializeToYAML(itemStack: ItemStack, name: String) {
        val file = File("${getDataFolder()}/item", "saved.yml")
        val config = if (file.exists()) Configuration.loadFromFile(file, Type.YAML) else Configuration.empty(Type.YAML)
        val section = config.createSection(name)
        val itemManager = Zaphkiel.api().getItemManager() as DefaultItemManager
        section["display"] = itemManager.registeredDisplay.keys.firstOrNull()
        section["icon"] = itemStack.type.name
        val itemMeta = itemStack.itemMeta ?: return
        section["name.NAME"] = itemMeta.displayName
        section["lore.LORE"] = itemMeta.lore
        val meta = section.createSection("meta")
        for ((key, m) in Zaphkiel.api().getItemManager().getMetaMap()) {
            m.invokeConstructor(meta).fromMeta(key, itemMeta, itemStack.getItemTag())
        }
        config.saveToFile(file)
    }

    override fun deserialize(json: String): ItemStream {
        return deserialize(JsonParser().parse(json).asJsonObject)
    }

    override fun deserialize(json: JsonObject): ItemStream {
        if (json["id"] == null) {
            error("id is null")
        }
        val id = json["id"]!!.asString
        return if (id.startsWith("minecraft:")) {
            DefaultItemStream(
                id.substring("minecraft:".length).parseToItemStack().also { it.amount = json["amount"]?.asInt ?: 1 })
        } else {
            deserialize(
                DefaultSerializedItem(
                    json["id"]!!.asString,
                    json["amount"]?.asInt ?: 1,
                    json["data"]?.asJsonObject,
                    json["unique"]?.asJsonObject?.let {
                        DefaultSerializedItem.UniqueData(
                            it["player"]?.asString,
                            it["date"]!!.asLong,
                            it["uuid"]!!.asString
                        )
                    }
                )
            )
        }
    }

    override fun deserialize(item: SerializedItem): ItemStream {
        return if (item.id.startsWith("minecraft:")) {
            DefaultItemStream(
                item.id.substring("minecraft:".length).parseToItemStack().also { it.amount = item.amount })
        } else {
            val itemStream =
                Zaphkiel.api().getItemManager().generateItem(item.id) ?: error("item not found: ${item.id}")
            val data = item.data
            if (data != null) {
                itemStream.getZaphkielCompound()!![ItemKey.DATA.key] = ItemTagSerializer.deserializeData(data)
            }
            val unique = item.uniqueData
            if (unique != null) {
                itemStream.getZaphkielCompound()!![ItemKey.UNIQUE.key] = ItemTag().also {
                    it["player"] = ItemTagData(unique.player)
                    it["date"] = ItemTagData(unique.date)
                    it["date-formatted"] = ItemTagData(DateFormatUtils.format(unique.date, MetaUnique.FORMAT))
                    it["uuid"] = ItemTagData(unique.uuid)
                }
            }
            itemStream.sourceItem.amount = item.amount
            itemStream
        }
    }
}