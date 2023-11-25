package me.tech.development

import me.tech.mcchestui.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class DevelopmentPlugin : JavaPlugin() {
    override fun onEnable() {
        getCommand("openui")?.setExecutor { sender, _, _, _ ->
            (sender as? Player)?.openGUI(open(sender))
            true
        }
    }

    private fun open(player: Player): GUI {
        return gui(
            this,
            title = Component.text("Test UI"),
            type = GUIType.Chest(rows = 3)
        ) {
            slot(0, 0) {
                item = item(Material.CAKE)
                onClick = { it.sendMessage("You've clicked the cake!") }
            }
        }
    }
}