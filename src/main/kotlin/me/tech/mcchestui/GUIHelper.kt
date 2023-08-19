package me.tech.mcchestui

import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity
import org.bukkit.plugin.java.JavaPlugin

/**
 * Create a GUI.
 *
 * @param plugin
 * @param title Title of the GUI
 * @param type Type of GUI to generate
 * @param rows Amount of rows in the Chest GUI
 * @return [GUI] Object
 */
fun gui(
	plugin: JavaPlugin,
	title: Component,
	type: GUIType,
	render: GUI.() -> Unit
): GUI {
	return GUI(plugin, title, type, render).apply(render)
}

/**
 * Open a [GUI] for a [HumanEntity].
 *
 * @param gui [GUI] to open.
 */
fun HumanEntity.openGUI(gui: GUI) {
	if(gui.unregistered) {
		throw IllegalStateException("unable to open unregistered ui.")
	}

	openInventory(gui.bukkitInventory)
}

/**
 * Prop for a [GUI] that is placed into a
 * [GUI.Slot] that usually has data passed into it
 * through a function.
 */
typealias GUIProp = GUI.Slot.() -> Unit
