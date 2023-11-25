package me.tech.development

import me.tech.mcchestui.*
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class DevelopmentPlugin : JavaPlugin(), Listener {
    override fun onEnable() {
        getCommand("openui")?.setExecutor { sender, _, _, _ ->
            (sender as? Player)?.openGUI(open(sender))
            true
        }

        // op & set gamemode on local server.
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun PlayerJoinEvent.on() {
                player.gameMode = GameMode.CREATIVE
                player.isOp = true
            }
        }, this)
    }

    private fun open(player: Player): GUI {
        return gui(
            this,
            title = Component.text("Test UI"),
            type = GUIType.Chest(rows = 3)
        ) {
            slot(1, 1) {
                item = item(Material.CAKE)
                onClick = { it.sendMessage("You've clicked the cake!") }
            }
        }
    }
}