package ink.ptms.zaphkiel.api.event

import ink.ptms.zaphkiel.api.ItemStream
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author sky
 * @since 2020-04-20 12:37
 */
class ItemEvent {

    class InventoryClick(val itemStreamCurrent: ItemStream?, val itemStreamButton: ItemStream?, val bukkitEvent: InventoryClickEvent) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        var saveCurrent = false
        var saveButton = false
    }

    class InteractEntity(val itemStream: ItemStream, val bukkitEvent: PlayerInteractEntityEvent) : BukkitProxyEvent() {

        var save = false
    }

    class Interact(val itemStream: ItemStream, val bukkitEvent: PlayerInteractEvent) : BukkitProxyEvent() {

        var save = false
    }

    class Consume(val itemStream: ItemStream, val bukkitEvent: PlayerItemConsumeEvent) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false
    }

    class Pick(val itemStream: ItemStream, val bukkitEvent: PlayerPickupItemEvent) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        var save = false
    }

    class Drop(val itemStream: ItemStream, val bukkitEvent: PlayerDropItemEvent) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        var save = false
    }

    class Select(val itemStream: ItemStream, val player: Player) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        var save = false
    }

    class AsyncTick(val itemStream: ItemStream, val player: Player) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        var save = false
    }
}