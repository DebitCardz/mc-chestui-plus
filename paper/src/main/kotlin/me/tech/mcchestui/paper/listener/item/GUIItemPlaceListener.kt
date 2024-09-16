package me.tech.mcchestui.paper.listener.item

import me.tech.mcchestui.paper.PaperGUI
import me.tech.mcchestui.paper.listener.GUIEventListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent


internal class GUIItemPlaceListener(gui: PaperGUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryClickEvent.itemPlace() {
        // ensure top inventory is ui inventory.
        if(!isSame(inventory)) {
            return
        }

        // TODO: attached
//        if(gui.hasAttachedGui) {
//            isCancelled = true
//            return
//        }

        if(
            action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && isShiftClick
            && !isSame(clickedInventory) // make sure its incoming.
        ) {
            if(!gui.allowItemPlacement || !gui.allowShiftClick) {
                isCancelled = true
                return
            }
        } else if(
            action in PLACE_ACTIONS
            && isSame(clickedInventory)
        ) {
            if(!gui.allowItemPlacement) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val originatesFromPlayerInventory = !isSame(clickedInventory)
        if(!originatesFromPlayerInventory) {
            val guiSlot = gui.slots.getOrNull(slot)
            if(guiSlot != null) {
                if(!guiSlot.allowPickup) {
                    isCancelled = true
                    return
                }
            }
        }

        val itemStack = (if(isShiftClick) currentItem else cursor)
            ?: return
        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPlaceItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }
}