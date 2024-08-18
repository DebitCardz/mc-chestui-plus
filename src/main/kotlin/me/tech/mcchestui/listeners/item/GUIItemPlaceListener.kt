package me.tech.mcchestui.listeners.item

import me.tech.mcchestui.GUI
import me.tech.mcchestui.listeners.GUIEventListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUIItemPlaceListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryClickEvent.itemPlace() {
        // ensure top inventory is ui inventory.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(gui.hasAttachedGui) {
            isCancelled = true
            return
        }

        if(
            action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && isShiftClick
            && !gui.isBukkitInventory(clickedInventory) // make sure its incoming.
        ) {
            if(!gui.allowItemPlacement || !gui.allowShiftClick) {
                isCancelled = true
                return
            }
        } else if(
            action in PLACE_ACTIONS
            && gui.isBukkitInventory(clickedInventory)
        ) {
            if(!gui.allowItemPlacement) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val originatesFromPlayerInventory = !gui.isBukkitInventory(clickedInventory)
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