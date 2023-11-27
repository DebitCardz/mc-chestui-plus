package me.tech.mcchestui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

interface AttachedInventoryCache {
    fun saveInventory(player: Player): Boolean

    fun loadInventory(player: Player): Boolean
}

internal object MemoryAttachedInventoryCache : AttachedInventoryCache {
    private val cachedInventory = mutableMapOf<UUID, Array<ItemStack?>>()

    override fun saveInventory(player: Player): Boolean {
        cachedInventory[player.uniqueId] = player.inventory.storageContents

        return true
    }

    override fun loadInventory(player: Player): Boolean {
        val inventory = cachedInventory[player.uniqueId]
            ?: return false

        player.inventory.storageContents = inventory
        cachedInventory.remove(player.uniqueId)

        return true
    }
}

internal fun restoreAttachedInventory(player: Player, gui: GUI) {
    player.inventory.storageContents = emptyArray()
    gui.attachedInventoryCache.loadInventory(player)
}

fun GUI.attach(player: Player) {
    attachedInventoryCache.saveInventory(player)
    player.inventory.storageContents = emptyArray()

    attach(player.inventory)
}

private fun GUI.attach(playerInventory: PlayerInventory) {
    val ui = GUI(plugin, playerInventory)
    attachedGui = ui
}

fun GUI.attachInventoryCache(cache: AttachedInventoryCache) {
    attachedInventoryCache = cache
}