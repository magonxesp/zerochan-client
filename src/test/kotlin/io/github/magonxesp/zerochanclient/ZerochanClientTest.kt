package io.github.magonxesp.zerochanclient

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.random.Random

class ZerochanClientTest : AnnotationSpec() {
    private val client = ZerochanClient("Zerochan api client test", System.getenv("ZEROCHAN_USERNAME"))

    @Test
    fun `it should search by tag`() = runTest {
        val items = client.getByTag("Non non biyori")

        items.isNotEmpty() shouldBe true
    }

    @Test
    fun `it should search by multiple tags`() = runTest {
        val items = client.getByTags(arrayOf("Non non biyori", "Overwacth"))

        items.isNotEmpty() shouldBe true
    }

    @Test
    fun `it should not search by multiple tags if it don't find results`() = runTest {
        val items = client.getByTags(arrayOf("Non non biyori", "Renge"))

        items.isNotEmpty() shouldBe false
    }

    @Test
    fun `it should get single item by id`() = runTest {
        val singleItem = client.getById(4064182)

        singleItem?.id shouldBe 4064182
    }

    @Test
    fun `it should not get single item by wrong id`() = runTest {
        val singleItem = client.getById(Random.nextInt())

        singleItem shouldBe null
    }
}