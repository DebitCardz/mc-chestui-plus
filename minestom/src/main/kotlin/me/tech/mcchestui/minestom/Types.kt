package me.tech.mcchestui.minestom

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.minestom.item.MinestomGUIItem
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack

typealias GUI = MinestomGUI

internal typealias AbstractMinestomGUI = AbstractGUI<MinestomGUIItem, GUISlotClickEvent>

// concrete gui type.
/** Render function for [MinestomGUI]. */
internal typealias MinestomGUIRender = MinestomGUI.() -> Unit

internal typealias GUIItemPickupEvent = InventoryPreClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

internal typealias GUIItemPlaceEvent = InventoryPreClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

internal typealias GUICloseEvent = InventoryCloseEvent.(player: Player) -> Unit

internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit