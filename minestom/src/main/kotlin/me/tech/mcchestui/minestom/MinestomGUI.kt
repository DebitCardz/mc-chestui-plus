package me.tech.mcchestui.minestom

import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.minestom.listener.GUIListener
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
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
    var onCloseInventory: GUICloseEvent? = null

    private val eventNode = EventNode.value(eventNodeId("gui"), EventFilter.INVENTORY) {
        it === this.inventory.minestomInventory
    }

    override fun title(title: Component) {
        // sends update window packet to viewers.
        inventory.minestomInventory.title = title
    }

    override fun registerListeners() {
        val listener = GUIListener(this)

        eventNode.addListener(listener.slotClickNode())
            .addListener(listener.closeNode())
        MinecraftServer.getGlobalEventHandler()
            .addChild(eventNode)

        initialized = true
    }

    override fun unregister() {
        MinecraftServer.getGlobalEventHandler()
            .removeChild(eventNode)

        unregistered = true
        initialized = false
    }

    private companion object {
        /**
         * Generate a random event node id.
         * @param prefix the prefix of the id
         * @return the generated id formatted as `<prefix>-<random>`.
         */
        fun eventNodeId(prefix: String): String =
            "$prefix-${UUID.randomUUID().toString().substring(0, 8)}"
    }
}