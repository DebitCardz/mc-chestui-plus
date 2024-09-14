package me.tech.mcchestui.paper

import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.paper.item.PaperGUIItem
import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity

fun gui(
    title: Component,
    type: GUIType,
    render: PaperGUIRender
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