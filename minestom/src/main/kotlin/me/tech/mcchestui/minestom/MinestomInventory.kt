package me.tech.mcchestui.minestom

import me.tech.mcchestui.GUIInventory
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.minestom.item.MinestomGUIItem
import net.kyori.adventure.text.Component
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack

class MinestomInventory(
    val minestomInventory: Inventory
) : GUIInventory<MinestomGUIItem> {
    override val size: Int
        get() = minestomInventory.size

    override fun setItem(index: Int, item: MinestomGUIItem?) {
        minestomInventory.setItemStack(index, item?.stack ?: ItemStack.AIR)
    }

    override fun clear() {
        minestomInventory.clear()
    }

    override fun firstEmpty(): Int {
        return minestomInventory.itemStacks
            .indexOfFirst { it.isAir }
    }

    override fun equals(other: Any?): Boolean {
        if(other is Inventory) {
            return other === minestomInventory
        }

        return super.equals(other)
    }
}

fun GUIType.toMinestomInventory(title: Component): MinestomInventory {
    val minestomInventory = when(this) {
        is GUIType.Chest -> {
            val chestRows = when(rows) {
                1 -> InventoryType.CHEST_1_ROW
                2 -> InventoryType.CHEST_2_ROW
                3 -> InventoryType.CHEST_3_ROW
                4 -> InventoryType.CHEST_4_ROW
                5 -> InventoryType.CHEST_5_ROW
                6 -> InventoryType.CHEST_6_ROW
                else -> error("invalid rows $rows provided.")
            }
            Inventory(chestRows, title)
        }
        is GUIType.Hopper -> Inventory(InventoryType.HOPPER, title)
        is GUIType.Dispenser -> Inventory(InventoryType.WINDOW_3X3, title)
        else -> error("invalid gui type specified $this.")
    }

    return MinestomInventory(minestomInventory)
}