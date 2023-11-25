package me.tech.mcchestui

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
    private val usingRows get() = rows != null

    /**
     * @return list of characters mapped to the row.
     */
    internal fun toMap(): Map<Int, List<Char>> {
        if(usingRows) {
            return rows
                ?.split("\n")
                ?.withIndex()
                ?.associate { it.index + 1 to toCharList(it.value) }
                ?: emptyMap()
        } else {
            return mapOf(
                1 to toCharList(firstRow),
                2 to toCharList(secondRow),
                3 to toCharList(thirdRow),
                4 to toCharList(fourthRow),
                5 to toCharList(fifthRow),
                6 to toCharList(sixthRow)
            )
        }
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