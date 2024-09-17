package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import me.tech.mcchestui.minestom.event.MinestomGUISlotClickEvent
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent

internal class GUISlotClickListener(
    gui: MinestomGUI
) : GUIListener(gui) {
    override fun listener() = EventListener.builder(MinestomGUISlotClickEvent::class.java)
        .filter { it.inventory === gui.inventory.minestomInventory }
        .ignoreCancelled(true)
        .handler { it.handler2() }
        .build()

    private fun MinestomGUISlotClickEvent.handler2() {
        val guiSlot = gui.slots.getOrNull(slot)
            ?: return

        guiSlot.onClick?.let { dispatcher ->
            dispatcher(this, player)
        }
    }
}