package me.awabi2048.customheadpainter.command

import java.util.UUID
import me.awabi2048.customheadpainter.editor.EditorManager
import me.awabi2048.customheadpainter.model.HeadLayer
import me.awabi2048.customheadpainter.model.PaintTool
import me.awabi2048.customheadpainter.persistence.ArtworkRepository
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class HeadPaintCommand(
    private val manager: EditorManager,
    private val repository: ArtworkRepository,
) : TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        val player = sender as? Player ?: run {
            sender.sendMessage("This command is player-only.")
            return true
        }
        if (!player.hasPermission("customheadpainter.use")) {
            player.sendMessage(Component.text("You do not have permission.", NamedTextColor.RED))
            return true
        }

        when (args.firstOrNull()?.lowercase() ?: "help") {
            "start" -> {
                val name = args.drop(1).joinToString(" ").ifBlank { "Untitled Head" }
                val session = manager.startNew(player, name)
                player.sendMessage(
                    Component.text("Started editor ${session.artwork.id}", NamedTextColor.GREEN),
                )
            }
            "open" -> {
                val id = args.getOrNull(1)?.let { runCatching { UUID.fromString(it) }.getOrNull() }
                if (id == null || manager.open(player, id) == null) {
                    player.sendMessage(Component.text("Artwork not found or not accessible.", NamedTextColor.RED))
                } else {
                    player.sendMessage(Component.text("Opened artwork $id", NamedTextColor.GREEN))
                }
            }
            "stop" -> {
                manager.stop(player)
                player.sendMessage(Component.text("Editor closed.", NamedTextColor.GRAY))
            }
            "save" -> {
                val artwork = manager.save(player)
                if (artwork == null) {
                    noSession(player)
                } else {
                    player.sendMessage(Component.text("Saved ${artwork.id}", NamedTextColor.GREEN))
                }
            }
            "publish" -> if (!manager.publish(player)) noSession(player)
            "color" -> {
                val argb = args.getOrNull(1)?.let(::parseColor)
                if (argb == null) {
                    player.sendMessage(Component.text("Usage: /headpaint color <#RRGGBB|#AARRGGBB>", NamedTextColor.RED))
                } else if (!manager.setColor(player, argb)) {
                    noSession(player)
                } else {
                    player.sendMessage(Component.text("Paint color set to ${formatColor(argb)}", NamedTextColor.GREEN))
                }
            }
            "layer" -> {
                val layer = args.getOrNull(1)?.let {
                    runCatching { HeadLayer.valueOf(it.uppercase()) }.getOrNull()
                }
                if (layer == null) {
                    player.sendMessage(Component.text("Usage: /headpaint layer <base|overlay>", NamedTextColor.RED))
                } else if (!manager.setLayer(player, layer)) {
                    noSession(player)
                } else {
                    player.sendMessage(Component.text("Layer: ${layer.name.lowercase()}", NamedTextColor.GREEN))
                }
            }
            "tool" -> {
                val tool = args.getOrNull(1)?.let {
                    runCatching { PaintTool.valueOf(it.uppercase()) }.getOrNull()
                }
                if (tool == null) {
                    player.sendMessage(Component.text("Usage: /headpaint tool <paint|erase>", NamedTextColor.RED))
                } else if (!manager.setTool(player, tool)) {
                    noSession(player)
                } else {
                    player.sendMessage(Component.text("Tool: ${tool.name.lowercase()}", NamedTextColor.GREEN))
                }
            }
            "list" -> {
                val artworks = repository.list(player.uniqueId)
                if (artworks.isEmpty()) {
                    player.sendMessage(Component.text("No saved artworks.", NamedTextColor.GRAY))
                } else {
                    artworks.take(20).forEach {
                        player.sendMessage(Component.text("${it.id}  ${it.name}", NamedTextColor.AQUA))
                    }
                }
            }
            else -> showHelp(player)
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>,
    ): List<String> {
        if (args.size == 1) {
            return SUBCOMMANDS.filter { it.startsWith(args[0], ignoreCase = true) }
        }
        if (args.size == 2) {
            return when (args[0].lowercase()) {
                "layer" -> listOf("base", "overlay").filter { it.startsWith(args[1], ignoreCase = true) }
                "tool" -> listOf("paint", "erase").filter { it.startsWith(args[1], ignoreCase = true) }
                else -> emptyList()
            }
        }
        return emptyList()
    }

    private fun showHelp(player: Player) {
        player.sendMessage(Component.text("CustomHeadPainter", NamedTextColor.GOLD))
        player.sendMessage(Component.text("/headpaint start [name]", NamedTextColor.GRAY))
        player.sendMessage(Component.text("/headpaint color <#RRGGBB|#AARRGGBB>", NamedTextColor.GRAY))
        player.sendMessage(Component.text("/headpaint layer <base|overlay>", NamedTextColor.GRAY))
        player.sendMessage(Component.text("/headpaint tool <paint|erase>", NamedTextColor.GRAY))
        player.sendMessage(Component.text("/headpaint save | publish | stop", NamedTextColor.GRAY))
        player.sendMessage(Component.text("/headpaint list | open <uuid>", NamedTextColor.GRAY))
    }

    private fun noSession(player: Player) {
        player.sendMessage(Component.text("Start or open an editor first.", NamedTextColor.RED))
    }

    private fun parseColor(input: String): Int? {
        val hex = input.removePrefix("#")
        return runCatching {
            when (hex.length) {
                6 -> (0xFF000000L or hex.toLong(16)).toInt()
                8 -> hex.toLong(16).toInt()
                else -> return null
            }
        }.getOrNull()
    }

    private fun formatColor(argb: Int): String = "#%08X".format(argb)

    companion object {
        private val SUBCOMMANDS = listOf("start", "open", "stop", "save", "publish", "color", "layer", "tool", "list", "help")
    }
}
