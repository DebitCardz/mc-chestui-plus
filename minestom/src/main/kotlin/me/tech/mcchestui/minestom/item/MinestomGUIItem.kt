package me.tech.mcchestui.minestom.item

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.item.GUIItem
import me.tech.mcchestui.minestom.GUISlotClickEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.component.DataComponent
import net.minestom.server.item.ItemComponent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.TagReadable

fun AbstractGUI<MinestomGUIItem, GUISlotClickEvent>.Slot.item(
    type: Material = Material.AIR,
    builder: MinestomGUIItem.() -> Unit = { }
): MinestomGUIItem {
    return MinestomGUIItem(type)
        .apply(builder)
}

class MinestomGUIItem(
    var stack: ItemStack
) : GUIItem {
    constructor(type: Material)
        : this(ItemStack.of(type))

    override var removeParentItalics = true

    override var name: Component?
        get() = stack.get(ItemComponent.CUSTOM_NAME)
        set(value) {
            requireNotNull(value) { "provided name must not be null" }
            stack = stack.withCustomName(value.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
        }

    override var lore: Collection<Component>?
        get() = stack.get(ItemComponent.LORE)
        set(value) { stack = stack.withLore(value?.toList()?.map { it.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) } ?: emptyList()) }

    var material: Material
        get() = stack.material()
        set(value) { stack = stack.withMaterial(value) }

    /** Amount of the [stack]. */
    var amount: Int
        get() = stack.amount()
        set(value) { stack = stack.withAmount(value) }

    override var customModelData: Int?
        get() = stack.get(ItemComponent.CUSTOM_MODEL_DATA)
        set(value) { stack = stack.builder().set(ItemComponent.CUSTOM_MODEL_DATA, value).build() }

    override var glowing: Boolean
        get() = stack.get(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE) ?: false
        set(value) { stack = stack.builder().set(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, value).build() }

    fun displayName(name: Component) {
        this.name = name
    }

    /**
     * Edit the attributes of the item.
     * Applies changes to [stack].
     * @param builder item builder
     */
    fun editItem(builder: ItemStack.Builder.() -> Unit) {
        stack = stack.builder()
            .apply(builder)
            .build()
    }
}