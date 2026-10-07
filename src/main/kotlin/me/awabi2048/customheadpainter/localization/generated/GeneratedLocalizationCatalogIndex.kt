package me.awabi2048.customheadpainter.localization.generated

import me.awabi2048.customheadpainter.localization.LocalizationEntry

/** 全領域のカタログをlocale単位で合成する唯一の索引です。 */
internal object GeneratedLocalizationCatalogIndex {
    fun entriesByLocale(): Map<String, List<LocalizationEntry>> = mapOf(
        "en_us" to buildList {
            addAll(EnUsHeadPainterCatalog.entries())
        },
        "ja_jp" to buildList {
            addAll(JaJpHeadPainterCatalog.entries())
        },
    )
}
