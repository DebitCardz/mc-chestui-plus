package me.tech.mcchestui.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

interface GUIItem {
    var removeParentItalics: Boolean

    var name: Component?

    var lore: Collection<Component>?

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