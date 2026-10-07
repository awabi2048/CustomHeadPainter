package me.awabi2048.customheadpainter.localization.generated

import me.awabi2048.customheadpainter.localization.LocalizationKey

/** コンパイル時に値型を保証する、領域別のローカライズキーです。 */
object HeadPainterKeys {
    @JvmField val COMMAND_PLAYER_ONLY: LocalizationKey<String> = LocalizationKey.text("command.player_only", setOf())
    @JvmField val COMMAND_NO_PERMISSION: LocalizationKey<String> = LocalizationKey.text("command.no_permission", setOf())
    @JvmField val COMMAND_STARTED: LocalizationKey<String> = LocalizationKey.text("command.started", setOf("id"))
    @JvmField val COMMAND_ARTWORK_NOT_FOUND: LocalizationKey<String> = LocalizationKey.text("command.artwork_not_found", setOf())
    @JvmField val COMMAND_OPENED: LocalizationKey<String> = LocalizationKey.text("command.opened", setOf("id"))
    @JvmField val COMMAND_STOPPED: LocalizationKey<String> = LocalizationKey.text("command.stopped", setOf())
    @JvmField val COMMAND_SAVED: LocalizationKey<String> = LocalizationKey.text("command.saved", setOf("id"))
    @JvmField val COMMAND_NO_SESSION: LocalizationKey<String> = LocalizationKey.text("command.no_session", setOf())
    @JvmField val COMMAND_PUBLISH_STARTED: LocalizationKey<String> = LocalizationKey.text("command.publish_started", setOf())
    @JvmField val COMMAND_PUBLISH_FAILED: LocalizationKey<String> = LocalizationKey.text("command.publish_failed", setOf("reason"))
    @JvmField val COMMAND_PUBLISH_FAILED_NO_API_KEY: LocalizationKey<String> = LocalizationKey.text("command.publish_failed_no_api_key", setOf())
    @JvmField val COMMAND_PUBLISH_SUCCESS: LocalizationKey<String> = LocalizationKey.text("command.publish_success", setOf())
    @JvmField val COMMAND_USAGE_COLOR: LocalizationKey<String> = LocalizationKey.text("command.usage_color", setOf())
    @JvmField val COMMAND_USAGE_LAYER: LocalizationKey<String> = LocalizationKey.text("command.usage_layer", setOf())
    @JvmField val COMMAND_USAGE_TOOL: LocalizationKey<String> = LocalizationKey.text("command.usage_tool", setOf())
    @JvmField val COMMAND_COLOR_SET: LocalizationKey<String> = LocalizationKey.text("command.color_set", setOf("color"))
    @JvmField val COMMAND_LAYER_SET: LocalizationKey<String> = LocalizationKey.text("command.layer_set", setOf("layer"))
    @JvmField val COMMAND_TOOL_SET: LocalizationKey<String> = LocalizationKey.text("command.tool_set", setOf("tool"))
    @JvmField val COMMAND_LIST_EMPTY: LocalizationKey<String> = LocalizationKey.text("command.list_empty", setOf())
    @JvmField val COMMAND_LIST_ENTRY: LocalizationKey<String> = LocalizationKey.text("command.list_entry", setOf("id", "name"))
    @JvmField val COMMAND_HELP_TITLE: LocalizationKey<String> = LocalizationKey.text("command.help_title", setOf())
    @JvmField val COMMAND_HELP_LINES: LocalizationKey<List<String>> = LocalizationKey.textList("command.help_lines", setOf())

    @JvmField val LAYER_BASE: LocalizationKey<String> = LocalizationKey.text("layer.base", setOf())
    @JvmField val LAYER_OVERLAY: LocalizationKey<String> = LocalizationKey.text("layer.overlay", setOf())

    @JvmField val TOOL_PAINT: LocalizationKey<String> = LocalizationKey.text("tool.paint", setOf())
    @JvmField val TOOL_ERASE: LocalizationKey<String> = LocalizationKey.text("tool.erase", setOf())

    @JvmField val ARTWORK_UNTITLED: LocalizationKey<String> = LocalizationKey.text("artwork.untitled", setOf())

    @JvmField val LOG_ENABLED_WITH_PUBLISH: LocalizationKey<String> = LocalizationKey.text("log.enabled_with_publish", setOf())
    @JvmField val LOG_ENABLED_NO_API_KEY: LocalizationKey<String> = LocalizationKey.text("log.enabled_no_api_key", setOf())
    @JvmField val LOG_PUBLISH_FAILED: LocalizationKey<String> = LocalizationKey.text("log.publish_failed", setOf("id", "reason"))

    @JvmStatic fun all(): List<LocalizationKey<*>> = listOf(
        COMMAND_PLAYER_ONLY,
        COMMAND_NO_PERMISSION,
        COMMAND_STARTED,
        COMMAND_ARTWORK_NOT_FOUND,
        COMMAND_OPENED,
        COMMAND_STOPPED,
        COMMAND_SAVED,
        COMMAND_NO_SESSION,
        COMMAND_PUBLISH_STARTED,
        COMMAND_PUBLISH_FAILED,
        COMMAND_PUBLISH_FAILED_NO_API_KEY,
        COMMAND_PUBLISH_SUCCESS,
        COMMAND_USAGE_COLOR,
        COMMAND_USAGE_LAYER,
        COMMAND_USAGE_TOOL,
        COMMAND_COLOR_SET,
        COMMAND_LAYER_SET,
        COMMAND_TOOL_SET,
        COMMAND_LIST_EMPTY,
        COMMAND_LIST_ENTRY,
        COMMAND_HELP_TITLE,
        COMMAND_HELP_LINES,
        LAYER_BASE,
        LAYER_OVERLAY,
        TOOL_PAINT,
        TOOL_ERASE,
        ARTWORK_UNTITLED,
        LOG_ENABLED_WITH_PUBLISH,
        LOG_ENABLED_NO_API_KEY,
        LOG_PUBLISH_FAILED,
    )
}
