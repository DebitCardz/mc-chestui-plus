package me.tech.mcchestui.paper.listener

import me.tech.mcchestui.paper.PaperGUI
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.Inventory

internal abstract class GUIEventListener(protected val gui: PaperGUI) : Listener {
    fun isSame(other: Inventory?): Boolean {
        return gui.inventory.bukkitInventory === other
    }

    internal companion object {
        /** All actions related to placing an item. */
        @JvmStatic
        protected val PLACE_ACTIONS = listOf(
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ALL,
            InventoryAction.SWAP_WITH_CURSOR
        )

        /** All actions related to using hotkeys to place/pickup an item. */
        @JvmStatic
        protected val HOTBAR_ACTIONS = listOf(
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD
        )

        /** All actions related to picking up an item. */
        @JvmStatic
        protected val PICKUP_ACTIONS = listOf(
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ALL,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.COLLECT_TO_CURSOR
        )

        /** Actions preformed when a player clicks a slot. */
        @JvmStatic
        protected val PICKUP_CLICK_ACTIONS = setOf(
            ClickType.DROP,
            ClickType.CONTROL_DROP,
            ClickType.SWAP_OFFHAND
        )
    }
}