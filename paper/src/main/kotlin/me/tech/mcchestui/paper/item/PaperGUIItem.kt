package me.tech.mcchestui.paper.item

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.item.GUIItem
import me.tech.mcchestui.paper.GUISlotClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material
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

    fun displayName(name: Component?) {
        this.name = name
    }
}