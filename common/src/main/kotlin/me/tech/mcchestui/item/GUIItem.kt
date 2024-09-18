package me.tech.mcchestui.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

interface GUIItem {
    /** Whether to remove default italics from the item. */
    var removeParentItalics: Boolean

    /** The display name of the item. */
    var name: Component?

    /** The lore of the item. */
    var lore: Collection<Component>?

    /** The amount of the item. */
    var amount: Int

    /** The custom model data attached to the item. */
    var customModelData: Int?

    /** Whether the item appears to be glowing or not. */
    var glowing: Boolean

    /**
     * Remove the italics of a [Component] if it is not already
     * set on the [Component].
     * @param component to remove from.
     * @return converted component.
     */
    // we have to basically remake decorationIfAbsent because of issues with
    // backwards compatibility.
    fun removeComponentParentItalics(component: Component): Component {
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
    fun sanitizeLore(lore: Collection<Component>?) = lore?.map {
        it.replaceText { builder ->
            builder.matchLiteral("\n")
            builder.replacement { replace ->
                replace.content("")
            }
        }
    }
}