/**
 * @author hazae41
 * This GUI library is used from https://github.com/hazae41/mc-chestui
 * and has been slightly recoded to better suite what I needed from it.
 * Thanks for originally creating it!
 */
package me.tech.mcchestui

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

fun toSlot(x: Int, y: Int, type: GUIType) = x + ((y - 1) * type.slotsPerRow) - 1
fun fromSlot(s: Int, type: GUIType) = Pair((s + 1) % type.slotsPerRow, (s + 1) / type.slotsPerRow)

class GUI(
	plugin: JavaPlugin,
	val title: Component,
	val type: GUIType,
	val rows: Int,
	private val render: GUI.() -> Unit
): Listener {
	@Deprecated(
		message =  "Use allowPlaceItem instead.",
		replaceWith = ReplaceWith("allowItemPlacement")
	)
	var allowShiftClicking: Boolean = false

	/**
	 * Allow for [ItemStack] to be placed in the [GUI].
	 */
	var allowItemPlacement: Boolean = false

	/**
	 * Automatically unregister the [Listener] attached to the [GUI]
	 * when all [GUI] viewers exit the menu.
	 *
	 * Useful to disable if a single instance of the GUI is shared.
	 */
	var automaticallyUnregisterListener: Boolean = true

	/**
	 * Event called when an [ItemStack] is placed into a [GUI].
	 * Requires [allowItemPlacement] to be true to work.
	 */
	var onPlaceItem: GUIItemEvent? = null

	/**
	 * Event called when an item is dragged across a [GUI].
	 * Requires [allowItemPlacement] to be true to work.
	 */
	var onDragItem: GUIDragItemEvent? = null

	/**
	 * Event called when a [Player] exits a [GUI].
	 */
	var onCloseInventory: GUICloseEvent? = null

	var slots = arrayOfNulls<Slot>(type.slotsPerRow * rows)

	val inventory =
		// Chest GUI.
		if(type == GUIType.CHEST) {
			plugin.server.createInventory(null, type.slotsPerRow * rows, title)
		// Other GUi.
		} else {
			plugin.server.createInventory(null, type.inventoryType, title)
		}

	private val inventoryRows =
		if(type == GUIType.CHEST) {
			rows
		// We can assert this won't be null since the
		// only null value is already checked.
		} else {
			type.rows!!
		}

	init {
		plugin.server.pluginManager.registerEvents(this, plugin)
	}

	inner class Slot {
		var item: GUIItem? = null
		var cancelled: Boolean = true
		var onClick: GUISlotClickEvent? = null
	}

	/**
	 * Refresh the inventory and put the new items
	 * in the slots.
	 */
	fun refresh() {
		inventory.clear()
		slots = arrayOfNulls(type.slotsPerRow * rows)

		this.render()
	}

	/**
	 * Set the item in a specific GUI slot.
	 *
	 * @param i slot index
	 * @param builder slot builder
	 */
	fun slot(i: Int, builder: Slot.() -> Unit) {
		if(i > inventory.size) {
			return
		}

		val slot = Slot().apply(builder)
		inventory.setItem(i, slot.item?.stack)

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

		val x1 = 1
		// Just makes it work with 1 row chest guis.
		val y1 = if(type == GUIType.CHEST && rows == 1) 1 else 2

		val x2 = type.slotsPerRow - 7
		// Doesn't really matter if we hard code these values,
		// what're they gonna do? Change? well besides chest guis.
		val y2 = when(type) {
			GUIType.CHEST -> if(rows > 2) inventoryRows - 1 else 2
			GUIType.HOPPER -> 0
			GUIType.DISPENSER -> 2
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
	fun all(builder: Slot.() -> Unit) = fill(0, 0, type.slotsPerRow - 1, inventoryRows - 1, builder)

	/**
	 * Set the item of the next available slot not occupied by any item.
	 * Any null item slot will be overridden as this method only checks for
	 * slots occupied by an ItemStack.
	 *
	 * @param builder slot builder
	 */
	fun nextAvailableSlot(builder: Slot.() -> Unit) {
		val firstEmptySlot = inventory.firstEmpty()
		if(firstEmptySlot == -1) {
			return
		}

		slot(firstEmptySlot, builder)
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	internal fun onInventoryClick(ev: InventoryClickEvent) {
		if(ev.inventory != inventory) {
			return
		}

		// Prevent placing items into empty space in the inventory.
		if(!allowItemPlacement && ev.isShiftClick && ev.clickedInventory != inventory) {
			ev.isCancelled = true
		}

		if(ev.clickedInventory != inventory) {
			return
		}

		val player = ev.whoClicked as Player
		val slot = slots.getOrNull(ev.slot)

		// Just make sure nothing weird happens in a null slot.
		if(slot == null) {
			ev.isCancelled = !allowItemPlacement
			return
		}

		ev.isCancelled = slot.cancelled

		slot.onClick?.let { uiEvent ->
			uiEvent(ev, player)
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	internal fun onInventoryPlaceItem(ev: InventoryClickEvent) {
		if(ev.inventory != inventory) {
			return
		}

		if(!allowItemPlacement || ev.clickedInventory != inventory || ev.isCancelled) {
			return
		}

		if(slots.getOrNull(ev.slot) != null) {
			return
		}

		val placedItem = ev.cursor
			?: return

		if(placedItem.type == Material.AIR) {
			return
		}

		onPlaceItem?.let { uiEvent ->
			uiEvent(ev, ev.whoClicked as Player, placedItem, ev.slot)
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	internal fun onInventoryPlaceItem(ev: InventoryDragEvent) {
		if(ev.inventory != inventory) {
			return
		}

		if(!allowItemPlacement || ev.view.topInventory != inventory || ev.isCancelled) {
			return
		}

		val newItems = ev.newItems

		for((index, _) in newItems) {
			// don't override slots
			if(slots.getOrNull(index) != null) {
				ev.isCancelled = true
				return
			}
		}

		onDragItem?.let { uiEvent ->
			uiEvent(ev, ev.whoClicked as Player, newItems)
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	internal fun onInventoryClose(ev: InventoryCloseEvent) {
		onCloseInventory?.let { uiEvent ->
			uiEvent(ev, ev.player)
		}

		// don't unregister this.
		if(!automaticallyUnregisterListener) {
			return
		}

		if(ev.inventory != inventory || ev.inventory.viewers.size > 1) {
			return
		}

		HandlerList.unregisterAll(this)
	}
}

/**
 * Event when an [ItemStack] interaction is preformed with a [GUI].
 */
internal typealias GUIItemEvent = InventoryClickEvent.(player: Player, item: ItemStack, slot: Int) -> Unit

/**
 * Event when an [ItemStack] is dragged across a [GUI].
 */
internal typealias GUIDragItemEvent = InventoryDragEvent.(player: Player, items: Map<Int, ItemStack>) -> Unit

/**
 * Event when a [GUI.Slot] is clicked.
 */
internal typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit

/**
 * Event when a [GUI] is closed by a [Player].
 */
internal typealias GUICloseEvent = InventoryCloseEvent.(player: HumanEntity) -> Unit
