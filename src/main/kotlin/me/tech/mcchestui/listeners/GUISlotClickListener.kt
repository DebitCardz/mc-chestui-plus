package me.tech.mcchestui.listeners

import me.tech.mcchestui.GUI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
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

        guiSlot.onClick?.let { uiEvent ->
            uiEvent(this, whoClicked as Player)
        }
    }
}