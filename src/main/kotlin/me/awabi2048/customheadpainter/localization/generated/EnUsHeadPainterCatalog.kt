package me.awabi2048.customheadpainter.localization.generated

import me.awabi2048.customheadpainter.localization.LocalizationEntry
import me.awabi2048.customheadpainter.localization.LocalizedValue

/** コンパイル済みの不変カタログです。実行時に外部ファイルを参照しません。 */
internal object EnUsHeadPainterCatalog {
    const val LOCALE: String = "en_us"
    const val DOMAIN: String = "headpainter"

    fun entries(): List<LocalizationEntry> = listOf(
        LocalizationEntry(key = "command.player_only", value = LocalizedValue.Text("&cThis command is player-only."), domain = DOMAIN),
        LocalizationEntry(key = "command.no_permission", value = LocalizedValue.Text("&cYou do not have permission."), domain = DOMAIN),
        LocalizationEntry(key = "command.started", value = LocalizedValue.Text("&aStarted editor %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.artwork_not_found", value = LocalizedValue.Text("&cArtwork not found or not accessible."), domain = DOMAIN),
        LocalizationEntry(key = "command.opened", value = LocalizedValue.Text("&aOpened artwork %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.stopped", value = LocalizedValue.Text("&7Editor closed."), domain = DOMAIN),
        LocalizationEntry(key = "command.saved", value = LocalizedValue.Text("&aSaved %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.no_session", value = LocalizedValue.Text("&cStart or open an editor first."), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_started", value = LocalizedValue.Text("&ePublishing head texture..."), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_failed", value = LocalizedValue.Text("&cPublish failed: %reason%"), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_failed_no_api_key", value = LocalizedValue.Text("mineskin.api-key is not configured in config.yml"), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_success", value = LocalizedValue.Text("&aPublished and added the head to your inventory."), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_color", value = LocalizedValue.Text("&cUsage: /headpaint color <#RRGGBB|#AARRGGBB>"), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_layer", value = LocalizedValue.Text("&cUsage: /headpaint layer <base|overlay>"), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_tool", value = LocalizedValue.Text("&cUsage: /headpaint tool <paint|erase>"), domain = DOMAIN),
        LocalizationEntry(key = "command.color_set", value = LocalizedValue.Text("&aPaint color set to %color%"), domain = DOMAIN),
        LocalizationEntry(key = "command.layer_set", value = LocalizedValue.Text("&aLayer: %layer%"), domain = DOMAIN),
        LocalizationEntry(key = "command.tool_set", value = LocalizedValue.Text("&aTool: %tool%"), domain = DOMAIN),
        LocalizationEntry(key = "command.list_empty", value = LocalizedValue.Text("&7No saved artworks."), domain = DOMAIN),
        LocalizationEntry(key = "command.list_entry", value = LocalizedValue.Text("&b%id%  &f%name%"), domain = DOMAIN),
        LocalizationEntry(key = "command.help_title", value = LocalizedValue.Text("&6CustomHeadPainter"), domain = DOMAIN),
        LocalizationEntry(
            key = "command.help_lines",
            value = LocalizedValue.TextList(
                listOf(
                    "&7/headpaint start [name]",
                    "&7/headpaint color <#RRGGBB|#AARRGGBB>",
                    "&7/headpaint layer <base|overlay>",
                    "&7/headpaint tool <paint|erase>",
                    "&7/headpaint save | publish | stop",
                    "&7/headpaint list | open <uuid>",
                ),
            ),
            domain = DOMAIN,
        ),
        LocalizationEntry(key = "layer.base", value = LocalizedValue.Text("base"), domain = DOMAIN),
        LocalizationEntry(key = "layer.overlay", value = LocalizedValue.Text("overlay"), domain = DOMAIN),
        LocalizationEntry(key = "tool.paint", value = LocalizedValue.Text("paint"), domain = DOMAIN),
        LocalizationEntry(key = "tool.erase", value = LocalizedValue.Text("erase"), domain = DOMAIN),
        LocalizationEntry(key = "artwork.untitled", value = LocalizedValue.Text("Untitled Head"), domain = DOMAIN),
        LocalizationEntry(key = "log.enabled_with_publish", value = LocalizedValue.Text("CustomHeadPainter enabled; MineSkin publishing is configured."), domain = DOMAIN),
        LocalizationEntry(key = "log.enabled_no_api_key", value = LocalizedValue.Text("CustomHeadPainter enabled; editing/saving works, but publishing requires mineskin.api-key."), domain = DOMAIN),
        LocalizationEntry(key = "log.publish_failed", value = LocalizedValue.Text("Failed to publish artwork %id%: %reason%"), domain = DOMAIN),
    )
}
