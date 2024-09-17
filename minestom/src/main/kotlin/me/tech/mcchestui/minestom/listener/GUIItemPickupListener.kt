package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.GUI
import me.tech.mcchestui.minestom.MinestomGUI
import me.tech.mcchestui.minestom.event.MinestomGUISlotClickEvent
import me.tech.mcchestui.minestom.isPlayerInventory
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryPreClickEvent

internal class GUIItemPickupListener(gui: MinestomGUI) : GUIListener(gui) {
    override fun listener() = EventListener.builder(InventoryPreClickEvent::class.java)
        .filter { it.inventory == gui.inventory.minestomInventory }
        .handler { it.guiItemPickup() }
        .build()

    private fun InventoryPreClickEvent.guiItemPickup() {
        val guiSlot = gui.slots.getOrNull(slot)
        if(guiSlot != null) {
            // we need to dispatch this event here
            // because if this is cancelled then
            // InventoryClickEvent will literally never run.
            GUI.GUI_EVENT_NODE
                .call(MinestomGUISlotClickEvent.from(
                    gui,
                    preClick = this
                ))

            if(!guiSlot.allowPickup) {
                isCancelled = true
                return
            }
        }

        val itemStack = clickedItem
        if(itemStack.isAir) {
            return
        }

        gui.onPickupItem?.let { dispatcher ->
            dispatcher(this, player, itemStack, slot).let {
                cancelled -> isCancelled = cancelled
            }
        }
    }
}

internal class GUIPlayerInventoryPickupListener(gui: MinestomGUI) : GUIListener(gui) {
    override fun listener() = EventListener.builder(InventoryPreClickEvent::class.java)
        .filter {
            it.player.openInventory == gui.inventory.minestomInventory
                    && it.inventory.isPlayerInventory()
        }
        .handler { it.playerInventoryItemPickup() }
        .build()

    private fun InventoryPreClickEvent.playerInventoryItemPickup() {
        val itemStack = clickedItem
        if(itemStack.isAir) {
            return
        }

        gui.onPlayerInventoryPickupItem?.let { dispatcher ->
            dispatcher(this, player, itemStack, slot).let {
                cancelled -> isCancelled = cancelled
            }
        }
    }
}