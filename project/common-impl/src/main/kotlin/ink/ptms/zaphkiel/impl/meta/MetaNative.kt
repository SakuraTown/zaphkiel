package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.impl.Translator
import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.ConfigSection
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData

@MetaKey("native")
class MetaNative(root: ConfigurationSection) : Meta(root) {

    val nativeNBT = ItemTag().also { nbt ->
        root.getConfigurationSection("meta.native")?.run {
            getValues(false).forEach {
                val value = it.value ?: return@forEach
                if (value is ConfigSection) {
                    nbt[it.key] = Translator.toNBTCompound(ItemTag(), value)
                } else {
                    val toNBT = ItemTagData.toNBT(value)
                    nbt[it.key] = toNBT
                }
            }
        }
    }
    override val id: String
        get() = "native"

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        val section = root.createSection(key)
        //复制一份
//        val fromJson = ItemTag.fromJson(compound.toJson())
        compound.remove("display")
        compound.remove("Enchantments")
        compound.remove("StoredEnchantments")
        compound.remove("CustomPotionEffects")
        compound.remove("AttributeModifiers")
        compound.remove("CanPlaceOn")
        compound.remove("CanDestroy")
        compound.remove("HideFlags")
        compound.remove("zaphkiel")
        compound.remove("SkullOwner")
        compound.remove("Unbreakable")
        compound.remove("Damage")
        compound.remove("BlockEntityTag")
        if (compound.isEmpty()) {
            root[key] = null
            return
        }
        Translator.toConfigSection(compound, section)
        return
    }

    override fun build(player: Player?, compound: ItemTag) {
        nativeNBT.forEach { t, u ->
            compound[t] = u
        }
    }

    override fun toString(): String {
        return "MetaNative(nativeNBT=$nativeNBT)"
    }


}