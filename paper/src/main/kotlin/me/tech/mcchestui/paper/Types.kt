package me.tech.mcchestui.paper

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.EventResult
import me.tech.mcchestui.paper.item.PaperGUIItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

typealias GUI = PaperGUI

/** Abstract GUI type with Paper generics. */
internal typealias AbstractPaperGUI = AbstractGUI<PaperGUIItem, GUISlotClickEvent>

// concrete gui type.
/** Render function for [PaperGUI]. */
internal typealias PaperGUIRender = PaperGUI.() -> Unit

/** Dispatched when a [ItemStack] or [AbstractGUI.Slot] is picked up. */
internal typealias GUIItemPickupEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> EventResult

/** Event when an [ItemStack] interaction is preformed with a [AbstractGUI]. */
internal typealias GUIItemPlaceEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> EventResult

/** Dispatched when a [AbstractGUI] is closed. */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: Player) -> Unit

/** Dispatched when a [AbstractGUI.Slot] is clicked. */
internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit