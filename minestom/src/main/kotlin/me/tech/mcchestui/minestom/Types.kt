package me.tech.mcchestui.minestom

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.EventResult
import me.tech.mcchestui.minestom.item.MinestomGUIItem
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack

typealias GUI = MinestomGUI

/** Abstract GUI type with Paper generics. */
internal typealias AbstractMinestomGUI = AbstractGUI<MinestomGUIItem, GUISlotClickEvent>

// concrete gui type.
/** Render function for [MinestomGUI]. */
internal typealias MinestomGUIRender = MinestomGUI.() -> Unit

/** Dispatched when a [ItemStack] or [AbstractGUI.Slot] is picked up. */
internal typealias GUIItemPickupEvent = InventoryPreClickEvent.(player: Player, item: ItemStack, slot: Int) -> EventResult

/** Event when an [ItemStack] interaction is preformed with a [AbstractGUI]. */
internal typealias GUIItemPlaceEvent = InventoryPreClickEvent.(player: Player, item: ItemStack, slot: Int) -> EventResult

/** Dispatched when a [AbstractGUI] is closed. */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: Player) -> Unit

/** Dispatched when a [AbstractGUI.Slot] is clicked. */
internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit