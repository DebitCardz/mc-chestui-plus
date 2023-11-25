package me.tech.mcchestui.template

import me.tech.mcchestui.GUI
import me.tech.mcchestui.guiSlot

/**
 * Representing the structure of the GUI Template.
 */
data class GUITemplate(
    var firstRow: String? = null,
    var secondRow: String? = null,
    var thirdRow: String? = null,
    var fourthRow: String? = null,
    var fifthRow: String? = null,
    var sixthRow: String? = null,

    var rows: String? = null
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
        return rowsMap()
            .toMutableMap()
            .apply { putAll(selectRowsMap().filterValues { it.isNotEmpty() }) }
    }

    private fun rowsMap(): Map<Int, List<Char>> {
        return rows
            ?.split("\n")
            ?.withIndex()
            ?.associate { it.index + 1 to toCharList(it.value) }
            ?: emptyMap()
    }

    private fun selectRowsMap(): Map<Int, List<Char>> {
        return listOf(firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow)
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
 * @param builder template builder
 */
fun GUI.template(builder: GUITemplate.() -> Unit) {
    val map = GUITemplate().apply(builder)
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