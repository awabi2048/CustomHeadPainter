package me.awabi2048.customheadpainter

import me.awabi2048.customheadpainter.command.HeadPaintCommand
import me.awabi2048.customheadpainter.editor.EditorInteractionListener
import me.awabi2048.customheadpainter.editor.EditorManager
import me.awabi2048.customheadpainter.persistence.ArtworkRepository
import me.awabi2048.customheadpainter.publish.MineSkinPublisher
import org.bukkit.plugin.java.JavaPlugin

class CustomHeadPainterPlugin : JavaPlugin() {
    private lateinit var editorManager: EditorManager

    override fun onEnable() {
        saveDefaultConfig()

        val repository = ArtworkRepository(this)
        val publisher = MineSkinPublisher(this)
        editorManager = EditorManager(this, repository, publisher)

        val command = HeadPaintCommand(editorManager, repository)
        getCommand("headpaint")?.apply {
            setExecutor(command)
            tabCompleter = command
        } ?: error("headpaint command is missing from plugin.yml")

        server.pluginManager.registerEvents(EditorInteractionListener(editorManager), this)
        logger.info(
            if (publisher.isConfigured()) {
                "CustomHeadPainter enabled; MineSkin publishing is configured."
            } else {
                "CustomHeadPainter enabled; editing/saving works, but publishing requires mineskin.api-key."
            },
        )
    }

    override fun onDisable() {
        if (::editorManager.isInitialized) {
            editorManager.shutdown()
        }
    }
}
