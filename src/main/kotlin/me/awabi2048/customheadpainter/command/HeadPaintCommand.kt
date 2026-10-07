package me.awabi2048.customheadpainter.command

import java.util.UUID
import me.awabi2048.customheadpainter.editor.EditorManager
import me.awabi2048.customheadpainter.localization.PainterI18n
import me.awabi2048.customheadpainter.localization.generated.HeadPainterKeys
import me.awabi2048.customheadpainter.model.HeadLayer
import me.awabi2048.customheadpainter.model.PaintTool
import me.awabi2048.customheadpainter.persistence.ArtworkRepository
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
            sender.sendMessage(PainterI18n.console(HeadPainterKeys.COMMAND_PLAYER_ONLY))
            return true
        }
        if (!player.hasPermission("customheadpainter.use")) {
            player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_NO_PERMISSION))
            return true
        }

        when (args.firstOrNull()?.lowercase() ?: "help") {
            "start" -> {
                val session = manager.startNew(player, args.drop(1).joinToString(" "))
                player.sendMessage(
                    PainterI18n.component(player, HeadPainterKeys.COMMAND_STARTED, "id" to session.artwork.id),
                )
            }
            "open" -> {
                val id = args.getOrNull(1)?.let { runCatching { UUID.fromString(it) }.getOrNull() }
                if (id == null || manager.open(player, id) == null) {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_ARTWORK_NOT_FOUND))
                } else {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_OPENED, "id" to id))
                }
            }
            "stop" -> {
                manager.stop(player)
                player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_STOPPED))
            }
            "save" -> {
                val artwork = manager.save(player)
                if (artwork == null) {
                    noSession(player)
                } else {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_SAVED, "id" to artwork.id))
                }
            }
            "publish" -> if (!manager.publish(player)) noSession(player)
            "color" -> {
                val argb = args.getOrNull(1)?.let(::parseColor)
                if (argb == null) {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_USAGE_COLOR))
                } else if (!manager.setColor(player, argb)) {
                    noSession(player)
                } else {
                    player.sendMessage(
                        PainterI18n.component(player, HeadPainterKeys.COMMAND_COLOR_SET, "color" to formatColor(argb)),
                    )
                }
            }
            "layer" -> {
                val layer = args.getOrNull(1)?.let {
                    runCatching { HeadLayer.valueOf(it.uppercase()) }.getOrNull()
                }
                if (layer == null) {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_USAGE_LAYER))
                } else if (!manager.setLayer(player, layer)) {
                    noSession(player)
                } else {
                    player.sendMessage(
                        PainterI18n.component(player, HeadPainterKeys.COMMAND_LAYER_SET, "layer" to layerLabel(player, layer)),
                    )
                }
            }
            "tool" -> {
                val tool = args.getOrNull(1)?.let {
                    runCatching { PaintTool.valueOf(it.uppercase()) }.getOrNull()
                }
                if (tool == null) {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_USAGE_TOOL))
                } else if (!manager.setTool(player, tool)) {
                    noSession(player)
                } else {
                    player.sendMessage(
                        PainterI18n.component(player, HeadPainterKeys.COMMAND_TOOL_SET, "tool" to toolLabel(player, tool)),
                    )
                }
            }
            "list" -> {
                val artworks = repository.list(player.uniqueId)
                if (artworks.isEmpty()) {
                    player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_LIST_EMPTY))
                } else {
                    artworks.take(20).forEach {
                        player.sendMessage(
                            PainterI18n.component(
                                player,
                                HeadPainterKeys.COMMAND_LIST_ENTRY,
                                "id" to it.id,
                                "name" to it.name,
                            ),
                        )
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
        player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_HELP_TITLE))
        PainterI18n.componentList(player, HeadPainterKeys.COMMAND_HELP_LINES).forEach(player::sendMessage)
    }

    private fun noSession(player: Player) {
        player.sendMessage(PainterI18n.component(player, HeadPainterKeys.COMMAND_NO_SESSION))
    }

    private fun layerLabel(player: Player, layer: HeadLayer): String = PainterI18n.text(
        player,
        when (layer) {
            HeadLayer.BASE -> HeadPainterKeys.LAYER_BASE
            HeadLayer.OVERLAY -> HeadPainterKeys.LAYER_OVERLAY
        },
    )

    private fun toolLabel(player: Player, tool: PaintTool): String = PainterI18n.text(
        player,
        when (tool) {
            PaintTool.PAINT -> HeadPainterKeys.TOOL_PAINT
            PaintTool.ERASE -> HeadPainterKeys.TOOL_ERASE
        },
    )

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
