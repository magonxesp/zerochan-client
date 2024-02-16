# Zerochan Client

Zerochan API client for kotlin

## Installation
Add the `kotlinx-coroutines-core` dependency from JetBrains and the zerochan client dependency to your `build.gradle.kts`

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_corroutines_version") // required
    implementation("io.github.magonxesp:zerochan-client:1.0.0")
}
```

## Usage

First you need to have a Zerochan account created, then you need to create a new instance of `ZerochanClient` class
and call a method.

### Get by tag

Get items by one tag

```kotlin
import io.github.magonxesp.zerochanclient.ZerochanClient

runBlocking {
    val client = ZerochanClient("<your app name>", System.getenv("ZEROCHAN_USERNAME"))
    val items = client.getByTag("Non non biyori") {
        // you can limit the results and
        page(1)
        limit(15)
        // sort by id
        sort(Sort.ID)
        // and more options, autocomplete with your IDE
    }

    items.forEach { println(it.source) }
}
```

### Get by tags

Get items by multiple tag

```kotlin
import io.github.magonxesp.zerochanclient.ZerochanClient

runBlocking {
    val client = ZerochanClient("<your app name>", System.getenv("ZEROCHAN_USERNAME"))
    val items = client.getByTag(arrayOf("Non non biyori", "Genshin impact")) {
        // you can limit the results and
        page(1)
        limit(15)
        // sort by id
        sort(Sort.ID)
        // and more options, autocomplete with your IDE
    }

    items.forEach { println(it.source) }
}
```

### Get by id

Get a single item by id

```kotlin
import io.github.magonxesp.zerochanclient.ZerochanClient

runBlocking {
    val client = ZerochanClient("<your app name>", System.getenv("ZEROCHAN_USERNAME"))
    val singleItem = client.getById(4114336)

    println(singleItem.large) // show the item image with large size
}
```