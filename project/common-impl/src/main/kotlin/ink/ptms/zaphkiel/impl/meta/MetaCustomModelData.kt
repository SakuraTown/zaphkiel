package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

@MetaKey("custom-model-data")
class MetaCustomModelData(root: ConfigurationSection) : Meta(root) {

    val data = root.getInt("meta.custom-model-data")

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (!itemMeta.hasCustomModelData()) return
        root[key] = itemMeta.customModelData
    }

    override fun build(itemMeta: ItemMeta) {
        itemMeta.setCustomModelData(data)
    }

    override fun drop(itemMeta: ItemMeta) {
        itemMeta.setCustomModelData(null)
    }

    override fun toString(): String {
        return "MetaCustomModel(data=$data)"
    }
}