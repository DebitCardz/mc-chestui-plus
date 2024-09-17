package me.tech.mcchestui.minestom.event

import me.tech.mcchestui.minestom.GUI
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.item.ItemStack

internal class MinestomGUISlotClickEvent(
    gui: GUI,
    player: Player,
    slot: Int,
    clickType: ClickType,
    clicked: ItemStack,
    cursor: ItemStack
) : InventoryClickEvent(gui.inventory.minestomInventory, player, slot, clickType, clicked, cursor) {
    companion object {
        fun from(gui: GUI, preClick: InventoryPreClickEvent) = MinestomGUISlotClickEvent(
            gui,
            preClick.player,
            preClick.slot,
            preClick.clickType,
            preClick.clickedItem,
            preClick.cursorItem
        )
    }
}
