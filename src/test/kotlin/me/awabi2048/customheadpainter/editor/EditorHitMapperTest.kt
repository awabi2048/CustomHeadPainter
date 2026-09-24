package me.awabi2048.customheadpainter.editor

import kotlin.test.assertEquals
import me.awabi2048.customheadpainter.model.HeadCell
import me.awabi2048.customheadpainter.model.HeadFace
import org.bukkit.util.Vector
import org.junit.jupiter.api.Test

class EditorHitMapperTest {
    @Test
    fun `front top-left maps to pixel zero zero`() {
        val hit = Vector(-0.249, 0.499, -0.25)
        assertEquals(HeadCell(HeadFace.FRONT, 0, 0), EditorHitMapper.map(hit))
    }

    @Test
    fun `front bottom-right maps to pixel seven seven`() {
        val hit = Vector(0.249, 0.001, -0.25)
        assertEquals(HeadCell(HeadFace.FRONT, 7, 7), EditorHitMapper.map(hit))
    }

    @Test
    fun `top center maps to top face`() {
        val hit = Vector(0.0, 0.5, 0.0)
        assertEquals(HeadFace.TOP, EditorHitMapper.map(hit).face)
    }

    @Test
    fun `right face takes precedence at right boundary`() {
        val hit = Vector(0.25, 0.25, 0.0)
        assertEquals(HeadFace.RIGHT, EditorHitMapper.map(hit).face)
    }
}
