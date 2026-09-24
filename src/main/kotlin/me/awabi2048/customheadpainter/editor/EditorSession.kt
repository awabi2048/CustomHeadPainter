package me.awabi2048.customheadpainter.editor

import java.util.UUID
import me.awabi2048.customheadpainter.model.HeadArtwork
import me.awabi2048.customheadpainter.model.HeadFace
import me.awabi2048.customheadpainter.model.HeadLayer
import me.awabi2048.customheadpainter.model.PaintTool
import org.bukkit.Location

class EditorSession(
    val playerId: UUID,
    val artwork: HeadArtwork,
    val anchor: Location,
) {
    var selectedArgb: Int = -1
    var selectedLayer: HeadLayer = HeadLayer.BASE
    var tool: PaintTool = PaintTool.PAINT
    var interactionId: UUID? = null
    val faceDisplayIds: MutableMap<HeadFace, UUID> = linkedMapOf()
}
