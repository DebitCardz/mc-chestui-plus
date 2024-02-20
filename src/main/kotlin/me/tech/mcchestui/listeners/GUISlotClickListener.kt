package me.tech.mcchestui.listeners

import me.tech.mcchestui.GUI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUISlotClickListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryClickEvent.slotClick() {
        // ensure clicked inventory is ui inventory.
        if(!gui.isBukkitInventory(clickedInventory)) {
            return
        }

        val guiSlot = gui.slots.getOrNull(slot)
            ?: return // handle cancellation of task in onPlace.

        if(click in PICKUP_CLICK_ACTIONS) {
            if(!gui.allowItemPickup) {
                isCancelled = true
                return
            }
        }

        guiSlot.onClick?.let { uiEvent ->
            uiEvent(this, whoClicked as Player)
        }
    }

    companion object {
        private val PICKUP_CLICK_ACTIONS = setOf(
            ClickType.DROP,
            ClickType.CONTROL_DROP,
            ClickType.SWAP_OFFHAND
        )
    }
}