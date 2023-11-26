package me.tech.mcchestui.listeners

import me.tech.mcchestui.GUI
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryCloseEvent

internal class GUICloseListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryCloseEvent.onClose() {
        if(!gui.isBukkitInventory(inventory)) {
            return
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