package com.magonxesp.zerochanclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import java.net.URLEncoder

/**
 * The Zerochan API client.
 *
 * The [appName] and [username] of your zerochan account is mandatory, you will get banned from the server for being anonymous
 */
class ZerochanClient(
    private val appName: String,
    private val username: String
) {
    private val httpClient = HttpClient(CIO)
    private val jsonSerializer = Json { ignoreUnknownKeys = true }
    private val baseUrl = "https://www.zerochan.net"

    class OptionsBuilder {
        private val queryParameters = mutableMapOf<String, String>()
        var strict: Boolean = false

        /**
         * Page. Use in combination with [limit] to paginate results.
         */
        fun page(page: Int) {
            queryParameters["p"] = page.toString()
        }

        /**
         * Limit, or entries per page. Use in combination with [page] to paginate results.
         */
        fun limit(limit: Int) {
            queryParameters["l"] = limit.toString()
        }

        /**
         * Sorting order. Sort by either recency [Sort.ID] or popularity [Sort.FAV].
         */
        fun sort(sort: Sort) {
            queryParameters["s"] = sort.value
        }

        /**
         * Time used for sorting by popularity. [TimeUsed.ALL_TIME] indicates all time,
         * [TimeUsed.LAST_7000] indicates the last 7000 entries,
         * [TimeUsed.LAST_15000] indicates the last 15000 entries.
         * This parameters' behavior might be changed soon.
         */
        fun timeUsed(timeUsed: TimeUsed) {
            queryParameters["t"] = timeUsed.value.toString()
        }

        /**
         * Picture dimensions, you may use this to filter by certain picture dimensions.
         */
        fun pictureDimension(dimension: PictureDimension) {
            queryParameters["d"] = dimension.value
        }

        /**
         * Color. Filter entries by certain overall color.
         */
        fun color(color: Color) {
            queryParameters["c"] = color.value
        }

        fun build(): String {
            var query = queryParameters.entries.joinToString("&") { "${it.key}=${it.value}" }

            if (strict) {
                query = "$query&strict"
            }

            return query
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun String.deserializeItems(): ItemsResponse? =
        try {
            jsonSerializer.decodeFromString<ItemsResponse>(this)
        } catch (exception: MissingFieldException) {
            null
        }


    private fun String.andQueryString(queryString: String) =
        if (queryString.isNotBlank()) {
            "$this&$queryString"
        } else {
            this
        }

    private fun HttpRequestBuilder.addUserAgentHeader() {
        header("User-Agent", "$appName - $username")
    }

    /**
     * Get items by tag.
     * The request can take 1 second or more to respond because It is delayed for avoid the rate limit of 60 requests per minute.
     * Please consider the rate limit is you plan to execute multiple requests at the same time.
     *
     * @return a list of [Item] or an empty list if there are no results.
     *
     * @throws ZerochanClientException if the request fails.
     */
    suspend fun getByTag(tag: String, buildOptions: OptionsBuilder.() -> Unit = {}): List<Item> {
        delay(1000) // for avoid the rate limit issue (60 requests per minute)
        val options = OptionsBuilder()
        options.buildOptions()
        val queryString = options.build()
        val tagEncoded = URLEncoder.encode(tag, "utf-8")

        val response = httpClient.get("$baseUrl/$tagEncoded?json".andQueryString(queryString)) {
            addUserAgentHeader()
        }
        val responseBody = response.bodyAsText()

        if (!response.status.isSuccess()) {
            throw ZerochanClientException("An error occurred during the get by tag request: status ${response.status} with body: $responseBody")
        }

        return responseBody.deserializeItems()?.items ?: listOf()
    }

    /**
     * Get items by tags.
     * The request can take 1 second or more to respond because It is delayed for avoid the rate limit of 60 requests per minute.
     * Please consider the rate limit is you plan to execute multiple requests at the same time.
     *
     * @return a list of [Item] or an empty list if there are no results.
     *
     * @throws ZerochanClientException if the request fails.
     */
    suspend fun getByTags(tags: Array<String>, buildOptions: OptionsBuilder.() -> Unit = {}): List<Item> {
        delay(1000) // for avoid the rate limit issue (60 requests per minute)
        val options = OptionsBuilder()
        options.buildOptions()
        val queryString = options.build()
        val tagEncoded = URLEncoder.encode(tags.joinToString(","), "utf-8")

        val response = httpClient.get("$baseUrl/$tagEncoded?json".andQueryString(queryString)) {
            addUserAgentHeader()
        }
        val responseBody = response.bodyAsText()

        if (!response.status.isSuccess()) {
            throw ZerochanClientException("An error occurred during the get by tags request: status ${response.status} with body: $responseBody")
        }

        return responseBody.deserializeItems()?.items ?: listOf()
    }

    /**
     * Get single item by id.
     * The request can take 1 second or more to respond because It is delayed for avoid the rate limit of 60 requests per minute.
     * Please consider the rate limit is you plan to execute multiple requests at the same time.
     *
     * @return [SingleItem] or null if the item not exists by the provided [id].
     *
     * @throws ZerochanClientException if the request fails.
     */
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getById(id: Int): SingleItem? {
        delay(1000) // for avoid the rate limit issue (60 requests per minute)
        val response = httpClient.get("$baseUrl/$id?json") {
            addUserAgentHeader()
        }
        val responseBody = response.bodyAsText()

        if (response.status == HttpStatusCode.NotFound) {
            return null
        }

        if (!response.status.isSuccess()) {
            throw ZerochanClientException("An error occurred during the get by id request: status ${response.status} with body: $responseBody")
        }

        return try {
            jsonSerializer.decodeFromString<SingleItem>(responseBody)
        } catch (exception: MissingFieldException) {
            null
        }
    }
}