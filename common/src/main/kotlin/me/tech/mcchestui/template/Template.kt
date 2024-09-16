package me.tech.mcchestui.template

/**
 * Converts template strings into a [Map].
 *
 * Will automatically override [rows] with each select row
 * so both options work with each other, select rows will take priority over
 * [rows] so it can override it.
 *
 * @return list of characters mapped to the row.
 */
internal fun toTemplateMap(rows: Array<out String>): Map<Int, List<Char>> {
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
 * @return list of each character
 */
private fun toCharList(str: String?): List<Char> {
    return str
        ?.filterNot { it.isWhitespace() }
        ?.map { it }
        ?: return emptyList()
}
