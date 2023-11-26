/**
 * @author hazae41
 * This GUI library is used from https://github.com/hazae41/mc-chestui
 * and has been heavily recoded to include features and
 * utilities the original did not have, and is still being actively maintained.
 */

package me.tech.mcchestui

import me.tech.mcchestui.item.GUIItem
import me.tech.mcchestui.listeners.GUIListener
import me.tech.mcchestui.utils.GUICloseEvent
import me.tech.mcchestui.utils.GUIDragItemEvent
import me.tech.mcchestui.utils.GUIItemPickupEvent
import me.tech.mcchestui.utils.GUIItemPlaceEvent
import me.tech.mcchestui.utils.GUISlotClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/**
 * Convert x/y coordinates to a slot index in
 * an [Inventory].
 *
 * @param x must be above 0 and less than slots per row.
 * @param y must be above 0 and less than rows.
 * @param type [GUIType] for the coordinates to map to.
 * @return [Int] representing the slot index.
 * @throws IllegalArgumentException if x or y coordinates are invalid.
 */
fun toSlot(x: Int, y: Int, type: GUIType): Int {
	if(x < 1 || y < 1) {
		throw IllegalArgumentException("x or y cannot be below 1.")
	}

	if(x > type.slotsPerRow) {
		throw IllegalArgumentException("x must be between 1 and ${type.slotsPerRow}.")
	}

	if(y > type.rows) {
		throw IllegalArgumentException("y must be between 1 and ${type.slotsPerRow}.")
	}

	return (x - 1) + ((y - 1) * type.slotsPerRow)
}
fun fromSlot(s: Int, type: GUIType) = Pair(s % type.slotsPerRow, s / type.slotsPerRow)

/**
 * Construct a [GUI.Slot] to be placed in a [GUI].
 * This is primarily a utility method.
 *
 * @param builder [GUI.Slot] builder.
 * @return constructed [GUI.Slot].
 */
fun GUI.guiSlot(builder: GUI.Slot.() -> Unit): GUI.Slot {
	return Slot().apply(builder)
}

class GUI(
	plugin: JavaPlugin,
	val title: Component,
	val type: GUIType,
	private val render: GUI.() -> Unit
) {
	/**
	 * Allow for [ItemStack] to be placed in the [GUI].
	 */
	var allowItemPlacement: Boolean = false

	/**
	 * Allow for [ItemStack] to be dragged within the [GUI].
	 */
	var allowItemDrag: Boolean = false

	/**
	 * Allow for hotkeys to be used to place & pickup items
	 * within the [GUI].
	 */
	var allowHotBarSwap: Boolean = false

	/**
	 * Allow for [ItemStack] not registered in slots to
	 * be taken from the [GUI].
	 */
	var allowItemPickup: Boolean = false

	/**
	 * Whether the [GUI] is a single instance that will be shared
	 * between different accessors.
	 *
	 * Useful to enable if a single instance of the GUI is shared.
	 */
	var singleInstance: Boolean = false

	/**
	 * Event called when an [ItemStack] is placed into a [GUI].
	 * Requires [allowItemPlacement] to be true to work.
	 */
	var onPlaceItem: GUIItemPlaceEvent? = null

	/**
	 * Event called when a [ItemStack] not registered to a slot is
	 * taken from a [GUI].
	 * Requires [allowItemPickup] to be true to work.
	 */
	var onPickupItem: GUIItemPickupEvent? = null

	/**
	 * Event called when an item is dragged across a [GUI].
	 * Requires [allowItemPlacement] to be true to work.
	 */
	var onDragItem: GUIDragItemEvent? = null

	/**
	 * Event called when a [Player] exits a [GUI].
	 */
	var onCloseInventory: GUICloseEvent? = null

	/**
	 * Bukkit [Inventory] of the [GUI].
	 *
	 * Do not open [GUI] from this reference as it may
	 * lead to undefined behavior.
	 */
	val bukkitInventory: Inventory = if(type is GUIType.Chest) {
		plugin.server.createInventory(null, type.slotsPerRow * type.rows, title)
	} else {
		plugin.server.createInventory(null, type.inventoryType, title)
	}

	internal var slots = arrayOfNulls<Slot>(type.slotsPerRow * type.rows)

	/**
	 * Chars mapped to Slot Builders for GUI templating.
	 */
	internal var templateSlots = mutableMapOf<Char, Slot>()

	private val guiListener = GUIListener(this)

	/**
	 * Define weather the [GUI] has been unregistered.
	 * If it has then it will be unable to be opened.
	 */
	internal var unregistered = false

	init {
		plugin.server.pluginManager
			.registerEvents(guiListener, plugin)
	}

	/**
	 * Structure of a slot item in a [GUI].
	 * Defines displayed [GUIItem] and click actions.
	 */
	inner class Slot {
		var item: GUIItem? = null
		var allowPickup: Boolean = false
		var onClick: GUISlotClickEvent? = null
	}

	/**
	 * Refresh the inventory and put the new items
	 * in the slots.
	 */
	fun refresh() {
		bukkitInventory.clear()
		slots = arrayOfNulls(type.slotsPerRow * type.rows)

		this.render()
	}

	/**
	 * Set the item in a specific GUI slot.
	 *
	 * @param i slot index
	 * @param slot slot
	 */
	fun slot(i: Int, slot: Slot) {
		if(i > bukkitInventory.size) {
			return
		}

		bukkitInventory.setItem(i, slot.item?.stack)

		slots[i] = slot
	}

	/**
	 * Set the item in a specific GUI slot.
	 *
	 * @param i slot index
	 * @param builder slot builder
	 */
	fun slot(i: Int, builder: Slot.() -> Unit) {
		slot(i, Slot().apply(builder))
	}

	/**
	 * Set the item in a specific GUI slot.
	 *
	 * @param x x-coordinate of the slot
	 * @param y y-coordinate of the slot
	 * @param slot slot
	 */
	fun slot(x: Int, y: Int, slot: Slot) {
		slot(toSlot(x, y, type), slot)
	}

	/**
	 * Set the item in a specific GUI slot.
	 *
	 * @param x x-coordinate of the slot
	 * @param y y-coordinate of the slot
	 * @param builder slot builder
	 */
	fun slot(x: Int, y: Int, builder: Slot.() -> Unit) {
		slot(toSlot(x, y, type), builder)
	}

	/**
	 * Fill the designated area of a GUI.
	 * Will fill item in a rectangular shape based on points provided.
	 *
	 * @param x1 first x-coordinate
	 * @param y1 first y-coordinate
	 * @param x2 second x-coordinate
	 * @param y2 second y-coordinate
	 * @param builder slot builder
	 */
	fun fill(
		x1: Int, y1: Int, x2: Int, y2: Int,
		builder: Slot.() -> Unit
	) {
		val dx = if (x1 < x2) x1..x2 else x2..x1
		val dy = if (y1 < y2) y1..y2 else y2..y1

		for (x in dx) for (y in dy) slot(x, y, builder)
	}

	/**
	 * Completely fill the outer border of a GUI.
	 *
	 * @param builder slot builder
	 */
	fun fillBorder(builder: Slot.() -> Unit) {
		all(builder)

		val x1 = 2
		// Just makes it work with 1 row chest guis.
		val y1 = if(type is GUIType.Chest && type.rows == 1 || type is GUIType.Hopper) 1 else 2

		val x2 = type.slotsPerRow - 1
		// Doesn't really matter if we hard code these values,
		// what're they gonna do? Change? well besides chest guis.
		val y2 = when(type) {
			is GUIType.Chest -> if(type.rows > 2) type.rows - 1 else 2
			is GUIType.Hopper -> 1
			is GUIType.Dispenser -> 2
		}

		fill(x1, y1, x2, y2) {
			item = null
		}
	}

	/**
	 * Fill all items of a GUI.
	 *
	 * @param builder slot builder.
	 */
	fun all(builder: Slot.() -> Unit) {
		fill(1, 1, type.slotsPerRow, type.rows, builder)
	}
	/**
	 * Set the item of the next available slot not occupied by any item.
	 * Any null item slot will be overridden as this method only checks for
	 * slots occupied by an ItemStack.
	 *
	 * @param builder slot builder
	 */
	fun nextAvailableSlot(builder: Slot.() -> Unit) {
		val firstEmptySlot = bukkitInventory.firstEmpty()
		if(firstEmptySlot == -1) {
			return
		}

		slot(firstEmptySlot, builder)
	}

	/**
	 * Check whether a [Inventory] is a
	 * GUI [Inventory].
	 *
	 * @return whether the other [Inventory] is a [GUI] inventory.
	 */
	fun isBukkitInventory(other: Inventory?): Boolean {
		if(other == null) {
			return false
		}

		return bukkitInventory == other
	}

	/**
	 * Mark a [GUI] as unregistered and remove its [Listener].
	 */
	fun unregister() {
		HandlerList.unregisterAll(guiListener)
		unregistered = true
	}
}