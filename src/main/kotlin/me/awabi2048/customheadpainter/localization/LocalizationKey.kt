package me.awabi2048.customheadpainter.localization

/**
 * 取得結果の型をコンパイル時に固定するローカライズキーです。
 * コンストラクタを閉じ、定義側だけが使用するfactoryから値型を明示します。
 * CC-Systemの生成済みカタログ形式に倣い、プラグイン内で完結する実装です。
 */
class LocalizationKey<T> private constructor(
    val id: String,
    val valueType: ValueType,
    val placeholders: Set<String>,
) {
    enum class ValueType { TEXT, TEXT_LIST }

    companion object {
        internal fun text(id: String, placeholders: Set<String> = emptySet()): LocalizationKey<String> =
            LocalizationKey(id, ValueType.TEXT, placeholders)

        internal fun textList(id: String, placeholders: Set<String> = emptySet()): LocalizationKey<List<String>> =
            LocalizationKey(id, ValueType.TEXT_LIST, placeholders)
    }
}
