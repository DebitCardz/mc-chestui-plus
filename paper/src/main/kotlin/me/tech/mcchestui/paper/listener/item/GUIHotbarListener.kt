package me.tech.mcchestui.paper.listener.item

import me.tech.mcchestui.paper.PaperGUI
import me.tech.mcchestui.paper.listener.GUIEventListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

internal class GUIHotbarListener(gui: PaperGUI) : GUIEventListener(gui) {
    @EventHandler(ignoreCancelled = true)
    internal fun hotBarSwitchToUIInventory(ev: InventoryClickEvent) {
        // we dont need hotbar swaps between two guis.
        if(!isSame(ev.inventory)) {
            return
        }

//        if(gui.hasAttachedGui) {
//            isCancelled = true
//            return
//        }

        // player inv -> gui inv
        if(
            ev.action !in HOTBAR_ACTIONS
            || ev.click != ClickType.NUMBER_KEY
        ) {
            return
        }

        if(!gui.allowHotBarSwap) {
            if(isSame(ev.clickedInventory)) {
                ev.isCancelled = true
            }

            return
        }

        if(ev.clickedInventory == ev.whoClicked.inventory) {
            return
        }

        val itemStack = if(ev.click == ClickType.NUMBER_KEY) {
            ev.whoClicked.inventory.getItem(ev.hotbarButton)
        } else {
            ev.currentItem
        } ?: return

        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPlaceItem?.let { dispatcher ->
            dispatcher(ev, ev.whoClicked as Player, itemStack, ev.slot).let { cancelled ->
                ev.isCancelled = cancelled
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    internal fun hotBarSwitchToPlayerInventory(ev: InventoryClickEvent) {
        // we dont need hotbar swaps between two guis.
        if(!isSame(ev.inventory)) {
            return
        }

//        if(gui.hasAttachedGui) {
//            isCancelled = true
//            return
//        }

        // gui inv -> player inv
        if(
            ev.action !in HOTBAR_ACTIONS
            || ev.click != ClickType.NUMBER_KEY
        ) {
            return
        }

        if(!gui.allowHotBarSwap) {
            if(isSame(ev.clickedInventory)) {
                ev.isCancelled = true
            }

            return
        }

        if(ev.clickedInventory == ev.whoClicked.inventory) {
            return
        }

        val itemStack = ev.currentItem
            ?: return
        if(itemStack.type.isEmpty) {
            return
        }

        gui.onPickupItem?.let { dispatcher ->
            dispatcher(ev, ev.whoClicked as Player, itemStack, ev.slot).let { cancelled ->
                ev.isCancelled = cancelled
            }
        }
    }
}