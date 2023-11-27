package me.tech.mcchestui.listeners.hotbar

import me.tech.mcchestui.GUI
import me.tech.mcchestui.listeners.GUIEventListener
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUIHotbarListener(gui: GUI) : GUIEventListener(gui) {
    @EventHandler(ignoreCancelled = true)
    internal fun InventoryClickEvent.hotBarSwitchToUIInventory() {
        // we dont need hotbar swaps between two guis.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(gui.hasAttachedGui) {
            isCancelled = true
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
    internal fun InventoryClickEvent.hotBarSwitchToPlayerInventory() {
        // we dont need hotbar swaps between two guis.
        if(!gui.isBukkitInventory(inventory)) {
            return
        }

        if(gui.hasAttachedGui) {
            isCancelled = true
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
}