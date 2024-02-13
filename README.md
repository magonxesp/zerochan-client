# Zerochan Client

Zerochan API client for kotlin

## Installation
Add on your `build.gradle.kts` the Jitpack repository

```kotlin
repositories {
    maven { setUrl("https://jitpack.io") }
}
```

And now you should add the `kotlinx-coroutines-core` dependency from JetBrains and the zerochan client dependency

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_corroutines_version") // required
    implementation("<TODO add dependency notation>")
}
```

## Usage

First you need to have a Zerochan account created, then you need to create a new instance of `ZerochanClient` class
and call a method.

### Get by tag

Get items by one tag

```kotlin
import com.magonxesp.zerochanclient.ZerochanClient

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
import com.magonxesp.zerochanclient.ZerochanClient

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
import com.magonxesp.zerochanclient.ZerochanClient

runBlocking {
    val client = ZerochanClient("<your app name>", System.getenv("ZEROCHAN_USERNAME"))
    val singleItem = client.getById(4114336)

    println(singleItem.large) // show the item image with large size
}
```