package me.tech.mcchestui

import me.tech.mcchestui.item.GUIItem
import net.kyori.adventure.text.Component

/**
 * GUI wrapper over the server implementations inventory system.
 * @param I the type of [GUIItem] this GUI uses.
 * @param C the type of GUI Slot Click Handler this GUI uses.
 */
abstract class GUI<I : GUIItem, C : Any>(
    /** Title of GUI. */
    val title: Component,
    /** Type of GUI to render. */
    val type: GUIType,
    /** Wrapped GUI Inventory. */
    open val inventory: GUIInventory<I>,
    /** Whether the current GUI is attached to a parent. */
    attached: Boolean,
    /** Render GUI State. */
    private val render: GUIRender = { }
) {
    /** Allow for items to be placed into the [GUI]. */
    var allowItemPlacement = false

    /** Allow for items to be shift-clicked into the [GUI], requires [allowItemPlacement] to be **true**. */
    var allowShiftClick = false

    /** Allow for items to be dragged within the [GUI]. */
    var allowItemDrag = false

    /** Allow for items to be taken from the [GUI]. */
    var allowItemPickup = false

    /** Allow for hotkeys to be used to pickup & place items within the [GUI]. */
    var allowHotBarSwap = false

    /** Whether this [GUI] is supposed to be persisted in memory or automatically unregistered. */
    var singleInstance = false

    /** The [Slot] that compose the [GUI]. */
    var slots = arrayOfNulls<Slot>(type.totalSize)

    /** Whether the [GUI] is currently initialized. */
    protected var initialized = false

    /** Whether the [GUI] is currently registered or not. */
    var unregistered = false
        protected set

    inner class Slot {
        var item: I? = null
        var allowPickup = false
        var onClick: C? = null

        fun onClick(builder: C) {
            this.onClick = builder
        }
    }

    fun refresh() {
        // wipe inv clean.
        inventory.clear()
        slots = arrayOfNulls(type.totalSize)

        // dispatch render task.
        this.render()
    }

    /**
     * Set the item in a specific GUI slot.
     * @param index slot index
     * @param slot slot
     */
    fun slot(index: Int, slot: Slot) {
        inventory.setItem(index, slot.item)
        slots[index] = slot
    }

    /**
     * Set the item in a specific GUI slot.
     * @param index slot index
     * @param builder slot builder
     */
    fun slot(index: Int, builder: Slot.() -> Unit) {
        slot(index, Slot().apply(builder))
    }

    /**
     * Set the item in a specific GUI slot.
     * @param x x-coordinate of the slot
     * @param y y-coordinate of the slot
     * @param slot slot
     */
    fun slot(x: Int, y: Int, slot: Slot) {
        slot(toSlot(x, y, type), slot)
    }

    /**
     * Set the item in a specific GUI slot.
     * @param x x-coordinate of the slot
     * @param y y-coordinate of the slot
     * @param builder slot builder
     */
    fun slot(x: Int, y: Int, builder: Slot.() -> Unit) {
        slot(toSlot(x, y, type), builder)
    }

    /**
     * Fill the designated area of a GUI.
     * Will fill item in a rectangular shape based on points provided.
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     * @param builder slot builder
     */
    fun fill(
        x1: Int, y1: Int, x2: Int, y2: Int,
        builder: Slot.() -> Unit
    ) {
        val dx = if (x1 < x2) x1..x2 else x2..x1
        val dy = if (y1 < y2) y1..y2 else y2..y1

        for (x in dx) for (y in dy) slot(x, y, builder)
    }

    /**
     * Completely fill the outer border of a GUI.
     * @param builder slot builder
     */
    fun fillBorder(builder: Slot.() -> Unit) {
        all(builder)

        val x1 = 2
        // Just makes it work with 1 row chest guis.
        val y1 = if(type is GUIType.Chest && type.rows == 1 || type is GUIType.Hopper) 1 else 2

        val x2 = type.slotsPerRow - 1
        // Doesn't really matter if we hard code these values,
        // what're they gonna do? Change? well besides chest guis.
        val y2 = when(type) {
            is GUIType.Chest -> if(type.rows > 2) type.rows - 1 else 2
            is GUIType.Hopper -> 1
            is GUIType.Dispenser -> 2
            else -> error("invalid type specified $type")
        }

        fill(x1, y1, x2, y2) {
            item = null
        }
    }

    /**
     * Fill all slots of the GUI.
     * @param builder slot builder.
     */
    fun all(builder: Slot.() -> Unit) {
        fill(1, 1, type.slotsPerRow, type.rows, builder)
    }

    /**
     * Set the item of the next available slot not occupied by any item.
     * Any null item slot will be overridden as this method only checks for
     * slots occupied by an ItemStack.
     * @param builder slot builder
     */
    fun nextAvailableSlot(builder: Slot.() -> Unit) {
        val firstEmptySlot = inventory.firstEmpty()
        if(firstEmptySlot == -1) {
            return
        }

        slot(firstEmptySlot, builder)
    }

    abstract fun title(title: Component)

    protected abstract fun registerListeners()

    abstract fun unregister()
}

// we just dont care about the type of gui provided to re-render.
/** GUI Render function. */
typealias GUIRender = GUI<*, *>.() -> Unit