package me.awabi2048.customheadpainter.editor

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import me.awabi2048.customheadpainter.model.HeadArtwork
import me.awabi2048.customheadpainter.model.HeadLayer
import me.awabi2048.customheadpainter.model.PaintTool
import me.awabi2048.customheadpainter.persistence.ArtworkRepository
import me.awabi2048.customheadpainter.publish.HeadItemFactory
import me.awabi2048.customheadpainter.publish.MineSkinPublisher
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class EditorManager(
    private val plugin: JavaPlugin,
    private val repository: ArtworkRepository,
    private val publisher: MineSkinPublisher,
) {
    private val renderer = EditorRenderer(plugin)
    private val sessions = ConcurrentHashMap<UUID, EditorSession>()
    private val interactionOwners = ConcurrentHashMap<UUID, UUID>()

    fun startNew(player: Player, name: String): EditorSession {
        val artwork = HeadArtwork(owner = player.uniqueId, name = name.ifBlank { "Untitled Head" })
        return start(player, artwork)
    }

    fun open(player: Player, artworkId: UUID): EditorSession? {
        val artwork = repository.load(artworkId) ?: return null
        if (artwork.owner != player.uniqueId && !player.hasPermission("customheadpainter.admin")) return null
        return start(player, artwork)
    }

    private fun start(player: Player, artwork: HeadArtwork): EditorSession {
        stop(player)
        val direction = player.eyeLocation.direction.clone().normalize()
        val center = player.eyeLocation.clone().add(direction.multiply(1.5))
        val anchor = center.subtract(0.0, EditorHitMapper.EDITOR_SIZE / 2.0, 0.0)
        val session = EditorSession(player.uniqueId, artwork, anchor)
        renderer.spawn(session)
        sessions[player.uniqueId] = session
        session.interactionId?.let { interactionOwners[it] = player.uniqueId }
        return session
    }

    fun stop(player: Player) {
        val session = sessions.remove(player.uniqueId) ?: return
        session.interactionId?.let { interactionOwners.remove(it) }
        renderer.despawn(session)
    }

    fun shutdown() {
        sessions.values.toList().forEach { session ->
            session.interactionId?.let { interactionOwners.remove(it) }
            renderer.despawn(session)
        }
        sessions.clear()
    }

    fun session(player: Player): EditorSession? = sessions[player.uniqueId]

    fun session(interaction: Interaction): EditorSession? {
        val owner = interactionOwners[interaction.uniqueId] ?: return null
        return sessions[owner]
    }

    fun applyClick(player: Player, interaction: Interaction, clickedPosition: Vector): Boolean {
        val session = session(interaction) ?: return false
        if (session.playerId != player.uniqueId && !player.hasPermission("customheadpainter.admin")) return false

        val cell = EditorHitMapper.map(clickedPosition)
        val argb = when (session.tool) {
            PaintTool.PAINT -> session.selectedArgb
            PaintTool.ERASE -> 0x00000000
        }
        session.artwork.canvas.set(session.selectedLayer, cell, argb)
        session.artwork.touch()
        renderer.updateFace(session, cell.face)
        return true
    }

    fun save(player: Player): HeadArtwork? {
        val artwork = sessions[player.uniqueId]?.artwork ?: return null
        repository.save(artwork)
        return artwork
    }

    fun publish(player: Player): Boolean {
        val session = sessions[player.uniqueId] ?: return false
        repository.save(session.artwork)
        player.sendMessage(Component.text("Publishing head texture...", NamedTextColor.YELLOW))

        publisher.publish(session.artwork).whenComplete { skin, throwable ->
            plugin.server.scheduler.runTask(plugin, Runnable {
                if (!player.isOnline) return@Runnable
                if (throwable != null) {
                    plugin.logger.warning("Failed to publish artwork ${session.artwork.id}: ${throwable.message}")
                    player.sendMessage(
                        Component.text(
                            "Publish failed: ${rootMessage(throwable)}",
                            NamedTextColor.RED,
                        ),
                    )
                    return@Runnable
                }

                session.artwork.publishedTextureUrl = skin.textureUrl
                repository.save(session.artwork)
                val item = HeadItemFactory.create(session.artwork.name, skin)
                val leftovers = player.inventory.addItem(item)
                leftovers.values.forEach { player.world.dropItemNaturally(player.location, it) }
                player.sendMessage(Component.text("Published and added the head to your inventory.", NamedTextColor.GREEN))
            })
        }
        return true
    }

    fun setColor(player: Player, argb: Int): Boolean {
        val session = sessions[player.uniqueId] ?: return false
        session.selectedArgb = argb
        session.tool = PaintTool.PAINT
        return true
    }

    fun setLayer(player: Player, layer: HeadLayer): Boolean {
        val session = sessions[player.uniqueId] ?: return false
        session.selectedLayer = layer
        return true
    }

    fun setTool(player: Player, tool: PaintTool): Boolean {
        val session = sessions[player.uniqueId] ?: return false
        session.tool = tool
        return true
    }

    private fun rootMessage(throwable: Throwable): String {
        var current = throwable
        while (current.cause != null && current.cause !== current) current = current.cause!!
        return current.message ?: current.javaClass.simpleName
    }
}
