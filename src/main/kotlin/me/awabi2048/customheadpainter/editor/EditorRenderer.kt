package me.awabi2048.customheadpainter.editor

import me.awabi2048.customheadpainter.model.HeadFace
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Interaction
import org.bukkit.entity.TextDisplay
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f

class EditorRenderer(
    private val plugin: JavaPlugin,
) {
    fun spawn(session: EditorSession) {
        despawn(session)
        val world = session.anchor.world

        val interaction = world.spawn(session.anchor, Interaction::class.java) { entity ->
            entity.setInteractionWidth(EditorHitMapper.EDITOR_SIZE.toFloat())
            entity.setInteractionHeight(EditorHitMapper.EDITOR_SIZE.toFloat())
            entity.isResponsive = true
            entity.isPersistent = false
        }
        session.interactionId = interaction.uniqueId

        HeadFace.entries.forEach { face ->
            val display = world.spawn(faceLocation(session.anchor, face), TextDisplay::class.java) { entity ->
                entity.billboard = Display.Billboard.FIXED
                entity.isPersistent = false
                entity.isShadowed = false
                entity.isSeeThrough = true
                entity.isDefaultBackground = false
                entity.backgroundColor = Color.fromARGB(0, 0, 0, 0)
                entity.alignment = TextDisplay.TextAlignment.CENTER
                entity.lineWidth = 512
                entity.viewRange = 0.25f
                entity.interpolationDuration = 0
                entity.setRotation(face.yaw, face.pitch)
                entity.setTransformation(
                    Transformation(
                        Vector3f(),
                        Quaternionf(),
                        Vector3f(TEXT_SCALE_X, TEXT_SCALE_Y, 1.0f),
                        Quaternionf(),
                    ),
                )
            }
            session.faceDisplayIds[face] = display.uniqueId
        }
        updateAll(session)
    }

    fun updateAll(session: EditorSession) {
        HeadFace.entries.forEach { updateFace(session, it) }
    }

    fun updateFace(session: EditorSession, face: HeadFace) {
        val id = session.faceDisplayIds[face] ?: return
        val display = session.anchor.world.getEntity(id) as? TextDisplay ?: return
        display.text(buildFaceComponent(session, face))
    }

    fun despawn(session: EditorSession) {
        session.interactionId?.let { session.anchor.world.getEntity(it)?.remove() }
        session.interactionId = null
        session.faceDisplayIds.values.forEach { session.anchor.world.getEntity(it)?.remove() }
        session.faceDisplayIds.clear()
    }

    private fun buildFaceComponent(session: EditorSession, face: HeadFace): Component {
        var result = Component.empty()
        for (y in 0..7) {
            for (x in 0..7) {
                val rgb = session.artwork.canvas.compositeRgb(face, x, y)
                result = result.append(Component.text(FULL_BLOCK, TextColor.color(rgb)))
            }
            if (y != 7) result = result.append(Component.newline())
        }
        return result
    }

    private fun faceLocation(anchor: Location, face: HeadFace): Location {
        val center = anchor.clone().add(0.0, HALF, 0.0)
        return when (face) {
            HeadFace.FRONT -> center.add(0.0, 0.0, -HALF - FACE_EPSILON)
            HeadFace.BACK -> center.add(0.0, 0.0, HALF + FACE_EPSILON)
            HeadFace.LEFT -> center.add(-HALF - FACE_EPSILON, 0.0, 0.0)
            HeadFace.RIGHT -> center.add(HALF + FACE_EPSILON, 0.0, 0.0)
            HeadFace.TOP -> center.add(0.0, HALF + FACE_EPSILON, 0.0)
            HeadFace.BOTTOM -> center.add(0.0, -HALF - FACE_EPSILON, 0.0)
        }
    }

    private val HeadFace.yaw: Float
        get() = when (this) {
            HeadFace.FRONT -> 180.0f
            HeadFace.BACK -> 0.0f
            HeadFace.LEFT -> 90.0f
            HeadFace.RIGHT -> -90.0f
            HeadFace.TOP, HeadFace.BOTTOM -> 180.0f
        }

    private val HeadFace.pitch: Float
        get() = when (this) {
            HeadFace.TOP -> 90.0f
            HeadFace.BOTTOM -> -90.0f
            else -> 0.0f
        }

    companion object {
        private const val HALF = EditorHitMapper.EDITOR_SIZE / 2.0
        private const val FACE_EPSILON = 0.002
        private const val FULL_BLOCK = "█"

        // Minecraft's default text display font uses different horizontal/vertical glyph metrics.
        // Keeping them separate allows exact 1/16-block texel tuning without changing the hitbox.
        private const val TEXT_SCALE_X = 0.41666666f
        private const val TEXT_SCALE_Y = 0.2777778f
    }
}
