package me.tech.mcchestui.paper

import me.tech.mcchestui.GUIType
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

fun gui(
    title: Component,
    type: GUIType,
    render: PaperGUI.() -> Unit
): PaperGUI = PaperGUI(
    title,
    type,
    type.toPaperInventory(title),
    false,
    render
).apply(render)

fun HumanEntity.openGUI(gui: PaperGUI) {
    require(!gui.unregistered) { "gui must not be unregistered" }

    gui.inventory.bukkitInventory
        .let { openInventory(it) }
}

internal fun Inventory.clone(
    title: Component,
    type: GUIType
): Inventory {
    val clone = type.toBukkitInventory(title)
    clone.contents = this.contents

    return clone
}