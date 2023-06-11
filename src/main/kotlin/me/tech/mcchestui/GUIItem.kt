/**
 * @author hazae41
 * This GUI library is used from https://github.com/hazae41/mc-chestui
 * and has been slightly recoded to better suite what I needed from it.
 * Thanks for originally creating it!
 */
package me.tech.mcchestui

import com.destroystokyo.paper.profile.PlayerProfile
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

fun item(type: Material = Material.AIR, builder: GUIItem.() -> Unit = {}) =
	GUIItem(type).apply(builder)

class GUIItem(
	type: Material
) {
	/**
	 * [ItemStack] of the [GUIItem].
	 */
	val stack = ItemStack(type, 1)

	/**
	 * [ItemMeta] of the [GUIItem].
	 */
	val itemMeta
		get() = stack.itemMeta

	/**
	 * Whether to remove default italics from the [ItemMeta].
	 */
	var removeDefaultItalics = false

	/**
	 * Modify the [ItemStack] of the [GUIItem].
	 * @param builder [ItemStack] builder.
	 */
	fun stack(builder: ItemStack.() -> Unit) =
		stack.apply(builder)

	/**
	 * Modify the [ItemMeta] of the [GUIItem].
	 * @param builder [ItemMeta] builder.
	 */
	fun meta(builder: ItemMeta.() -> Unit) =
		itemMeta?.apply(builder)?.also { stack.itemMeta = it }

	/**
	 * Current display name of the [ItemStack].
	 */
	var name: Component?
		get() = itemMeta.displayName()
		set(value) {
			val name = if(removeDefaultItalics) {
				value?.decoration(TextDecoration.ITALIC, false)
			} else {
				value
			}

			itemMeta.apply {
				displayName(name)
			}.also { stack.itemMeta = it }
		}

	/**
	 * Current lore of the [ItemStack].
	 */
	var lore: Collection<Component>?
		get() = itemMeta.lore()
		set(value) {
			val lore = if(removeDefaultItalics) {
				value?.map { it.decoration(TextDecoration.ITALIC, false) }
			} else {
				value
			}

			itemMeta.apply {
				lore(sanitizeLore(lore))
			}.also { stack.itemMeta = it }
		}

	/**
	 * Current [ItemStack] size.
	 */
	var amount: Int
		get() = stack.amount
		set(value) {
			stack.amount = value
		}

	/**
	 * Current [OfflinePlayer] that owns the player head.
	 *
	 * @warning Will only apply on items that inherit [SkullMeta].
	 */
	var skullOwner: OfflinePlayer?
		get() = (itemMeta as? SkullMeta)?.owningPlayer
		set(value) {
			(itemMeta as? SkullMeta)
				?.apply { owningPlayer = value }
				?.also { stack.itemMeta = it }
		}

	/**
	 * Current [PlayerProfile] that owns the player head.
	 *
	 * @warning Will only apply on items that inherit [SkullMeta].
	 */
	var playerProfile: PlayerProfile?
		get() = (itemMeta as? SkullMeta)?.playerProfile
		set(value) {
			(itemMeta as? SkullMeta)
				?.apply { playerProfile = value }
				?.also { stack.itemMeta = it }
		}

	/**
	 * Whether the [ItemStack] is glowing.
	 */
	var glowing: Boolean = false
		set(value) {
			// Add glow.
			if(value) {
				itemMeta.apply {
					addEnchant(Enchantment.ARROW_INFINITE, 0, true)
					addItemFlags(ItemFlag.HIDE_ENCHANTS)
				}.also { stack.itemMeta = it }
				// Remove glow.
			} else {
				itemMeta.apply {
					removeEnchant(Enchantment.ARROW_INFINITE)
					removeItemFlags(ItemFlag.HIDE_ENCHANTS)
				}.also { stack.itemMeta = it }
			}

			field = value
		}

	/**
	 * Custom model data id of the [ItemStack].
	 */
	var customModelData: Int
		get() = itemMeta.customModelData
		set(value) {
			itemMeta.apply {
				setCustomModelData(value)
			}.also { stack.itemMeta = it }
		}

	/**
	 * Sanitize the input lore to remove trailing new line breaks.
	 * @param lore to be sanitized.
	 * @return sanitized lore.
	 */
	private fun sanitizeLore(lore: Collection<Component>?) = lore?.map {
		it.replaceText { builder ->
			builder.matchLiteral("\n")
			builder.replacement { replace ->
				replace.content("")
			}
		}
	}
}