package me.tech.mcchestui.minestom.item

import me.tech.mcchestui.GUI
import me.tech.mcchestui.item.GUIItem
import me.tech.mcchestui.minestom.MinestomGUI
import me.tech.mcchestui.minestom.item.MinestomGUIItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.minestom.server.component.DataComponent
import net.minestom.server.item.ItemComponent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.TagReadable

fun GUI<MinestomGUIItem, *>.Slot.item(
    type: Material = Material.AIR,
    builder: MinestomGUIItem.() -> Unit = { }
): MinestomGUIItem {
    return MinestomGUIItem(type)
        .apply(builder)
}

class MinestomGUIItem(
    val stack: ItemStack
) : TagReadable by stack, DataComponent.Holder by stack, HoverEventSource<HoverEvent.ShowItem> by stack, GUIItem {
    constructor(type: Material)
        : this(ItemStack.of(type))

    override var removeParentItalics = true

    override var name: Component?
        get() = stack.get(ItemComponent.CUSTOM_NAME)
        set(value) {
            requireNotNull(value) { "provided name must not be null" }
            stack.withCustomName(value)
        }

    override var lore: Collection<Component>?
        get() = stack.get(ItemComponent.LORE)
        set(value) {
            stack.withLore(value?.toList() ?: emptyList())
        }

    var material: Material
        get() = stack.material()
        set(value) { stack.withMaterial(value) }

    fun displayName(name: Component) {
        this.name = name
    }
}