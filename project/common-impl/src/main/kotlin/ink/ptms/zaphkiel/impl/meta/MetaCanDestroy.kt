package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.util.asList
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagList

@MetaKey("can-destroy")
class MetaCanDestroy(root: ConfigurationSection) : Meta(root) {

    val canDestroy = root["meta.can-destroy"]?.asList()

    override val id: String
        get() = "can-destroy"

    override fun build(player: Player?, compound: ItemTag) {
        if (canDestroy == null || compound.containsKey("CanDestroy")) {
            return
        }
        compound.putDeep("CanDestroy", ItemTagList.of(*canDestroy.toTypedArray()))
    }

    override fun drop(player: Player?, compound: ItemTag) {
        compound.remove("CanDestroy")
    }

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (!compound.containsKey("CanDestroy")) return
        root[key] = compound["CanDestroy"]!!.asList().map { it.asString() }
    }

    override fun toString(): String {
        return "MetaCanDestroy(canDestroy=$canDestroy)"
    }
}