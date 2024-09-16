package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.inventory.click.ClickType

internal class GUIListener(
    private val gui: MinestomGUI
) {
    private val listenerPredicate = { ev: InventoryEvent -> ev.inventory === gui.inventory.minestomInventory }

    fun slotClickNode() = EventListener.builder(InventoryClickEvent::class.java)
        .filter(listenerPredicate)
        .handler {
            val guiSlot = gui.slots.getOrNull(it.slot)
                ?: return@handler // handle cancel in onPlace
        }
        .build()

    fun closeNode() = EventListener.builder(InventoryCloseEvent::class.java)
        .filter(listenerPredicate)
        .handler {
            if(it.inventory == null) {
                return@handler
            }
            gui.onCloseInventory?.let { dispatcher ->
                dispatcher(it, it.player)
            }

            it.inventory?.let { inventory ->
                if(inventory.viewers.size != 0 || gui.singleInstance) {
                    return@handler
                }

                gui.unregister()
            }
        }
        .build()

    internal companion object {
//        /** All actions related to placing an item. */
//        @JvmStatic
//        protected val PLACE_ACTIONS = listOf(
//            InventoryAction.PLACE_ONE,
//            InventoryAction.PLACE_SOME,
//            InventoryAction.PLACE_ALL,
//            InventoryAction.SWAP_WITH_CURSOR
//        )
//
//        /**
//         * All actions related to using hotkeys to place/pickup
//         * an item.
//         */
//        @JvmStatic
//        protected val HOTBAR_ACTIONS = listOf(
//            InventoryAction.HOTBAR_SWAP,
//            InventoryAction.HOTBAR_MOVE_AND_READD
//        )
//
//        /** All actions related to picking up an item. */
//        @JvmStatic
//        protected val PICKUP_ACTIONS = listOf(
//            InventoryAction.PICKUP_ONE,
//            InventoryAction.PICKUP_SOME,
//            InventoryAction.PICKUP_HALF,
//            InventoryAction.PICKUP_ALL,
//            InventoryAction.SWAP_WITH_CURSOR,
//            InventoryAction.COLLECT_TO_CURSOR
//        )

        /** Actions preformed when a player clicks a slot. */
        @JvmStatic
        protected val PICKUP_CLICK_ACTIONS = setOf(
            ClickType.DROP,
//            ClickType.CONTROL_DROP,
//            ClickType.SWAP_OFFHAND
        )
    }
}