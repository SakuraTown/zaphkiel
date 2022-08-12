package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag
import taboolib.platform.util.deserializeToInventory
import taboolib.platform.util.serializeToByteArray
import java.util.*

@MetaKey("inventory")
class MetaInventory(root: ConfigurationSection) : Meta(root) {
    private var bytes: ByteArray? = null
    private var temp: Array<ItemStack>? = null

    init {
        val string = root.getString("meta.inventory")
        if (string != null) bytes = Base64.getDecoder().decode(string)
    }

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (itemMeta !is BlockStateMeta) return
        val blockState = itemMeta.blockState
        if (blockState !is BlockInventoryHolder) return
        val string = blockState.inventory.serializeToByteArray(zipped = true)
        root[key] = Base64.getEncoder().encodeToString(string)
    }

//    override val id: String
//        get() = "inventory"

    override fun build(itemMeta: ItemMeta) {
        if (bytes == null) return
        if (itemMeta !is BlockStateMeta) return
        val blockState = itemMeta.blockState
        if (blockState !is BlockInventoryHolder) return
        if (temp == null) {
            bytes!!.deserializeToInventory(blockState.inventory, true)
            temp = blockState.inventory.contents
        } else {
            blockState.inventory.contents = temp!!
        }
        itemMeta.blockState = blockState
    }

    override fun drop(itemMeta: ItemMeta) {
        if (itemMeta !is BlockStateMeta) return
        val blockState = itemMeta.blockState
        if (blockState !is BlockInventoryHolder) return
        blockState.inventory.clear()
    }

    override fun toString(): String {
        return "MetaInventory(inventory=$bytes)"
    }
}