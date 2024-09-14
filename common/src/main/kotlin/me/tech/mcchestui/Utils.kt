package me.tech.mcchestui

/**
 * Convert x/y coordinates to a slot index in
 * an [Inventory].
 *
 * @param x must be above 0 and less than slots per row.
 * @param y must be above 0 and less than rows.
 * @param type [GUIType] for the coordinates to map to.
 * @return [Int] representing the slot index.
 */
fun toSlot(x: Int, y: Int, type: GUIType): Int {
    return (x - 1) + ((y - 1) * type.slotsPerRow)
}