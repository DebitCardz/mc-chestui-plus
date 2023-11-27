package me.tech.mcchestui.listeners.item

import me.tech.mcchestui.GUI
import me.tech.mcchestui.listeners.GUIEventListener
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*

internal class GUIItemDragListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler
    internal fun InventoryDragEvent.itemDrag() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(!gui.allowItemPlacement || gui.hasAttachedGui) {
            isCancelled = true
            return
        }

        // single item drag handler, move to onPlaceItem event.
        if(rawSlots.size == 1 && newItems.size == 1) {
            val slotIndex = rawSlots.first()

            val guiSlot = gui.slots.getOrNull(slotIndex)
            if(guiSlot != null) {
                if(!guiSlot.allowPickup) {
                    isCancelled = true
                    return
                }
            }

            val itemStack = newItems.values.firstOrNull()
                ?: return
            if(itemStack.type == Material.AIR) {
                return
            }

            gui.onPlaceItem?.let { uiEvent ->
                // TODO: Look for better method of handling this.
                val fakeEvent = InventoryClickEvent(
                    view,
                    InventoryType.SlotType.CONTAINER,
                    slotIndex,
                    if(type == DragType.SINGLE) ClickType.RIGHT else ClickType.LEFT,
                    InventoryAction.PLACE_ALL
                )

                uiEvent(fakeEvent, whoClicked as Player, itemStack, slotIndex).let { outcome ->
                    isCancelled = outcome
                }
            }
            return
        }

        if(!gui.allowItemDrag) {
            isCancelled = true
            return
        }

        // ensure our drag doesn't override a slot.
        if(newItems.any { (index, _) ->
                val guiSlot = gui.slots.getOrNull(index)
                    ?: return@any false

                !guiSlot.allowPickup
            }) {
            isCancelled = true
            return
        }

        gui.onDragItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, newItems).let { outcome ->
                isCancelled = outcome
            }
        }
    }
}