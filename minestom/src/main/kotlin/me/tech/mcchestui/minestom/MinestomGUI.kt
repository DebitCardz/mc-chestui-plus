package me.tech.mcchestui.minestom

import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.minestom.listener.*
import me.tech.mcchestui.minestom.listener.GUICloseListener
import me.tech.mcchestui.minestom.listener.GUIItemPickupListener
import me.tech.mcchestui.minestom.listener.GUISlotClickListener
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventBinding
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.inventory.Inventory
import java.util.*

@Suppress("UNCHECKED_CAST")
class MinestomGUI(
    /** Title of the GUI. */
    title: Component,
    /** Type of GUI to render. */
    type: GUIType,
    /** Wrapped GUI Inventory. */
    override val inventory: MinestomInventory,
    /** Whether the current GUI is attached to a parent. */
    attached: Boolean,
    /** Render GUI State. */
    render: MinestomGUIRender = { }
) : AbstractMinestomGUI(title, type, inventory, attached, (render as GUIRender)) {
    var onPickupItem: GUIItemPickupEvent? = null

    var onPlayerInventoryPickupItem: GUIItemPickupEvent? = null

    var onPlaceItem: GUIItemPlaceEvent? = null

    var onCloseInventory: GUICloseEvent? = null

    private val listeners = listOf(
        GUISlotClickListener(this),
        GUICloseListener(this),
        GUIItemPickupListener(this),
        GUIPlayerInventoryPickupListener(this),
        GUIItemPlaceListener(this)
    ).map { it.listener() }

    init {
        registerListeners()
    }

    override fun guiTitle(title: Component) {
        // sends update window packet to viewers.
        inventory.minestomInventory.title = title
    }

    override fun registerListeners() {
        super.registerListeners()

        listeners.forEach(GUI_EVENT_NODE::addListener)

        initialized = true
    }

    override fun unregister() {
        super.unregister()

        // close inv for all viewers.
        inventory.minestomInventory
            .viewers
            .toList()
            .forEach(Player::closeInventory)

        listeners.forEach(GUI_EVENT_NODE::removeListener)

        unregistered = true
        initialized = false
    }

    private companion object {
        val GUI_EVENT_NODE = EventNode.type("mc-chestui", EventFilter.INVENTORY)
            .apply { priority = 1 }

        init {
            MinecraftServer.getGlobalEventHandler()
                .addChild(GUI_EVENT_NODE)
        }

        /**
         * Generate a random event node id.
         * @param prefix the prefix of the id
         * @return the generated id formatted as `<prefix>-<random>`.
         */
        fun eventNodeId(prefix: String): String =
            "$prefix-${UUID.randomUUID().toString().substring(0, 8)}"
    }
}