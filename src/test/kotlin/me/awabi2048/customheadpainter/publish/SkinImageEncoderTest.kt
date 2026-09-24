package me.awabi2048.customheadpainter.publish

import kotlin.test.assertEquals
import me.awabi2048.customheadpainter.model.HeadCanvas
import me.awabi2048.customheadpainter.model.HeadFace
import me.awabi2048.customheadpainter.model.HeadLayer
import org.junit.jupiter.api.Test

class SkinImageEncoderTest {
    @Test
    fun `front base starts at 8 8`() {
        val canvas = HeadCanvas()
        val color = 0xFF123456.toInt()
        canvas.set(HeadLayer.BASE, HeadFace.FRONT, 0, 0, color)
        val image = SkinImageEncoder.toImage(canvas)
        assertEquals(color, image.getRGB(8, 8))
    }

    @Test
    fun `front overlay starts at 40 8`() {
        val canvas = HeadCanvas()
        val color = 0x80123456.toInt()
        canvas.set(HeadLayer.OVERLAY, HeadFace.FRONT, 0, 0, color)
        val image = SkinImageEncoder.toImage(canvas)
        assertEquals(color, image.getRGB(40, 8))
    }

    @Test
    fun `all generated skins are 64 by 64`() {
        val image = SkinImageEncoder.toImage(HeadCanvas())
        assertEquals(64, image.width)
        assertEquals(64, image.height)
    }
}
