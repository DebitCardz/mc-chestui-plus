package me.tech.mcchestui.item

import com.destroystokyo.paper.profile.PlayerProfile
import me.tech.mcchestui.GUI
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

/**
 * Construct a [ItemStack] to be placed in a [GUI.Slot].
 * Automatically uses [Material.PLAYER_HEAD].
 * @param builder [GUIHeadItem] builder.
 */
fun GUI.Slot.headItem(
    builder: GUIHeadItem.() -> Unit = {}
): GUIHeadItem {
    return guiHeadItem(builder)
}

/**
 * Construct a [ItemStack] to be placed in a [GUI.Slot].
 * Automatically uses [Material.PLAYER_HEAD].
 * @param builder [GUIHeadItem] builder.
 */
fun guiHeadItem(
    builder: GUIHeadItem.() -> Unit = {}
): GUIHeadItem {
    return GUIHeadItem().apply(builder)
}

class GUIHeadItem : GUIItem(Material.PLAYER_HEAD) {
    override val itemMeta: SkullMeta
        get() = stack.itemMeta as SkullMeta

    /**
     * Modify the [SkullMeta] of the [GUIItem].
     * @param builder [SkullMeta] builder.
     */
    fun meta(builder: SkullMeta.() -> Unit) {
        itemStack.editMeta(SkullMeta::class.java, builder)
    }

    /**
     * Current [OfflinePlayer] that owns the player head.
     *
     * @warning Will only apply on items that inherit [SkullMeta].
     */
    var skullOwner: OfflinePlayer?
        get() = itemMeta.owningPlayer
        set(value) {
            itemStack.editMeta(SkullMeta::class.java) {
                it.owningPlayer = value
            }
        }

    /**
     * Current [PlayerProfile] that owns the player head.
     *
     * @warning Will only apply on items that inherit [SkullMeta].
     */
    var playerProfile: PlayerProfile?
        get() = itemMeta.playerProfile
        set(value) {
            itemStack.editMeta(SkullMeta::class.java) {
                it.playerProfile = value
            }
        }
}