package me.tech.mcchestui.paper

import me.tech.mcchestui.GUIInventory
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.paper.item.PaperGUIItem
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

class PaperInventory(
    val bukkitInventory: Inventory
) : GUIInventory<PaperGUIItem> {
    override val size: Int
        get() = bukkitInventory.size

    override fun setItem(index: Int, item: PaperGUIItem?) {
        bukkitInventory.setItem(index, item)
    }

    override fun clear() {
        bukkitInventory.clear()
    }

    override fun firstEmpty(): Int {
        return bukkitInventory.firstEmpty()
    }
}

fun GUIType.toPaperInventory(title: Component): PaperInventory {
    val bukkitInventory = when(this) {
        is GUIType.Chest -> Bukkit.createInventory(null, slotsPerRow * rows, title)
        is GUIType.Hopper -> Bukkit.createInventory(null, InventoryType.HOPPER, title)
        is GUIType.Dispenser -> Bukkit.createInventory(null, InventoryType.DISPENSER, title)
        else -> error("invalid gui type specified $this.")
    }
    return PaperInventory(bukkitInventory)
}

/** [GUIInventory] for Paper impl. */
typealias PaperGUIInventory = GUIInventory<PaperGUIItem>