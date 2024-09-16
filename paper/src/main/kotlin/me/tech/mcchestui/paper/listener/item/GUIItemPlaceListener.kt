package me.tech.mcchestui.paper.listener.item

import me.tech.mcchestui.paper.PaperGUI
import me.tech.mcchestui.paper.listener.GUIEventListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent


internal class GUIItemPlaceListener(gui: PaperGUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun itemPlace(ev: InventoryClickEvent) {
        // ensure top inventory is ui inventory.
        if(!isSame(ev.inventory)) {
            return
        }

        // TODO: attached
//        if(gui.hasAttachedGui) {
//            isCancelled = true
//            return
//        }

        if(
            ev.action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && ev.isShiftClick
            && !isSame(ev.clickedInventory) // make sure its incoming.
        ) {
            if(!gui.allowItemPlacement || !gui.allowShiftClick) {
                ev.isCancelled = true
                return
            }
        } else if(
            ev.action in PLACE_ACTIONS
            && isSame(ev.clickedInventory)
        ) {
            if(!gui.allowItemPlacement) {
                ev.isCancelled = true
                return
            }
        } else {
            return
        }

        val originatesFromPlayerInventory = !isSame(ev.clickedInventory)
        if(!originatesFromPlayerInventory) {
            val guiSlot = gui.slots.getOrNull(ev.slot)
            if(guiSlot != null) {
                if(!guiSlot.allowPickup) {
                    ev.isCancelled = true
                    return
                }
            }
        }

        val itemStack = (if(ev.isShiftClick) ev.currentItem else ev.cursor)
            ?: return
        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPlaceItem?.let { dispatcher ->
            dispatcher(ev, ev.whoClicked as Player, itemStack, ev.slot).let { cancelled ->
                ev.isCancelled = cancelled
            }
        }
    }
}