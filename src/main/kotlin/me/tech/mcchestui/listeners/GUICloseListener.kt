package me.tech.mcchestui.listeners

import me.tech.mcchestui.GUI
import me.tech.mcchestui.attached.restoreCachedInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent

internal class GUICloseListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryCloseEvent.onClose() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(gui.hasAttachedGui) {
            player.inventory.storageContents = emptyArray()

            // TODO: remove listeners from child to reduce load.
            gui.attachedGui?.unregister()

            restoreCachedInventory(player as Player, gui)
        }

        gui.onCloseInventory?.let { uiEvent ->
            uiEvent(this, player)
        }

        if(gui.singleInstance) {
            return
        }

        if(inventory.viewers.size > 1) {
            return
        }

        gui.unregister()
    }
}