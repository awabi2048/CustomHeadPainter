package me.awabi2048.customheadpainter.editor

import me.awabi2048.customheadpainter.model.HeadCell
import me.awabi2048.customheadpainter.model.HeadFace
import org.bukkit.util.Vector
import kotlin.math.abs
import kotlin.math.floor

object EditorHitMapper {
    const val EDITOR_SIZE = 0.5
    private const val HALF = EDITOR_SIZE / 2.0

    /**
     * Maps PlayerInteractAtEntityEvent#getClickedPosition to a head texel.
     *
     * Interaction entities are centered on X/Z and start at Y=0. The editor therefore occupies
     * x,z in [-0.25, 0.25] and y in [0.0, 0.5].
     */
    fun map(clicked: Vector): HeadCell {
        val candidates = listOf(
            FaceDistance(HeadFace.LEFT, abs(clicked.x + HALF)),
            FaceDistance(HeadFace.RIGHT, abs(HALF - clicked.x)),
            FaceDistance(HeadFace.BOTTOM, abs(clicked.y)),
            FaceDistance(HeadFace.TOP, abs(EDITOR_SIZE - clicked.y)),
            FaceDistance(HeadFace.FRONT, abs(clicked.z + HALF)),
            FaceDistance(HeadFace.BACK, abs(HALF - clicked.z)),
        )
        val face = candidates.minBy { it.distance }.face

        val nx = normalizeCentered(clicked.x)
        val ny = normalizeVertical(clicked.y)
        val nz = normalizeCentered(clicked.z)

        val (u, v) = when (face) {
            HeadFace.FRONT -> nx to (1.0 - ny)
            HeadFace.BACK -> (1.0 - nx) to (1.0 - ny)
            HeadFace.LEFT -> (1.0 - nz) to (1.0 - ny)
            HeadFace.RIGHT -> nz to (1.0 - ny)
            HeadFace.TOP -> nx to nz
            HeadFace.BOTTOM -> nx to (1.0 - nz)
        }

        return HeadCell(face, texel(u), texel(v))
    }

    private fun normalizeCentered(value: Double): Double = ((value + HALF) / EDITOR_SIZE).coerceIn(0.0, 1.0)

    private fun normalizeVertical(value: Double): Double = (value / EDITOR_SIZE).coerceIn(0.0, 1.0)

    private fun texel(value: Double): Int = floor(value * 8.0).toInt().coerceIn(0, 7)

    private data class FaceDistance(val face: HeadFace, val distance: Double)
}
