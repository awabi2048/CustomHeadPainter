package me.awabi2048.customheadpainter.persistence

import java.io.File
import java.time.Instant
import java.util.UUID
import me.awabi2048.customheadpainter.model.HeadArtwork
import me.awabi2048.customheadpainter.model.HeadCanvas
import me.awabi2048.customheadpainter.model.HeadFace
import me.awabi2048.customheadpainter.model.HeadLayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

class ArtworkRepository(
    plugin: JavaPlugin,
) {
    private val directory = File(plugin.dataFolder, "artworks").apply { mkdirs() }

    fun save(artwork: HeadArtwork) {
        artwork.touch()
        val yaml = YamlConfiguration()
        yaml.set("id", artwork.id.toString())
        yaml.set("owner", artwork.owner.toString())
        yaml.set("name", artwork.name)
        yaml.set("created-at", artwork.createdAt.toString())
        yaml.set("updated-at", artwork.updatedAt.toString())
       yaml.set("published-texture-url", artwork.publishedTextureUrl)

        HeadLayer.entries.forEach { layer ->
            HeadFace.entries.forEach { face ->
                yaml.set(
                    "pixels.${layer.name.lowercase()}.${face.name.lowercase()}",
                    artwork.canvas.facePixels(layer, face),
                )
            }
        }
        yaml.save(file(artwork.id))
    }

    fun load(id: UUID): HeadArtwork? {
        val file = file(id)
        if (!file.isFile) return null
        val yaml = YamlConfiguration.loadConfiguration(file)
        val owner = yaml.getString("owner")?.let(UUID::fromString) ?: return null
        val name = yaml.getString("name") ?: id.toString()
        val canvas = HeadCanvas()

        HeadLayer.entries.forEach { layer ->
            HeadFace.entries.forEach { face ->
                val path = "pixels.${layer.name.lowercase()}.${face.name.lowercase()}"
                val values = yaml.getIntegerList(path)
                if (values.size == HeadCanvas.PIXELS_PER_FACE) {
                    canvas.replaceFace(layer, face, values)
                }
            }
        }

        return HeadArtwork(
            id = id,
            owner = owner,
            name = name,
            canvas = canvas,
            createdAt = yaml.getString("created-at")?.let(Instant::parse) ?: Instant.now(),
            updatedAt = yaml.getString("updated-at")?.let(Instant::parse) ?: Instant.now(),
            publishedTextureUrl = yaml.getString("published-texture-url"),
        )
    }

    fun list(owner: UUID): List<HeadArtwork> = directory
        .listFiles { file -> file.isFile && file.extension.equals("yml", ignoreCase = true) }
        .orEmpty()
        .mapNotNull { runCatching { load(UUID.fromString(it.nameWithoutExtension)) }.getOrNull() }
        .filter { it.owner == owner }
        .sortedByDescending { it.updatedAt }

    private fun file(id: UUID): File = File(directory, "$id.yml")
}
