package me.tech.mcchestui.minestom

import me.tech.mcchestui.GUIType
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player

fun gui(
    title: Component,
    type: GUIType,
    render: MinestomGUI.() -> Unit
): MinestomGUI = MinestomGUI(
    title,
    type,
    type.toMinestomInventory(title),
    false,
    render
).apply(render)

/**
 * @return whether the gui was opened.
 */
fun Player.openGUI(gui: MinestomGUI): Boolean {
    require(!gui.unregistered) { "gui must not be unregistered" }

    return gui.inventory.minestomInventory
        .let { openInventory(it) }
}