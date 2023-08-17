package me.tech.mcchestui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

fun toSlot(x: Int, y: Int, type: GUIType) = (x - 1) + ((y - 1) * type.slotsPerRow)
fun fromSlot(s: Int, type: GUIType) = Pair(s % type.slotsPerRow, s / type.slotsPerRow)

class GUI(
	private val plugin: JavaPlugin,
	val title: Component,
	val type: GUIType,
	private val render: GUI.() -> Unit
) {
	/**
	 * Allow for [ItemStack] to be placed in the [GUI].
	 */
	var allowItemPlacement: Boolean = false

	var allowItemDrag: Boolean = false

	var allowHotBarSwap: Boolean = false

	/**
	 * Allow for [ItemStack] not registered in slots to
	 * be taken from the [GUI].]
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
	val bukkitInventory = if(type is GUIType.Chest) {
		plugin.server.createInventory(null, type.slotsPerRow * type.rows, title)
	} else {
		plugin.server.createInventory(null, type.inventoryType, title)
	}

	internal var slots = arrayOfNulls<Slot>(type.slotsPerRow * type.rows)

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
	 * @param builder slot builder
	 */
	fun slot(i: Int, builder: Slot.() -> Unit) {
		if(i > bukkitInventory.size) {
			return
		}

		val slot = Slot().apply(builder)
		bukkitInventory.setItem(i, slot.item?.stack)

		slots[i] = slot
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
	 *
	 */
	fun isBukkitInventory(other: Inventory?): Boolean {
		if(other == null) {
			return false
		}

		return bukkitInventory == other
	}

	/**
	 *
	 */
	fun unregister() {
		HandlerList.unregisterAll(guiListener)
		unregistered = true
	}
}

/**
 * Event when an [ItemStack] interaction is preformed with a [GUI].
 */
internal typealias GUIItemPlaceEvent = InventoryInteractEvent.(player: Player, item: ItemStack, slot: Int) -> Boolean

internal typealias GUIItemPickupEvent = InventoryInteractEvent.(player: Player, item: ItemStack?, slot: Int) -> Boolean

/**
 * Event when an [ItemStack] is dragged across a [GUI].
 */
internal typealias GUIDragItemEvent = InventoryDragEvent.(player: Player, items: Map<Int, ItemStack>) -> Boolean

/**
 * Event when a [GUI.Slot] is clicked.
 */
internal typealias GUISlotClickEvent = InventoryInteractEvent.(player: Player) -> Unit

/**
 * Event when a [GUI] is closed by a [Player].
 */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: HumanEntity) -> Unit