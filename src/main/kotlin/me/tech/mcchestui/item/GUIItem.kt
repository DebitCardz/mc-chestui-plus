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
 * @param stack item stack.
 * @param builder [GUIItem] builder.
 */
fun GUI.Slot.item(
	stack: ItemStack,
	builder: GUIItem.() -> Unit = {}
): GUIItem {
	return guiItem(stack, builder)
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

/**
 * Construct a [ItemStack] to be placed in a [GUI.Slot].
 * @param stack item stack.
 * @param builder [GUIItem] builder.
 */
fun guiItem(
	stack: ItemStack,
	builder: GUIItem.() -> Unit = {}
): GUIItem {
	return GUIItem(stack).apply(builder)
}


open class GUIItem constructor(
	protected val itemStack: ItemStack
) {
	constructor(type: Material)
			: this(ItemStack(type, 1))

	/**
	 * [ItemStack] of the [GUIItem].
	 */
	val stack: ItemStack
		get() = itemStack.clone()

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
	 * Modify the [Material] of the [GUIItem].
	 * @param material [Material] to set to.
	 */
	fun material(material: Material) {
		itemStack.type = material
	}

	/**
	 * Modify the [ItemMeta] of the [GUIItem].
	 * @param builder [ItemMeta] builder.
	 */
	fun meta(builder: ItemMeta.() -> Unit) {
		itemStack.editMeta(builder)
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

			itemStack.editMeta {
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

			itemStack.editMeta {
				it.lore(sanitizeLore(lore))
			}
		}

	/**
	 * Current [ItemStack] size.
	 */
	var amount: Int
		get() = itemStack.amount
		set(value) {
			itemStack.amount = value
		}

	/**
	 * Whether the [ItemStack] is glowing.
	 */
	var glowing: Boolean = false
		set(value) {
			itemStack.editMeta {
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
			itemStack.editMeta {
				it.setCustomModelData(value)
			}
		}

	/**
	 * Add [ItemFlag] to the [ItemStack].
	 * @param flags [ItemFlag] to add.
	 */
	fun addFlags(vararg flags: ItemFlag) {
		itemStack.editMeta {
			it.addItemFlags(*flags)
		}
	}

	/**
	 * Remove [ItemFlag] from the [ItemStack].
	 * @param flags [ItemFlag] to remove.
	 */
	fun removeFlags(vararg flags: ItemFlag) {
		itemStack.editMeta {
			it.removeItemFlags(*flags)
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