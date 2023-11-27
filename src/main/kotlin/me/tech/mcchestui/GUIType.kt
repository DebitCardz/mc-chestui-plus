package me.tech.mcchestui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

sealed class GUIType(
	val slotsPerRow: Int,
	open val rows: Int,
	val inventoryType: InventoryType
) {
	 internal open fun createBukkitInventory(
		 title: Component
	 ): Inventory {
		 return Bukkit.createInventory(null, inventoryType, title)
	 }

	 class Chest(rows: Int) : GUIType(
		 slotsPerRow = 9,
		 rows,
		 inventoryType = InventoryType.CHEST
	 ) {
		 init {
			 if(rows < 1 || rows > 6) {
				 throw IllegalArgumentException(
					 "chest rows cannot be ${if(rows < 1) "below" else "above"} $rows."
				 )
			 }
		 }

		 override fun createBukkitInventory(title: Component): Inventory {
			 return Bukkit.createInventory(null, slotsPerRow * rows, title)
		 }
	 }

	 data object Dispenser : GUIType(
		 slotsPerRow = 3,
		 rows = 3,
		 inventoryType = InventoryType.HOPPER
	 )

	data object Hopper : GUIType(
		slotsPerRow = 5,
		rows = 1,
		inventoryType = InventoryType.HOPPER
	)
}