/**
 * @author hazae41
 * This GUI library is used from https://github.com/hazae41/mc-chestui
 * and has been slightly recoded to better suite what I needed from it.
 * Thanks for originally creating it!
 */
package me.tech.mcchestui

import org.bukkit.event.inventory.InventoryType

enum class GUIType(
	val inventoryType: InventoryType,
	val slotsPerRow: Int,
	val rows: Int?
) {
	CHEST(InventoryType.CHEST, 9, null),
	DISPENSER(InventoryType.DISPENSER, 3, 3),
	HOPPER(InventoryType.HOPPER, 5, 1);
}