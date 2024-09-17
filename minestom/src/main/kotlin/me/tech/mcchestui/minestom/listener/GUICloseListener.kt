package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryCloseEvent

internal class GUICloseListener(
    gui: MinestomGUI
) : GUIListener(gui) {
    override fun listener() = EventListener.builder(InventoryCloseEvent::class.java)
        .filter { it.inventory === gui.inventory.minestomInventory }
        .handler { it.handler() }
        .build()

    private fun InventoryCloseEvent.handler() {
        gui.onCloseInventory?.let { dispatcher ->
            dispatcher(this, player)
        }

        MinecraftServer.getSchedulerManager().scheduleNextTick {
            inventory?.let { inv ->
                if(gui.singleInstance || inv.viewers.size != 0) {
                    return@scheduleNextTick
                }

                gui.unregister()
            }
        }
    }
}