package me.tech.mcchestui.listeners.item

import me.tech.mcchestui.GUI
import me.tech.mcchestui.listeners.GUIEventListener
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUIItemPickupListener(gui: GUI): GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryClickEvent.onItemPickup() {
        // ensure top inventory is ui inventory.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(gui.hasAttachedGui) {
            isCancelled = true
            return
        }

        if(clickedInventory == whoClicked.inventory) {
            if(currentItem == null || currentItem?.type?.isEmpty == true) {
                return
            }

            gui.onPlayerInventoryPickupItem?.let { uiEvent ->
                uiEvent(this, whoClicked as Player, currentItem, slot).let { outcome ->
                    isCancelled = outcome
                }
            }
        }

        // handle shift click
        if(
            action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && isShiftClick
            && gui.isBukkitInventory(clickedInventory) // make sure its outgoing.
        ) {
            if(!gui.allowItemPickup) {
                isCancelled = true
                return
            }
        } else if(
            action in PICKUP_ACTIONS
            && gui.isBukkitInventory(clickedInventory)
        ) {
            if(!gui.allowItemPickup) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val guiSlot = gui.slots.getOrNull(slot)
        if(guiSlot != null) {
            if(!guiSlot.allowPickup) {
                isCancelled = true
                return
            }
        }

        val itemStack = currentItem
            ?: return
        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPickupItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }
}