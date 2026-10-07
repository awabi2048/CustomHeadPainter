package me.awabi2048.customheadpainter.localization

import kotlin.test.Test
import kotlin.test.assertTrue

class LocalizationCatalogTest {
    @Test
    fun `全localeでキー集合・値型・プレースホルダーが一致する`() {
        val result = LocalizationCatalog.validate()
        assertTrue(result.isValid, "カタログ検証エラー:\n" + result.errors.joinToString("\n"))
    }

    @Test
    fun `型付きキーとカタログが完全に対応する`() {
        val errors = LocalizationCatalog.validateGeneratedKeys()
        assertTrue(errors.isEmpty(), "型付きキー検証エラー:\n" + errors.joinToString("\n"))
    }
}
