package me.tech.mcchestui.minestom.listener

import me.tech.mcchestui.minestom.MinestomGUI
import net.minestom.server.event.EventListener
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.inventory.Inventory

internal abstract class GUIListener(protected val gui: MinestomGUI) {
    protected fun isSame(inventory: Inventory?): Boolean {
        return inventory === gui.inventory.minestomInventory
    }

    abstract fun listener(): EventListener<out InventoryEvent>
}