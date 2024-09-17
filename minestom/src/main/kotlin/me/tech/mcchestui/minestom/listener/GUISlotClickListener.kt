package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent

internal class GUISlotClickListener(
    gui: MinestomGUI
) : GUIListener(gui) {
    override fun listener() = EventListener.builder(InventoryClickEvent::class.java)
//        .filter { it.inventory === gui.inventory.minestomInventory }
        .ignoreCancelled(true)
        .handler { it.handler() }
        .build()

    private fun InventoryClickEvent.handler() {
        println("slot = ${gui.slots.getOrNull(slot)}")
        val guiSlot = gui.slots.getOrNull(slot)
            ?: return

        guiSlot.onClick?.let { dispatcher ->
            dispatcher(this, player)
        }
//        guiSlot.onClick?.let { dispatcher ->
//            dispatcher(this, player)
//        }
    }
}