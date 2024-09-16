package me.tech.mcchestui.paper

import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.paper.item.PaperGUIItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

/** Abstract GUI type with Paper generics. */
internal typealias AbstractPaperGUI = GUI<PaperGUIItem, GUISlotClickEvent>

// concrete gui type.
/** Render function for [PaperGUI]. */
internal typealias PaperGUIRender = PaperGUI.() -> Unit

/** Dispatched when a [ItemStack] or [GUI.Slot] is picked up. */
internal typealias GUIItemPickupEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

/** Event when an [ItemStack] interaction is preformed with a [GUI]. */
internal typealias GUIItemPlaceEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

/** Dispatched when a [GUI] is closed. */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: Player) -> Unit

/** Dispatched when a [GUI.Slot] is clicked. */
internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit