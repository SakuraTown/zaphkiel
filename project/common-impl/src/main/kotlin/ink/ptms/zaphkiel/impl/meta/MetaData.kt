package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.api.event.ItemReleaseEvent
import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

@MetaKey("data")
class MetaData(root: ConfigurationSection) : Meta(root) {

    val data = root.getInt("meta.data")

    override fun build(itemReleaseEvent: ItemReleaseEvent) {
        itemReleaseEvent.data = data
    }

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        return
    }

    override fun drop(itemReleaseEvent: ItemReleaseEvent) {
        itemReleaseEvent.data = 0
    }

    override fun toString(): String {
        return "MetaData(data=$data)"
    }
}