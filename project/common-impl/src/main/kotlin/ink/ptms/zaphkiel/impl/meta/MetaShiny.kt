package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

/**
 * @author Administrator
 * @since 2019-12-26 17:12
 */
@MetaKey("shiny")
class MetaShiny(root: ConfigurationSection) : Meta(root) {

    val shiny = root.getBoolean("meta.shiny")
    override val id: String
        get() = "shiny"

    override fun build(itemMeta: ItemMeta) {
        if (shiny) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        return
    }

    override fun drop(itemMeta: ItemMeta) {
        itemMeta.removeEnchant(Enchantment.ARROW_DAMAGE)
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
    }

    override fun toString(): String {
        return "MetaShiny(shiny=$shiny)"
    }
}