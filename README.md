# CustomHeadPainter

Paper 26.1.2向けの、ゲーム内完結型カスタムプレイヤーヘッドエディタ。

## 現在のMVP

- 実際のプレイヤーヘッドと同じ `0.5 × 0.5 × 0.5` ブロックの編集領域
- 6面それぞれを8×8 texelとして直接クリック編集
- Base / Overlayレイヤー
- 24-bit RGB + alphaの色指定
- YAMLによる作品保存・再編集
- 64×64 Java skin PNGへの変換
- SHA-256によるPublish結果キャッシュ
- MineSkin V2 queue経由のテクスチャ生成
- Paper `PROFILE` data componentを用いたPLAYER_HEAD出力

編集はすべてサーバー内で処理し、MineSkin APIは `/headpaint publish` のときだけ使用する。

## コマンド

```text
/headpaint start [name]
/headpaint color <#RRGGBB|#AARRGGBB>
/headpaint layer <base|overlay>
/headpaint tool <paint|erase>
/headpaint save
/headpaint publish
/headpaint list
/headpaint open <uuid>
/headpaint stop
```

## Build

- Java 25
- Kotlin 2.3.20
- Maven
- Paper API 26.1.2

```bash
mvn clean verify
```

## MineSkin

`plugins/CustomHeadPainter/config.yml` の `mineskin.api-key` にV2 API keyを設定する。API keyがない場合も編集・保存は利用できる。
