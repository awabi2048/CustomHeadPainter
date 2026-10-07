package me.awabi2048.customheadpainter

import me.awabi2048.customheadpainter.command.HeadPaintCommand
import me.awabi2048.customheadpainter.editor.EditorInteractionListener
import me.awabi2048.customheadpainter.editor.EditorManager
import me.awabi2048.customheadpainter.localization.PainterI18n
import me.awabi2048.customheadpainter.localization.generated.HeadPainterKeys
import me.awabi2048.customheadpainter.persistence.ArtworkRepository
import me.awabi2048.customheadpainter.publish.MineSkinPublisher
import org.bukkit.plugin.java.JavaPlugin

class CustomHeadPainterPlugin : JavaPlugin() {
    private lateinit var editorManager: EditorManager

    override fun onEnable() {
        saveDefaultConfig()

        // 言語キーの不足・型不正はここで例外となり、欠損したまま起動しない
        PainterI18n.init(this)

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
            PainterI18n.console(
                if (publisher.isConfigured()) {
                    HeadPainterKeys.LOG_ENABLED_WITH_PUBLISH
                } else {
                    HeadPainterKeys.LOG_ENABLED_NO_API_KEY
                },
            ),
        )
    }

    override fun onDisable() {
        if (::editorManager.isInitialized) {
            editorManager.shutdown()
        }
    }
}
