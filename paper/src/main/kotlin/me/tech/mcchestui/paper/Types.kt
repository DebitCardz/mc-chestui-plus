package me.tech.mcchestui.paper

import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.paper.item.PaperGUIItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

typealias AbstractPaperGUI = GUI<PaperGUIItem, GUISlotClickEvent>

typealias PaperGUIRender = GUIRender<PaperGUIItem, GUISlotClickEvent>

/** Dispatched when a [GUI.Slot] is clicked. */
typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit