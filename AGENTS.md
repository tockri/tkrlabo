# tkrlabo 

@tockri がSpring Bootの実験をするためのプロジェクト。

## コミュニケーションルール
- 返答は短く最低限に。
- 作業完了したら「完了。」とだけ出力する。今の作業に対するコメントはいらない。

## Javaコーディングスタイル

### 変数宣言は`var` で宣言する

#### 好まない
```java
String message = "hello";
```

#### 好む
```java
var message = "hello";
```

## JUnitテストコーディングスタイル

### @DisplayName必須
日本語で「〜〜〜の場合、〜〜〜する/になる」のように、条件と結果を明示する

### シンプルなテストメソッド
一つのテストで一つのことだけを確かめる

### AAAパターン
以下のようにArrange,Act,Assertのコメントを挟む
```java
// Arrange
準備

// Act
メソッド実行

// Assert
検証
```

### @Nestedを使用する
テスト対象メソッドごとに@Nestedで内部クラスを作る
例：テスト対象がMyClass.myMethod()の場合

```java
class MyClassTest {
    @Nested
    class MyMethodTest {

    }
}
```