package me.tech.mcchestui.template

import me.tech.mcchestui.GUI
import me.tech.mcchestui.guiSlot

/**
 * Representing the structure of the GUI Template.
 */
class GUITemplate(
    private val rows: Array<out String>
) {
    /**
     * Converts [GUITemplate] into a [Map].
     *
     * Will automatically override [rows] with each select row
     * so both options work with each other, select rows will take priority over
     * [rows] so it can override it.
     *
     * @return list of characters mapped to the row.
     */
    internal fun toMap(): Map<Int, List<Char>> {
        if(rows.isEmpty()) {
            return emptyMap()
        }

        if(rows.size == 1) {
            val row = rows.first()
            if(row.contains("\n")) {
                return row
                    .split("\n")
                    .withIndex()
                    .associate { it.index + 1 to toCharList(it.value) }
            }

            return mapOf(1 to toCharList(row))
        }

        return rows
            .withIndex()
            .associate { it.index + 1 to toCharList(it.value) }
    }

    /**
     * Automatically removes whitespace and maps [Char].
     *
     * @return list of each character
     */
    private fun toCharList(str: String?): List<Char> {
        return str
            ?.filterNot { it.isWhitespace() }
            ?.map { it }
            ?: return emptyList()
    }
}

/**
 * Define chars to be used for templating.
 *
 * @param char template character
 * @param builder slot builder
 */
fun GUI.addTemplateSlot(char: Char, builder: GUI.Slot.() -> Unit) {
    templateSlots[char] = guiSlot(builder)
}

/**
 * Define chars to be used for templating.
 *
 * @param char template character
 * @param slot slot
 */
fun GUI.addTemplateSlot(char: Char, slot: GUI.Slot) {
    templateSlots[char] = slot
}

/**
 * Structure the template of the [GUI].
 *
 * @param template [GUI] string template.
 */
fun GUI.template(vararg template: String) {
    val map = GUITemplate(template)
        .toMap()

    for((yIndex, chars) in map) {
        if(chars.isEmpty()) {
            continue
        }

        for((xIndex, char) in chars.withIndex()) {
            val slot = templateSlots[char]
                ?: continue

            slot(xIndex + 1, yIndex, slot)
        }
    }
}