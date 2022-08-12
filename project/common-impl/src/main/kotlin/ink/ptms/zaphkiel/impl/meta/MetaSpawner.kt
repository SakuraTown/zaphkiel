package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.item.meta.MetaKey
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

/**
 * @author Administrator
 * @since 2019-12-26 17:12
 */
@MetaKey("spawner")
class MetaSpawner(root: ConfigurationSection) : Meta(root) {

    val type = root.getString("meta.spawner").toString()

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (itemMeta !is BlockStateMeta) return
        val blockState = itemMeta.blockState
        if (blockState !is CreatureSpawner) return
        root[key] = blockState.spawnedType.name
        return
    }

    override fun build(itemMeta: ItemMeta) {
        if (itemMeta !is BlockStateMeta) return
        val blockState = itemMeta.blockState
        if (blockState !is CreatureSpawner) return
        blockState.spawnedType =
            kotlin.runCatching { EntityType.valueOf(type.uppercase()) }.getOrElse { EntityType.VILLAGER }
        itemMeta.blockState = blockState
    }

    override fun drop(itemMeta: ItemMeta) {
    }

    override fun toString(): String {
        return "MetaSpawnerEgg(type='$type')"
    }
}