package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.trait.InventoryEvent

internal class GUIListener(
    private val gui: MinestomGUI
) {
    private val listenerPredicate = { ev: InventoryEvent -> ev.inventory === gui.inventory.minestomInventory }

    fun slotClickNode() = EventListener.builder(InventoryClickEvent::class.java)
        .filter(listenerPredicate)
        .handler {
            println(it.slot)
            val guiSlot = gui.slots.getOrNull(it.slot)
                ?: return@handler // handle cancel in onPlace

            guiSlot.onClick?.let { dispatcher ->
                dispatcher(it, it.player)
            }
        }
        .build()

    fun closeNode() = EventListener.builder(InventoryCloseEvent::class.java)
        .filter(listenerPredicate)
        .ignoreCancelled(true) // cancelled by pickup
        .handler {
            if(it.inventory == null) {
                return@handler
            }
            gui.onCloseInventory?.let { dispatcher ->
                dispatcher(it, it.player)
            }

            MinecraftServer.getSchedulerManager().scheduleNextTick {
                it.inventory?.let { inventory ->
                    if(inventory.viewers.size != 0 || gui.singleInstance) {
                        return@scheduleNextTick
                    }

                    gui.unregister()
                }
            }
        }
        .build()

    fun itemPickupNode() = EventListener.builder(InventoryPreClickEvent::class.java)
        .filter(listenerPredicate)
        .handler {

        }
        .build()
}