package me.awabi2048.customheadpainter.publish

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIo
import me.awabi2048.customheadpainter.model.HeadCanvas
import me.awabi2048.customheadpainter.model.HeadFace
import me.awabi2048.customheadpainter.model.HeadLayer

object SkinImageEncoder {
    const val IMAGE_SIZE = 64

    fun toImage(canvas: HeadCanvas): BufferedImage {
        val image = BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB)
        writeLayer(image, canvas, HeadLayer.BASE, BASE_UV)
        writeLayer(image, canvas, HeadLayer.OVERLAY, OVERLAY_UV)
        return image
    }

    fun toPngBytes(canvas: HeadCanvas): ByteArray {
        val output = ByteArrayOutputStream()
        check(ImageIO.write(toImage(canvas), "png", output)) { "No PNG ImageIO writer is available" }
        return output.toByteArray()
    }

    fun writePng(canvas: HeadCanvas, file: File) {
        file.parentFile?.mkdirs()
        check(ImageIO.write(toImage(canvas), "png", file)) { "No PNG ImageIO writer is available" }
    }

    private fun writeLayer(
        image: BufferedImage,
        canvas: HeadCanvas,
        layer: HeadLayer,
        uv: Map<HeadFace, UvOrigin>,
    ) {
        uv.forEach { (face, origin) ->
            for (y in 0..7) {
                for (x in 0..7) {
                    image.setRGB(origin.x + x, origin.y + y, canvas.get(layer, face, x, y))
                }
            }
        }
    }

    private data class UvOrigin(val x: Int, val y: Int)

    // Standard Java skin head UV layout. Canvas coordinates are stored in the same orientation as the PNG face.
    private val BASE_UV = mapOf(
        HeadFace.TOP to UvOrigin(8, 0),
        HeadFace.BOTTOM to UvOrigin(16, 0),
        HeadFace.RIGHT to UvOrigin(0, 8),
        HeadFace.FRONT to UvOrigin(8, 8),
        HeadFace.LEFT to UvOrigin(16, 8),
        HeadFace.BACK to UvOrigin(24, 8),
    )

    private val OVERLAY_UV = mapOf(
        HeadFace.TOP to UvOrigin(40, 0),
        HeadFace.BOTTOM to UvOrigin(48, 0),
        HeadFace.RIGHT to UvOrigin(32, 8),
        HeadFace.FRONT to UvOrigin(40, 8),
        HeadFace.LEFT to UvOrigin(48, 8),
        HeadFace.BACK to UvOrigin(56, 8),
    )
}
