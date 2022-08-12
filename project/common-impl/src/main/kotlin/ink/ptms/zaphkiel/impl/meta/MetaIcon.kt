package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.api.event.ItemReleaseEvent
import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.parseToMaterial
import taboolib.module.nms.ItemTag

@MetaKey("icon")
class MetaIcon(root: ConfigurationSection) : Meta(root) {

    val icon = root.getString("meta.icon")?.run { parseToMaterial() } ?: Material.STONE

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        return
    }

    override fun build(itemReleaseEvent: ItemReleaseEvent) {
        itemReleaseEvent.icon = icon
    }

    override fun drop(itemReleaseEvent: ItemReleaseEvent) {
        itemReleaseEvent.icon = itemReleaseEvent.item.icon.type
    }

    override fun toString(): String {
        return "MetaIcon(icon=$icon)"
    }
}