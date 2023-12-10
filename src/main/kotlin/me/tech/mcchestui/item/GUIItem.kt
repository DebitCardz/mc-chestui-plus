package me.tech.mcchestui.item

import me.tech.mcchestui.GUI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Construct a [ItemStack] to be placed in a [GUI.Slot].
 * @param type material type.
 * @param builder [GUIItem] builder.
 */
fun GUI.Slot.item(
	type: Material = Material.AIR,
	builder: GUIItem.() -> Unit = {}
): GUIItem {
	return guiItem(type, builder)
}

/**
 * Construct a [ItemStack] to be placed in a [GUI.Slot].
 * @param type material type.
 * @param builder [GUIItem] builder.
 */
fun guiItem(
	type: Material = Material.AIR,
	builder: GUIItem.() -> Unit = {}
): GUIItem {
	return GUIItem(type).apply(builder)
}

open class GUIItem(
	type: Material
) {
	/**
	 * [ItemStack] of the [GUIItem].
	 */
	var stack = ItemStack(type, 1)
		private set

	/**
	 * [ItemMeta] of the [GUIItem].
	 */
	open val itemMeta: ItemMeta
		get() = stack.itemMeta

	/**
	 * Whether to remove default italics from the [ItemMeta].
	 */
	var removeParentItalics = true

	/**
	 * Modify the [ItemStack] of the [GUIItem].
	 * @param builder [ItemStack] builder.
	 */
	fun stack(builder: ItemStack.() -> Unit) {
		stack.apply(builder)
	}

	/**
	 * Modify the [Material] of the [GUIItem].
	 * @param material [Material] to set to.
	 */
	fun material(material: Material) {
		stack.type = material
	}

	/**
	 * Current display name of the [ItemStack].
	 */
	var name: Component?
		get() = itemMeta.displayName()
		set(value) {
			val name = if(removeParentItalics) {
				value?.let { removeComponentParentItalics(it) }
			} else {
				value
			}

			stack.editMeta {
				it.displayName(name)
			}
		}

	/**
	 * Current lore of the [ItemStack].
	 */
	var lore: Collection<Component>?
		get() = itemMeta.lore()
		set(value) {
			val lore = if(removeParentItalics) {
				value?.map { removeComponentParentItalics(it) }
			} else {
				value
			}

			stack.editMeta {
				it.lore(sanitizeLore(lore))
			}
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
	 * Whether the [ItemStack] is glowing.
	 */
	var glowing: Boolean = false
		set(value) {
			stack.editMeta {
				if(value) {
					it.addEnchant(Enchantment.ARROW_INFINITE, 0, true)
					it.addItemFlags(ItemFlag.HIDE_ENCHANTS)
				} else {
					it.removeEnchant(Enchantment.ARROW_INFINITE)
					it.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
				}
			}

			field = value
		}

	/**
	 * Custom model data id of the [ItemStack].
	 */
	var customModelData: Int
		get() = itemMeta.customModelData
		set(value) {
			stack.editMeta {
				it.setCustomModelData(value)
			}
		}

	/**
	 * Remove the italics of a [Component] if it is not already
	 * set on the [Component].
	 * @param component to remove from.
	 * @return converted component.
	 */
	// we have to basically remake decorationIfAbsent because of issues with
	// backwards compatibility.
	private fun removeComponentParentItalics(component: Component): Component {
		if(component.decoration(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
			return component.decoration(TextDecoration.ITALIC, false)
		}

		return component
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