package me.tech.mcchestui.paper

import me.tech.mcchestui.AbstractGUI
import me.tech.mcchestui.GUIRender
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.paper.listener.GUIListener
import me.tech.mcchestui.paper.listener.item.GUIHotbarListener
import me.tech.mcchestui.paper.listener.item.GUIItemPickupListener
import me.tech.mcchestui.paper.listener.item.GUIItemPlaceListener
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.entity.HumanEntity
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNCHECKED_CAST")
class PaperGUI(
    /** Title of the GUI. */
    title: Component,
    /** Type of GUI to render. */
    type: GUIType,
    /** Wrapped GUI Inventory. */
    inventory: PaperInventory,
    /** Whether the current GUI is attached to a parent. */
    attached: Boolean,
    /** Render GUI State. */
    render: PaperGUIRender = { }
) : AbstractPaperGUI(title, type, inventory, attached, (render as GUIRender)) {
    override var inventory: PaperInventory = inventory
        private set

    /** Dispatched when an [ItemStack] is placed into a [AbstractGUI]. */
    var onPlaceItem: GUIItemPlaceEvent? = null

    /** Dispatched when a [ItemStack] not registered to a [AbstractGUI.Slot] is taken from a [AbstractGUI]. */
    var onPickupItem: GUIItemPickupEvent? = null

    /** Dispatched when a [ItemStack] is taken from a [PlayerInventory]. */
    var onPlayerInventoryPickupItem: GUIItemPickupEvent? = null

    var onCloseInventory: GUICloseEvent? = null

    private val eventListeners = listOf(
        GUIListener(this),
        GUIItemPickupListener(this),
        GUIItemPlaceListener(this),
        GUIHotbarListener(this)
    )

    init {
        registerListeners()
    }

    override fun guiTitle(title: Component) {
        val clone = inventory.bukkitInventory
            .clone(title, type)

        inventory.bukkitInventory
            .viewers
            .toList() // copy
            .forEach {
                it.ignoreCloseDispatcher { it.openInventory(clone) }
            }

        inventory = PaperInventory(clone)
    }

    override fun registerListeners() {
        super.registerListeners()

        eventListeners.forEach {
            pluginInstance.server.pluginManager.registerEvents(it, pluginInstance)
        }

        initialized = true
    }

    override fun unregister() {
        super.unregister()

        // close inv for all viewers.
        inventory.bukkitInventory
            .viewers
            .toList()
            .forEach(HumanEntity::closeInventory)

        eventListeners.forEach(HandlerList::unregisterAll)

        unregistered = false
        initialized = false
    }

    internal companion object {
        /** Namespace used to denote when a InventoryClose should be ignored for the player. */
        val ignoreNamespace = NamespacedKey("mcchestui", "ignore")

        /** [JavaPlugin] instance. */
        val pluginInstance: JavaPlugin
            get() = JavaPlugin.getProvidingPlugin(PaperGUI::class.java)

        fun HumanEntity.ignoreCloseDispatcher(action: () -> Unit) {
            persistentDataContainer.set(ignoreNamespace, PersistentDataType.BOOLEAN, true)
            action()
            persistentDataContainer.remove(ignoreNamespace)
        }
    }
}