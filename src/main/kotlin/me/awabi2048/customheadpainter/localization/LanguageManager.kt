package me.awabi2048.customheadpainter.localization

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * 埋込カタログに対するlocale解決と表示変換を担当します。
 * 言語データは不変なので、ファイル探索・解析・reloadという概念を持ちません。
 */
class LanguageManager(
    private val plugin: JavaPlugin,
) {
    private val serializer = LegacyComponentSerializer.legacyAmpersand()
    private val plainSerializer = PlainTextComponentSerializer.plainText()

    fun load() {
        val errors = LocalizationCatalog.validate().errors + LocalizationCatalog.validateGeneratedKeys()
        check(errors.isEmpty()) {
            "埋込ローカライズカタログの検証に失敗しました:\n" + errors.joinToString("\n") { "- $it" }
        }
    }

    /** プレイヤーのクライアントlocaleを優先し、未対応ならconfig.ymlのlanguage、最後にja_jpへ倒します。 */
    fun resolveLocale(player: Player?): String =
        if (player == null) resolveLocale(defaultLanguage()) else resolveLocale(player.locale().toString())

    fun resolveLocale(sender: CommandSender?): String = resolveLocale(sender as? Player)

    fun defaultLanguage(): String = resolveLocale(plugin.config.getString("language"))

    fun text(player: Player?, key: LocalizationKey<String>, placeholders: Map<String, Any> = emptyMap()): String {
        val locale = resolveLocale(player)
        val value = LocalizationCatalog.value(locale, key.id)
        require(value is LocalizedValue.Text) {
            "言語キーが見つからないか型が不正です: locale=$locale key=${key.id} expected=String"
        }
        return applyPlaceholders(value.value, placeholders)
    }

    fun list(player: Player?, key: LocalizationKey<List<String>>, placeholders: Map<String, Any> = emptyMap()): List<String> {
        val locale = resolveLocale(player)
        val value = LocalizationCatalog.value(locale, key.id)
        require(value is LocalizedValue.TextList) {
            "言語キーが見つからないか型が不正です: locale=$locale key=${key.id} expected=List"
        }
        return value.values.map { applyPlaceholders(it, placeholders) }
    }

    fun component(player: Player?, key: LocalizationKey<String>, placeholders: Map<String, Any> = emptyMap()): Component =
        normalizeComponent(serializer.deserialize(text(player, key, placeholders)))

    fun componentList(player: Player?, key: LocalizationKey<List<String>>, placeholders: Map<String, Any> = emptyMap()): List<Component> =
        list(player, key, placeholders).map { normalizeComponent(serializer.deserialize(it)) }

    /** コンソール向けに既定言語へ解決し、装飾コードを取り除いたプレーンテキストを返します。 */
    fun console(key: LocalizationKey<String>, placeholders: Map<String, Any> = emptyMap()): String =
        plainSerializer.serialize(component(null, key, placeholders))

    private fun normalizeComponent(component: Component): Component = component
        .colorIfAbsent(NamedTextColor.WHITE)
        .decoration(TextDecoration.ITALIC, false)

    private fun resolveLocale(raw: String?): String {
        val normalized = normalizeLocale(raw)
        val locales = LocalizationCatalog.locales
        val configured = normalizeLocale(plugin.config.getString("language"))
        return when {
            normalized in locales -> normalized
            configured in locales -> configured
            "ja_jp" in locales -> "ja_jp"
            else -> locales.first()
        }
    }

    private fun normalizeLocale(raw: String?): String {
        val normalized = raw?.trim()?.lowercase()?.replace('-', '_').orEmpty()
        return when (normalized) {
            "", "ja" -> "ja_jp"
            "en" -> "en_us"
            else -> normalized
        }
    }

    private fun applyPlaceholders(template: String, placeholders: Map<String, Any>): String =
        placeholders.entries.fold(template) { result, (key, value) ->
            result.replace("{$key}", value.toString()).replace("%$key%", value.toString())
        }
}
