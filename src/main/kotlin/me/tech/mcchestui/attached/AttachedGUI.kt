package me.tech.mcchestui.attached

import me.tech.mcchestui.GUI
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

/**
 * Attach a [PlayerInventory] to a [GUI] to be used as a
 * secondary menu alongside the parent [GUI].
 *
 * @param player [PlayerInventory] to be used.
 */
fun GUI.attach(player: Player) {
    // cache inventory.
    attachedInventoryCache.saveInventory(player)
    player.inventory.storageContents = emptyArray()

    attach(player.inventory)
}

/**
 * Attach a [PlayerInventory] to a [GUI] to be used as a
 * secondary menu alongside the parent [GUI].
 *
 * @param playerInventory inventory to be used.
 */
private fun GUI.attach(playerInventory: PlayerInventory) {
    attachedGui = GUI(plugin, playerInventory)
}