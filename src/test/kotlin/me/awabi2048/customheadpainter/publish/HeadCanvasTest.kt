package me.awabi2048.customheadpainter.publish

import kotlin.test.assertEquals
import me.awabi2048.customheadpainter.model.HeadCanvas
import org.junit.jupiter.api.Test

class HeadCanvasTest {
    @Test
    fun `opaque overlay replaces base`() {
        val base = 0xFFFF0000.toInt()
        val overlay = 0xFF0000FF.toInt()
        assertEquals(overlay, HeadCanvas.compositeArgb(base, overlay))
    }

    @Test
    fun `transparent overlay preserves base`() {
        val base = 0xFF123456.toInt()
        assertEquals(base, HeadCanvas.compositeArgb(base, 0x00000000))
    }
}
