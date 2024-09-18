package me.tech.mcchestui.paper.item

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.item.GUIItem
import me.tech.mcchestui.paper.GUISlotClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun AbstractGUI<PaperGUIItem, GUISlotClickEvent>.Slot.item(
    type: Material,
    builder: PaperGUIItem.() -> Unit = { }
): PaperGUIItem {
    return PaperGUIItem(type)
        .apply(builder)
}

class PaperGUIItem(
    stack: ItemStack
) : ItemStack(stack), GUIItem {
    constructor(type: Material)
            : this(ItemStack(type, 1))

    /** Whether to remove default italics from the [ItemMeta]. */
    override var removeParentItalics = true

    override var name: Component?
        get() = displayName()
        set(value) {
            val name = value?.let {
                if(removeParentItalics) removeComponentParentItalics(value)
                else it
            }
            editMeta { it.displayName(name) }
        }

    override var lore: Collection<Component>?
        get() = itemMeta.lore()
        set(value) {
            val lore = if(removeParentItalics) {
                value?.map { removeComponentParentItalics(it) }
            } else {
                value
            }

            editMeta {
                it.lore(sanitizeLore(lore))
            }
        }

    var material: Material
        get() = type
        set(value) { type = value }

    /**
     * Whether the [ItemStack] is glowing.
     */
    override var glowing: Boolean = false
        set(value) {
            editMeta {
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
    override var customModelData: Int?
        get() = itemMeta.customModelData
        set(value) {
            editMeta {
                it.setCustomModelData(value)
            }
        }

    fun displayName(name: Component?) {
        this.name = name
    }

    /**
     * Add [ItemFlag] to the [ItemStack].
     * @param flags [ItemFlag] to add.
     */
    fun addFlags(vararg flags: ItemFlag) {
        editMeta {
            it.addItemFlags(*flags)
        }
    }

    /**
     * Remove [ItemFlag] from the [ItemStack].
     * @param flags [ItemFlag] to remove.
     */
    fun removeFlags(vararg flags: ItemFlag) {
        editMeta {
            it.removeItemFlags(*flags)
        }
    }
}