package me.tech.mcchestui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory

internal class GUIListener(private val gui: GUI): Listener {
    /**
     * When a [GUI.Slot] is clicked in the [Inventory].
     *
     * This listener should not be used to modify the [Cancellable] of
     * the bukkit event, handle that in the onPickup event.
     */
    @EventHandler
    internal fun InventoryClickEvent.onSlotClick() {
        // ensure clicked inventory is ui inventory.
        if(!gui.isBukkitInventory(clickedInventory)) {
            return
        }

        val guiSlot = gui.slots.getOrNull(slot)
            ?: return // handle cancellation of task in onPlace.

        guiSlot.onClick?.let { uiEvent ->
            uiEvent(this, whoClicked as Player)
        }
    }

    @EventHandler
    internal fun InventoryClickEvent.onItemPlace() {
        // ensure top inventory is ui inventory.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(
            action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && isShiftClick
            && !gui.isBukkitInventory(clickedInventory) // make sure its incoming.
        ) {
            if(!gui.allowItemPlacement) {
                isCancelled = true
                return
            }
        } else if(
            action in PLACE_ACTIONS
            && gui.isBukkitInventory(clickedInventory)
        ) {
            if(!gui.allowItemPlacement) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val guiSlot = gui.slots.getOrNull(slot)
        if(guiSlot != null) {
            if(!guiSlot.allowPickup) {
                isCancelled = true
                return
            }
        }

        val itemStack = cursor
            ?: return
        if(itemStack.type == Material.AIR) {
            return
        }

        gui.onPlaceItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }

    @EventHandler
    internal fun InventoryDragEvent.onItemDrag() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(!gui.allowItemPlacement) {
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

    @EventHandler(ignoreCancelled = true)
    internal fun InventoryClickEvent.onHotBarSwitchToUIInventory() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        // player inv -> gui inv
        if(
            action !in HOTBAR_ACTIONS
            || click != ClickType.NUMBER_KEY
        ) {
            return
        }

        if(!gui.allowHotBarSwap) {
            if(gui.isBukkitInventory(clickedInventory)) {
                isCancelled = true
            }

            return
        }

        if(clickedInventory == whoClicked.inventory) {
            return
        }

        val itemStack = if(click == ClickType.NUMBER_KEY) {
            whoClicked.inventory.getItem(hotbarButton)
        } else {
            currentItem
        } ?: return

        if(itemStack.type == Material.AIR) {
            return
        }

        gui.onPlaceItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    internal fun InventoryClickEvent.onHotBarSwitchToPlayerInventory() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        // gui inv -> player inv
        if(
            action !in HOTBAR_ACTIONS
            || click != ClickType.NUMBER_KEY
        ) {
            return
        }

        if(!gui.allowHotBarSwap) {
            if(gui.isBukkitInventory(clickedInventory)) {
                isCancelled = true
            }

            return
        }

        if(clickedInventory == whoClicked.inventory) {
            return
        }

        val itemStack = currentItem
            ?: return
        if(itemStack.type == Material.AIR) {
            return
        }

        gui.onPickupItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }

    @EventHandler
    internal fun InventoryClickEvent.onItemPickup() {
        // ensure top inventory is ui inventory.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        // handle shift click
        if(
            action == InventoryAction.MOVE_TO_OTHER_INVENTORY
            && isShiftClick
            && gui.isBukkitInventory(clickedInventory) // make sure its outgoing.
        ) {
            if(!gui.allowItemPickup) {
                isCancelled = true
                return
            }
        } else if(
            action in PICKUP_ACTIONS
            && gui.isBukkitInventory(clickedInventory)
        ) {
            if(!gui.allowItemPickup) {
                isCancelled = true
                return
            }
        } else {
            return
        }

        val guiSlot = gui.slots.getOrNull(slot)
        if(guiSlot != null) {
            if(!guiSlot.allowPickup) {
                isCancelled = true
                return
            }
        }

        val itemStack = currentItem
            ?: return
        if(itemStack.type == Material.AIR) {
            return
        }

        gui.onPickupItem?.let { uiEvent ->
            uiEvent(this, whoClicked as Player, itemStack, slot).let { outcome ->
                isCancelled = outcome
            }
        }
    }

    @EventHandler
    internal fun InventoryCloseEvent.onClose() {
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        gui.onCloseInventory?.let { uiEvent ->
            uiEvent(this, player)
        }

        if(gui.singleInstance) {
            return
        }

        if(inventory.viewers.size > 1) {
            return
        }

        gui.unregister()
    }

    companion object {
        /**
         * All actions related to placing an item.
         */
        private val PLACE_ACTIONS = listOf(
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ALL,
            InventoryAction.SWAP_WITH_CURSOR
        )

        /**
         * All actions related to using hotkeys to place/pickup
         * an item.
         */
        private val HOTBAR_ACTIONS = listOf(
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.HOTBAR_MOVE_AND_READD
        )

        /**
         * All actions related to picking up an item.
         */
        private val PICKUP_ACTIONS = listOf(
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ALL,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.COLLECT_TO_CURSOR
        )
    }
}