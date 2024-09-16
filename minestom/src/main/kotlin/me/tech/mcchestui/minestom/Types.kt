package me.tech.mcchestui.minestom

import me.tech.mcchestui.GUI
import me.tech.mcchestui.minestom.item.MinestomGUIItem
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent

typealias AbstractMinestomGUI = GUI<MinestomGUIItem, *>

// concrete gui type.
/** Render function for [MinestomGUI]. */
typealias MinestomGUIRender = MinestomGUI.() -> Unit

typealias GUICloseEvent = InventoryCloseEvent.(player: Player) -> Unit

typealias GUISlotPreClickEvent = InventoryPreClickEvent.(player: Player) -> Unit

typealias GUISlotClickEvent = InventoryClickEvent.(player: Player) -> Unit