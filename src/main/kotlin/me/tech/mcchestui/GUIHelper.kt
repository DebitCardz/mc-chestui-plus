package me.tech.mcchestui

import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/**
 * Create a GUI.
 *
 * @param plugin
 * @param title Title of the GUI
 * @param type Type of GUI to generate
 * @param rows Amount of rows in the Chest GUI
 * @return [GUI] Object
 */
fun gui(
	plugin: JavaPlugin,
	title: Component,
	type: GUIType,
	render: GUI.() -> Unit
): GUI {
	return GUI(plugin, title, type, render).apply(render)
}

/**
 * Open a [GUI] for a [HumanEntity].
 *
 * @param gui [GUI] to open.
 */
fun HumanEntity.openGUI(gui: GUI) {
	if(gui.unregistered) {
		throw IllegalStateException("unable to open unregistered ui.")
	}

	openInventory(gui.bukkitInventory)
}

/**
 * Prop for a [GUI] that is placed into a
 * [GUI.Slot] that usually has data passed into it
 * through a function.
 */
typealias GUIProp = GUI.Slot.() -> Unit

/**
 * Event when an [ItemStack] interaction is preformed with a [GUI].
 */
internal typealias GUIItemPlaceEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

/**
 * Event when a [ItemStack] or [GUI.Slot] is picked up.
 */
internal typealias GUIItemPickupEvent = InventoryClickEvent.(player: Player, item: ItemStack?, slot: Int) -> Boolean

/**
 * Event when an [ItemStack] is dragged across a [GUI].
 */
internal typealias GUIDragItemEvent = InventoryDragEvent.(player: Player, items: Map<Int, ItemStack>) -> Boolean

/**
 * Event when a [GUI.Slot] is clicked.
 */
internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit

/**
 * Event when a [GUI] is closed by a [Player].
 */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: HumanEntity) -> Unit