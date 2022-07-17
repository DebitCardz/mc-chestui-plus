/**
 * @author hazae41
 * This GUI library is used from https://github.com/hazae41/mc-chestui
 * and has been slightly recoded to better suite what I needed from it.
 * Thanks for originally creating it!
 */
package me.tech.chestuiplus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

fun toSlot(x: Int, y: Int, type: GUIType) = x + (y * type.slotsPerRow)
fun fromSlot(s: Int, type: GUIType) = Pair(s % type.slotsPerRow, s / type.slotsPerRow)

class GUI(
	plugin: JavaPlugin,
	val title: Component,
	val type: GUIType,
	val rows: Int,
	private val render: GUI.() -> Unit
): Listener {
	var allowShiftClicking = false

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
		var onClick: InventoryClickEvent.(Player) -> Unit = { }
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

	fun slot(i: Int, builder: Slot.() -> Unit) {
		if(i > inventory.size) {
			return
		}

		val slot = Slot().apply(builder)
		inventory.setItem(i, slot.item?.stack)

		slots[i] = slot
	}

	fun slot(x: Int, y: Int, builder: Slot.() -> Unit) {
		slot(toSlot(x, y, type), builder)
	}

	fun fill(
		x1: Int, y1: Int, x2: Int, y2: Int,
		builder: Slot.() -> Unit
	) {
		val dx = if (x1 < x2) x1..x2 else x2..x1
		val dy = if (y1 < y2) y1..y2 else y2..y1

		for (x in dx) for (y in dy) slot(x, y, builder)
	}

	fun fillBorder(builder: Slot.() -> Unit) {
		all(builder)

		val x1 = 1
		// Just makes it work with 1 row chest guis.
		val y1 = if(type == GUIType.CHEST && rows == 1) 0 else 1

		val x2 = type.slotsPerRow - 2
		// Doesn't really matter if we hard code these values,
		// what're they gonna do? Change? well besides chest guis.
		val y2 = when(type) {
			GUIType.CHEST -> if(rows > 2) inventoryRows - 2 else 1
			GUIType.HOPPER -> 0
			GUIType.DISPENSER -> 1
		}

		fill(x1, y1, x2, y2) {
			item = null
		}
	}

	fun all(builder: Slot.() -> Unit) = fill(0, 0, type.slotsPerRow - 1, inventoryRows - 1, builder)

	@EventHandler(priority = EventPriority.HIGHEST)
	internal fun onInventoryClick(ev: InventoryClickEvent) {
		if(ev.inventory != inventory) {
			return
		}

		// Prevent shift clicking items into empty space in the inventory.
		if(
			!allowShiftClicking
			&& ev.isShiftClick
			&& ev.clickedInventory != inventory
		) {
			ev.isCancelled = true
		}

		if(ev.clickedInventory != inventory) {
			return
		}

		val player = ev.whoClicked as Player
		val slot = slots.getOrNull(ev.slot)

		// Just make sure nothing weird happens in a null slot.
		if(slot == null) {
			ev.isCancelled = true
			return
		}

		ev.isCancelled = slot.cancelled

		slot.onClick(ev, player)
	}

	@EventHandler(priority = EventPriority.MONITOR)
	internal fun onInventoryClose(ev: InventoryCloseEvent) {
		if(
			ev.inventory != inventory
			|| ev.inventory.viewers.size > 1
		) {
			return
		}

		HandlerList.unregisterAll(this)
	}
}