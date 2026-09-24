package me.awabi2048.customheadpainter.publish

import java.io.File
import java.security.MessageDigest
import java.util.concurrent.CompletableFuture
import me.awabi2048.customheadpainter.model.HeadArtwork
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.mineskin.Java11RequestHandler
import org.mineskin.MineSkinClient
import org.mineskin.data.Visibility
import org.mineskin.request.GenerateRequest

data class PublishedSkin(
    val textureUrl: String,
    val textureValue: String,
    val textureSignature: String?,
)

class MineSkinPublisher(
    private val plugin: JavaPlugin,
) {
    private val cacheFile = File(plugin.dataFolder, "texture-cache.yml")
    private val publishDirectory = File(plugin.dataFolder, "publish-cache").apply { mkdirs() }
    private val cache = YamlConfiguration.loadConfiguration(cacheFile)

    private val apiKey: String
        get() = plugin.config.getString("mineskin.api-key").orEmpty().trim()

    fun isConfigured(): Boolean = apiKey.isNotEmpty()

    fun publish(artwork: HeadArtwork): CompletableFuture<PublishedSkin> {
        val png = SkinImageEncoder.toPngBytes(artwork.canvas)
        val hash = sha256(png)
        findCached(hash)?.let { return CompletableFuture.completedFuture(it) }

        if (!isConfigured()) {
            return CompletableFuture.failedFuture(
                IllegalStateException("MineSkin API key is not configured in config.yml"),
            )
        }

        val file = File(publishDirectory, "$hash.png")
        file.writeBytes(png)

        val client = MineSkinClient.builder()
            .requestHandler(::Java11RequestHandler)
            .userAgent("CustomHeadPainter/${plugin.pluginMeta.version}")
            .apiKey(apiKey)
            .build()

        val visibility = runCatching {
            Visibility.valueOf(plugin.config.getString("mineskin.visibility", "UNLISTED")!!.uppercase())
        }.getOrDefault(Visibility.UNLISTED)

        val request = GenerateRequest.upload(file)
            .name(artwork.name.take(32).ifBlank { "Custom Head" })
            .visibility(visibility)

        return client.queue().submit(request)
            .thenCompose { response -> response.getJob().waitForCompletion(client) }
            .thenCompose { response -> response.getOrLoadSkin(client) }
            .thenApply { skin ->
                val texture = skin.texture()
                PublishedSkin(
                    textureUrl = texture.url().skin(),
                    textureValue = texture.data().value(),
                    textureSignature = texture.data().signature(),
                ).also { saveCached(hash, it) }
            }
            .whenComplete { _, _ ->
                if (!plugin.config.getBoolean("mineskin.keep-generated-png", false)) {
                    file.delete()
                }
            }
    }

    @Synchronized
    private fun findCached(hash: String): PublishedSkin? {
        val url = cache.getString("textures.$hash.url") ?: return null
        val value = cache.getString("textures.$hash.value") ?: return null
        return PublishedSkin(
            textureUrl = url,
            textureValue = value,
            textureSignature = cache.getString("textures.$hash.signature"),
        )
    }

    @Synchronized
    private fun saveCached(hash: String, skin: PublishedSkin) {
        cache.set("textures.$hash.url", skin.textureUrl)
        cache.set("textures.$hash.value", skin.textureValue)
        cache.set("textures.$hash.signature", skin.textureSignature)
        cache.save(cacheFile)
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
