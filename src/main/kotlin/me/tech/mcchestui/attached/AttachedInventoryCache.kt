package me.tech.mcchestui.attached

import me.tech.mcchestui.GUI
import org.bukkit.entity.Player

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

/**
 * Used to cache the items removed from a [Player] when
 * a [GUI.attachedGui] is being used.
 *
 * The items of the [Player] are restored when the GUICloseEvent is called
 * on the parent [GUI].
 * Contents of the [Player] inventory will then be restored.
 */
interface AttachedInventoryCache {
    /**
     * Save the storage contents of a [PlayerInventory] before the contents of
     * the [GUI.attachedGui] override the [PlayerInventory].
     *
     * @param player
     * @return whether the operation was a success.
     */
    fun saveInventory(player: Player): Boolean

    /**
     * Restore the storage contents of a [PlayerInventory] to their state
     * before being overridden by the [GUI.attachedGui].
     *
     * @param player
     * @return whether the operation was a success.
     */
    fun restoreInventory(player: Player): Boolean
}

/**
 * Memory attached gui cache.
 */
internal object MemoryAttachedInventoryCache : AttachedInventoryCache {
    private val cachedInventory = mutableMapOf<UUID, Array<ItemStack?>>()

    override fun saveInventory(player: Player): Boolean {
        cachedInventory[player.uniqueId] = player.inventory.storageContents

        return true
    }

    override fun restoreInventory(player: Player): Boolean {
        val inventory = cachedInventory[player.uniqueId]
            ?: return false

        player.inventory.storageContents = inventory
        cachedInventory.remove(player.uniqueId)

        return true
    }
}

/**
 * Attach a custom [AttachedInventoryCache] to a [GUI].
 * By default, the [MemoryAttachedInventoryCache] is used.
 */
fun GUI.attachInventoryCache(cache: AttachedInventoryCache) {
    attachedInventoryCache = cache
}

/**
 * Restore a players inventory from the [AttachedInventoryCache].
 *
 * @param player player to cache.
 * @param gui gui to cache for.
 */
internal fun restoreCachedInventory(player: Player, gui: GUI) {
    player.inventory.storageContents = emptyArray()
    gui.attachedInventoryCache.restoreInventory(player)
}