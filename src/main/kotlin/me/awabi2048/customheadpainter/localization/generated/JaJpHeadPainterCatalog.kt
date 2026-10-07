package me.awabi2048.customheadpainter.localization.generated

import me.awabi2048.customheadpainter.localization.LocalizationEntry
import me.awabi2048.customheadpainter.localization.LocalizedValue

/** コンパイル済みの不変カタログです。実行時に外部ファイルを参照しません。 */
internal object JaJpHeadPainterCatalog {
    const val LOCALE: String = "ja_jp"
    const val DOMAIN: String = "headpainter"

    fun entries(): List<LocalizationEntry> = listOf(
        LocalizationEntry(key = "command.player_only", value = LocalizedValue.Text("&cこのコマンドはプレイヤーのみ実行できます。"), domain = DOMAIN),
        LocalizationEntry(key = "command.no_permission", value = LocalizedValue.Text("&c権限がありません。"), domain = DOMAIN),
        LocalizationEntry(key = "command.started", value = LocalizedValue.Text("&aエディタを開始しました: %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.artwork_not_found", value = LocalizedValue.Text("&c作品が見つからないか、アクセス権がありません。"), domain = DOMAIN),
        LocalizationEntry(key = "command.opened", value = LocalizedValue.Text("&a作品を開きました: %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.stopped", value = LocalizedValue.Text("&7エディタを閉じました。"), domain = DOMAIN),
        LocalizationEntry(key = "command.saved", value = LocalizedValue.Text("&a保存しました: %id%"), domain = DOMAIN),
        LocalizationEntry(key = "command.no_session", value = LocalizedValue.Text("&c先にエディタを開始または作品を開いてください。"), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_started", value = LocalizedValue.Text("&eヘッドテクスチャを生成しています..."), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_failed", value = LocalizedValue.Text("&c生成に失敗しました: %reason%"), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_failed_no_api_key", value = LocalizedValue.Text("config.yml に mineskin.api-key が設定されていません"), domain = DOMAIN),
        LocalizationEntry(key = "command.publish_success", value = LocalizedValue.Text("&a生成が完了し、ヘッドをインベントリへ追加しました。"), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_color", value = LocalizedValue.Text("&c使用法: /headpaint color <#RRGGBB|#AARRGGBB>"), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_layer", value = LocalizedValue.Text("&c使用法: /headpaint layer <base|overlay>"), domain = DOMAIN),
        LocalizationEntry(key = "command.usage_tool", value = LocalizedValue.Text("&c使用法: /headpaint tool <paint|erase>"), domain = DOMAIN),
        LocalizationEntry(key = "command.color_set", value = LocalizedValue.Text("&a描画色を %color% に設定しました。"), domain = DOMAIN),
        LocalizationEntry(key = "command.layer_set", value = LocalizedValue.Text("&aレイヤー: %layer%"), domain = DOMAIN),
        LocalizationEntry(key = "command.tool_set", value = LocalizedValue.Text("&aツール: %tool%"), domain = DOMAIN),
        LocalizationEntry(key = "command.list_empty", value = LocalizedValue.Text("&7保存済みの作品はありません。"), domain = DOMAIN),
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
        LocalizationEntry(key = "layer.base", value = LocalizedValue.Text("ベース"), domain = DOMAIN),
        LocalizationEntry(key = "layer.overlay", value = LocalizedValue.Text("オーバーレイ"), domain = DOMAIN),
        LocalizationEntry(key = "tool.paint", value = LocalizedValue.Text("ペイント"), domain = DOMAIN),
        LocalizationEntry(key = "tool.erase", value = LocalizedValue.Text("消しゴム"), domain = DOMAIN),
        LocalizationEntry(key = "artwork.untitled", value = LocalizedValue.Text("無題のヘッド"), domain = DOMAIN),
        LocalizationEntry(key = "log.enabled_with_publish", value = LocalizedValue.Text("CustomHeadPainter が有効になりました。MineSkin への公開機能は利用可能です。"), domain = DOMAIN),
        LocalizationEntry(key = "log.enabled_no_api_key", value = LocalizedValue.Text("CustomHeadPainter が有効になりました。編集・保存は利用できますが、公開には mineskin.api-key が必要です。"), domain = DOMAIN),
        LocalizationEntry(key = "log.publish_failed", value = LocalizedValue.Text("作品 %id% の生成に失敗しました: %reason%"), domain = DOMAIN),
    )
}
