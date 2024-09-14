package me.tech.mcchestui.paper

import me.tech.mcchestui.GUIType
import net.kyori.adventure.text.Component
import org.bukkit.plugin.java.JavaPlugin

class PaperGUI(
    /** Title of GUI. */
    title: Component,
    /** Type of GUI to render. */
    type: GUIType,
    /** Wrapped GUI Inventory. */
    override val inventory: PaperInventory,
    /** Whether the current GUI is attached to a parent. */
    attached: Boolean,
    /** Render GUI State. */
    render: PaperGUIRender = { }
) : AbstractPaperGUI(title, type, inventory, attached, render) {
    init {
        registerListeners()
    }

    override fun registerListeners() {
        initialized = true
    }

    override fun unregister() {
        unregistered = false
        initialized = false
    }

    private companion object {
        /** [JavaPlugin] instance. */
        val pluginInstance: JavaPlugin
            get() = JavaPlugin.getProvidingPlugin(PaperGUI::class.java)
    }
}