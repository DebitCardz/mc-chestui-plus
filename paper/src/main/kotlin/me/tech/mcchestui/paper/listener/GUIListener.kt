package me.tech.mcchestui.paper.listener

import me.tech.mcchestui.paper.PaperGUI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

internal class GUIListener(gui: PaperGUI) : GUIEventListener(gui) {
    @EventHandler
    fun onSlotClick(ev: InventoryClickEvent) {
        if(!isSame(ev.inventory)) {
            return
        }

        val guiSlot = gui.slots.getOrNull(ev.slot)
            ?: return // we need to handle cancelling the event in the onPlace listener.

        if(ev.click in PICKUP_CLICK_ACTIONS) {
            if(!gui.allowItemPickup) {
                ev.isCancelled = true
                return
            }
        }

        guiSlot.onClick?.let { dispatcher ->
            dispatcher(ev, ev.whoClicked as Player)
        }
    }

    @EventHandler
    fun onClose(ev: InventoryCloseEvent) {
        if(
            !isSame(ev.inventory)
            || ev.player.persistentDataContainer.has(PaperGUI.ignoreNamespace) // ignore close dispatcher.
        ) {
            return
        }

        // TODO: unregister attached ui.

        gui.onCloseInventory?.let { dispatcher ->
            dispatcher(ev, ev.player as Player)
        }

        // TODO: check single instance

        if(ev.inventory.viewers.size != 0 || gui.singleInstance) {
            return
        }

        gui.unregister()
    }
}