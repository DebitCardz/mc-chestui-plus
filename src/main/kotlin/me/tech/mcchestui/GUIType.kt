package me.tech.mcchestui

import org.bukkit.event.inventory.InventoryType

sealed interface GUIType {
	 val slotsPerRow: Int
	 val rows: Int
	 val inventoryType: InventoryType

	 data class Chest(override val rows: Int) : GUIType {
		 override val slotsPerRow: Int
			 get() = 9

		 override val inventoryType: InventoryType
			 get() = InventoryType.CHEST

		 init {
			 if(rows < 1 || rows > 6) {
				 throw IllegalArgumentException(
					 "chest rows cannot be ${if(rows < 1) "below" else "above"} $rows."
				 )
			 }
		 }
	 }

	 data object Dispenser : GUIType {
		override val slotsPerRow: Int
			get() = 3

		override val rows: Int
			get() = 3

		override val inventoryType: InventoryType
			get() = InventoryType.DISPENSER
	}

	data object Hopper : GUIType {
		override val slotsPerRow: Int
			get() = 5

		override val rows: Int
			get() = 1

		override val inventoryType: InventoryType
			get() = InventoryType.HOPPER
	}
}