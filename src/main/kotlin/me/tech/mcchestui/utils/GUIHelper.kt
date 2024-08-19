package me.tech.mcchestui.utils

import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/**
 * Create a GUI.
 *
 * @param plugin
 * @param title Title of the GUI
 * @param type Type of GUI to generate
 * @return [GUI] Object
 */
fun gui(
	plugin: JavaPlugin,
	title: Component,
	type: GUIType,
	render: GUI.() -> Unit
): GUI {
	return GUI(plugin, title, type, type.createBukkitInventory(title), false, render).apply(render)
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
 * Anonymous function to construct new [GUI].
 */
typealias GUIBuilder = GUI.() -> Unit

/**
 * Event when an [ItemStack] interaction is preformed with a [GUI].
 */
internal typealias GUIItemPlaceEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

/**
 * Event when a [ItemStack] or [GUI.Slot] is picked up.
 */
internal typealias GUIItemPickupEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

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
