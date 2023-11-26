package me.tech.mcchestui.item

import com.destroystokyo.paper.profile.PlayerProfile
import me.tech.mcchestui.GUI
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
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
     * Current [OfflinePlayer] that owns the player head.
     *
     * @warning Will only apply on items that inherit [SkullMeta].
     */
    var skullOwner: OfflinePlayer?
        get() = itemMeta.owningPlayer
        set(value) {
            stack.editMeta(SkullMeta::class.java) {
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
            stack.editMeta(SkullMeta::class.java) {
                it.playerProfile = value
            }
        }
}