package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

@MetaKey("unbreakable")
class MetaUnbreakable(root: ConfigurationSection) : Meta(root) {

    val unbreakable = root.getBoolean("meta.unbreakable")

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (!itemMeta.isUnbreakable) return
        root[key] = true
        return
    }

    override fun build(itemMeta: ItemMeta) {
        itemMeta.isUnbreakable = unbreakable
    }

    override fun drop(itemMeta: ItemMeta) {
        itemMeta.isUnbreakable = false
    }

    override fun toString(): String {
        return "MetaUnbreakable(unbreakable=$unbreakable)"
    }
}