package me.awabi2048.customheadpainter.localization

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * 型付きキーだけを受け取るローカライズの唯一の入口です。
 * 任意の文字列キーを実行時に組み立てる経路を設けません。
 */
object PainterI18n {
    private lateinit var languageManager: LanguageManager

    fun init(plugin: JavaPlugin) {
        languageManager = LanguageManager(plugin)
        languageManager.load()
    }

    fun text(player: Player?, key: LocalizationKey<String>, vararg placeholders: Pair<String, Any?>): String =
        languageManager.text(player, key, placeholdersMap(*placeholders))

    fun list(player: Player?, key: LocalizationKey<List<String>>, vararg placeholders: Pair<String, Any?>): List<String> =
        languageManager.list(player, key, placeholdersMap(*placeholders))

    fun component(player: Player?, key: LocalizationKey<String>, vararg placeholders: Pair<String, Any?>): Component =
        languageManager.component(player, key, placeholdersMap(*placeholders))

    fun componentList(player: Player?, key: LocalizationKey<List<String>>, vararg placeholders: Pair<String, Any?>): List<Component> =
        languageManager.componentList(player, key, placeholdersMap(*placeholders))

    fun console(key: LocalizationKey<String>, vararg placeholders: Pair<String, Any?>): String =
        languageManager.console(key, placeholdersMap(*placeholders))

    private fun placeholdersMap(vararg pairs: Pair<String, Any?>): Map<String, Any> =
        pairs.filter { it.second != null }.associate { it.first to it.second!! }
}
