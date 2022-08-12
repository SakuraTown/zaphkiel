package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.Color
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import taboolib.common5.Coerce
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

@MetaKey("color")
class MetaColor(root: ConfigurationSection) : Meta(root) {

    val color = root.getString("meta.color")?.split("-")?.run {
        Color.fromRGB(
            Coerce.toInteger(getOrElse(0) { 0 }),
            Coerce.toInteger(getOrElse(1) { 0 }),
            Coerce.toInteger(getOrElse(2) { 0 })
        )
    }

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        val color: Color = if (itemMeta is PotionMeta)
            itemMeta.color ?: return
        else if (itemMeta is LeatherArmorMeta)
            itemMeta.color
        else return
        root["color"] = "${color.red}-${color.green}-${color.blue}"
    }

    override fun build(itemMeta: ItemMeta) {
        if (itemMeta is PotionMeta) {
            itemMeta.color = color ?: return
        } else if (itemMeta is LeatherArmorMeta) {
            itemMeta.setColor(color ?: return)
        }
    }

    override fun drop(itemMeta: ItemMeta) {
        if (itemMeta is PotionMeta) {
            itemMeta.color = null
        } else if (itemMeta is LeatherArmorMeta) {
            itemMeta.setColor(null)
        }
    }

    override fun toString(): String {
        return "MetaColor(color=$color)"
    }
}