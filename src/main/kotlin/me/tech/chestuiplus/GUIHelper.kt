package me.tech.chestuiplus

import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Create a GUI.
 * Automatically sets the rows to 1.
 * @param plugin
 * @param title Title of the GUI
 * @param type Type of GUI to generate
 * @return GUI Object
 */
fun gui(
	plugin: JavaPlugin,
	title: TextComponent,
	type: GUIType,
	render: GUI.() -> Unit
): GUI {
	return gui(plugin, title, type, 1, render)
}

/**
 * Create a GUI.
 * @param plugin
 * @param title Title of the GUI
 * @param type Type of GUI to generate
 * @param rows Amount of rows in the Chest GUI
 * @return GUI Object
 */
fun gui(
	plugin: JavaPlugin,
	title: TextComponent,
	type: GUIType,
	rows: Int,
	render: GUI.() -> Unit
): GUI {
	return GUI(plugin, title, type, rows, render).apply(render)
}

fun Player.openGUI(gui: GUI) = openInventory(gui.inventory)