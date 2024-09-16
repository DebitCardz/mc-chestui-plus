package me.tech.mcchestui.paper.listener.item

import me.tech.mcchestui.paper.PaperGUI
import me.tech.mcchestui.paper.listener.GUIEventListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUIItemPickupListener(gui: PaperGUI) : GUIEventListener(gui) {
    @EventHandler
    fun onItemPickup(ev: InventoryClickEvent) {
        if(!isSame(ev.inventory)) {
            return
        }

        // TODO: check if has attached gui then cancel.

        // handle player inventory item pickups.
        if(ev.clickedInventory == ev.whoClicked.inventory) {
            val itemStack = ev.currentItem
                ?: return
            if(itemStack.type.isEmpty) {
                return
            }

            gui.onPlayerInventoryPickupItem?.let { dispatcher ->
                dispatcher(ev, ev.whoClicked as Player, itemStack, ev.slot).let {
                    cancelled -> ev.isCancelled = cancelled
                }
            }
        }

        // handle shift click
        if(
            ev.action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && ev.isShiftClick
            && isSame(ev.clickedInventory) // make sure its outgoing.
        ) {
            if(!gui.allowItemPickup) {
                ev.isCancelled = true
                return
            }
        } else if(
            ev.action in PICKUP_ACTIONS
            && isSame(ev.clickedInventory)
        ) {
            if(!gui.allowItemPickup) {
                ev.isCancelled = true
                return
            }
        } else {
            return
        }

        val guiSlot = gui.slots.getOrNull(ev.slot)
        if(guiSlot != null) {
            if(!guiSlot.allowPickup) {
                ev.isCancelled = true
                return
            }
        }

        val itemStack = ev.currentItem
            ?: return
        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPickupItem?.let { dispatcher ->
            dispatcher(ev, ev.whoClicked as Player, itemStack, ev.slot).let { cancelled ->
                ev.isCancelled = cancelled
            }
        }
    }
}