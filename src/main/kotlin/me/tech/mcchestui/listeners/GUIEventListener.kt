package me.tech.mcchestui.listeners

import me.tech.mcchestui.GUI
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction

internal abstract class GUIEventListener(
    protected val gui: GUI
) : Listener {
    companion object {
        /**
         * All actions related to placing an item.
         */
        @JvmStatic
        protected val PLACE_ACTIONS = listOf(
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ALL,
            InventoryAction.SWAP_WITH_CURSOR
        )

        /**
         * All actions related to using hotkeys to place/pickup
         * an item.
         */
        @JvmStatic
        protected val HOTBAR_ACTIONS = listOf(
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD
        )

        /**
         * All actions related to picking up an item.
         */
        @JvmStatic
        protected val PICKUP_ACTIONS = listOf(
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ALL,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.COLLECT_TO_CURSOR
        )
    }
}