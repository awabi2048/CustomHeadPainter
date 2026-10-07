package me.awabi2048.customheadpainter.model

import java.time.Instant
import java.util.UUID
import kotlin.math.roundToInt

enum class HeadFace {
    FRONT,
    BACK,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}

enum class HeadLayer {
    BASE,
    OVERLAY,
}

enum class PaintTool {
    PAINT,
    ERASE,
}

data class HeadCell(
    val face: HeadFace,
    val x: Int,
    val y: Int,
) {
    init {
        require(x in 0..7) { "x must be in 0..7" }
        require(y in 0..7) { "y must be in 0..7" }
    }
}

class HeadCanvas private constructor(
    private val pixels: IntArray,
) {
    constructor(baseArgb: Int = DEFAULT_BASE_ARGB) : this(
        IntArray(TOTAL_PIXEL_COUNT) { index ->
            if (index < LAYER_PIXEL_COUNT) baseArgb else TRANSPARENT_ARGB
        },
    )

    fun copy(): HeadCanvas = HeadCanvas(pixels.copyOf())

    fun get(layer: HeadLayer, face: HeadFace, x: Int, y: Int): Int = pixels[index(layer, face, x, y)]

    fun set(layer: HeadLayer, face: HeadFace, x: Int, y: Int, argb: Int) {
        pixels[index(layer, face, x, y)] = argb
    }

    fun set(layer: HeadLayer, cell: HeadCell, argb: Int) {
        set(layer, cell.face, cell.x, cell.y, argb)
    }

    fun facePixels(layer: HeadLayer, face: HeadFace): List<Int> = buildList(PIXELS_PER_FACE) {
        for (y in 0..7) {
            for (x in 0..7) {
                add(get(layer, face, x, y))
            }
        }
    }

    fun replaceFace(layer: HeadLayer, face: HeadFace, values: List<Int>) {
        require(values.size == PIXELS_PER_FACE) { "Expected $PIXELS_PER_FACE pixels, got ${values.size}" }
        values.forEachIndexed { index, argb ->
            val x = index % 8
            val y = index / 8
            set(layer, face, x, y, argb)
        }
    }

    fun compositeRgb(face: HeadFace, x: Int, y: Int): Int {
        val base = get(HeadLayer.BASE, face, x, y)
        val overlay = get(HeadLayer.OVERLAY, face, x, y)
        return compositeArgb(base, overlay) and 0x00FFFFFF
    }

    fun rawPixels(): IntArray = pixels.copyOf()

    companion object {
        const val PIXELS_PER_FACE = 64
        const val FACE_COUNT = 6
        const val LAYER_PIXEL_COUNT = PIXELS_PER_FACE * FACE_COUNT
        const val TOTAL_PIXEL_COUNT = LAYER_PIXEL_COUNT * 2
        const val DEFAULT_BASE_ARGB: Int = -1 // 0xFFFFFFFF
        const val TRANSPARENT_ARGB: Int = 0x00000000

        fun fromRawPixels(values: IntArray): HeadCanvas {
            require(values.size == TOTAL_PIXEL_COUNT) { "Expected $TOTAL_PIXEL_COUNT pixels, got ${values.size}" }
            return HeadCanvas(values.copyOf())
        }

        fun compositeArgb(base: Int, overlay: Int): Int {
            val ba = (base ushr 24) and 0xFF
            val br = (base ushr 16) and 0xFF
            val bg = (base ushr 8) and 0xFF
            val bb = base and 0xFF

            val oa = (overlay ushr 24) and 0xFF
            if (oa == 0) return base
            if (oa == 255) return overlay

            val baseAlpha = ba / 255.0
            val overlayAlpha = oa / 255.0
            val outAlpha = overlayAlpha + baseAlpha * (1.0 - overlayAlpha)
            if (outAlpha <= 0.0) return 0

            fun channel(baseChannel: Int, overlayChannel: Int): Int {
                val value = (
                    overlayChannel * overlayAlpha +
                        baseChannel * baseAlpha * (1.0 - overlayAlpha)
                    ) / outAlpha
                return value.roundToInt().coerceIn(0, 255)
            }

            val a = (outAlpha * 255.0).roundToInt().coerceIn(0, 255)
            val r = channel(br, (overlay ushr 16) and 0xFF)
            val g = channel(bg, (overlay ushr 8) and 0xFF)
            val b = channel(bb, overlay and 0xFF)
            return (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        private fun index(layer: HeadLayer, face: HeadFace, x: Int, y: Int): Int {
            require(x in 0..7) { "x must be in 0..7" }
            require(y in 0..7) { "y must be in 0..7" }
            val layerOffset = layer.ordinal * LAYER_PIXEL_COUNT
            val faceOffset = face.ordinal * PIXELS_PER_FACE
            return layerOffset + faceOffset + y * 8 + x
        }
    }
}

data class HeadArtwork(
    val id: UUID = UUID.randomUUID(),
    val owner: UUID,
    var name: String,
    val canvas: HeadCanvas = HeadCanvas(),
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now(),
    var publishedTextureUrl: String? = null,
) {
    fun touch() {
        updatedAt = Instant.now()
    }
}
