package me.awabi2048.customheadpainter.editor

import org.bukkit.entity.Interaction
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot

class EditorInteractionListener(
    private val manager: EditorManager,
) : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onInteract(event: PlayerInteractAtEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val interaction = event.rightClicked as? Interaction ?: return
        if (manager.applyClick(event.player, interaction, event.clickedPosition)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        manager.stop(event.player)
    }
}
