package me.tech.mcchestui

interface GUIType {
    /** The amount of slots per row the inventory has. */
    val slotsPerRow: Int

    /** The amount of rows an inventory has. */
    val rows: Int

    /** @return total amount fo slots an inventory can hold. */
    val totalSize: Int
        get() = slotsPerRow * rows

    class Chest(override val rows: Int): GUIType {
        override val slotsPerRow = 9

        init {
            require(rows >= 1) { "rows must be above 0" }
            require(rows <= 6) { "rows must be below 6" }
        }
    }

    data object Dispenser : GUIType {
        override val slotsPerRow = 3

        override val rows = 1
    }

    data object Hopper : GUIType {
        override val slotsPerRow = 5

        override val rows = 1
    }
}