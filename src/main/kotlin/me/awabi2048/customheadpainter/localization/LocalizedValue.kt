package me.awabi2048.customheadpainter.localization

/**
 * ローカライズ値の取り得る型です。
 *
 * YAMLのような汎用データ型を持ち込まず、利用側が返せる2種類だけを明示します。
 */
sealed interface LocalizedValue {
    data class Text(val value: String) : LocalizedValue

    data class TextList(val values: List<String>) : LocalizedValue
}

/** カタログ内の1項目と、その文言を所有する機能領域を表します。 */
data class LocalizationEntry(
    val key: String,
    val value: LocalizedValue,
    val domain: String,
)
