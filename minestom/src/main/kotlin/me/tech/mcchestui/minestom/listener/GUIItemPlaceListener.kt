package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.minestom.MinestomGUI
import me.tech.mcchestui.minestom.isPlayerInventory
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.click.ClickType

internal class GUIItemPlaceListener(gui: MinestomGUI) : GUIListener(gui) {
    override fun listener() = EventListener.builder(InventoryPreClickEvent::class.java)
        .filter { it.player.openInventory === gui.inventory.minestomInventory }
        .ignoreCancelled(true)
        .handler { it.guiItemPlace() }
        .build()

    private fun InventoryPreClickEvent.guiItemPlace() {
        if(
            !inventory.isPlayerInventory()
            && clickType.name.endsWith("DRAGGING", true)
            && !gui.allowItemDrag
        ) {
            isCancelled = true
            return
        }

        if(
            clickType == ClickType.START_SHIFT_CLICK
            && inventory.isPlayerInventory()
        ) {

            if(!gui.allowItemPlacement || !gui.allowShiftClick) {
                isCancelled = true
                return
            }
        } else if(
            !inventory.isPlayerInventory()
        ) {
            if(!gui.allowItemPlacement) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val originatesFromPlayerInventory = inventory.isPlayerInventory()
        if(!originatesFromPlayerInventory) {
            val guiSlot = gui.slots.getOrNull(slot)
            if(guiSlot != null) {
                if(!guiSlot.allowPickup) {
                    isCancelled = true
                    return
                }
            }
        }

        val itemStack = if(clickType == ClickType.SHIFT_CLICK) clickedItem else cursorItem
        if(itemStack.isAir) {
            return
        }

        gui.onPlaceItem?.let { dispatcher ->
            dispatcher(this, player, itemStack, slot).let {
                cancelled -> isCancelled = cancelled.isCancelled
            }
        }
    }
}