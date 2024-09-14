package me.tech.mcchestui

import me.tech.mcchestui.item.GUIItem

interface GUIInventory <T : GUIItem> {
    val size: Int

    fun setItem(index: Int, item: T?)

    fun clear()

    fun firstEmpty(): Int
}