package com.example.weirdwebtestclient

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.http.client.reactive.JettyClientHttpConnector
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class WeirdWebTestClientTest {

    // suspect this is responsible for the issue, 'org.eclipse.jetty:jetty-reactive-httpclient:1.0.2'

    private val webTestClient =  WebTestClient
        .bindToServer(JettyClientHttpConnector())
        .build()

    private val webClient = WebClient
        .builder()
        .clientConnector(JettyClientHttpConnector())
        .build()

    private val jsonUri = "https://jsonplaceholder.typicode.com/todos/1"
    private val uri = "http://google.com"

    @Test
    fun `WebTestClient not working`() {

        webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectBody<String>()
            .returnResult()
            .responseBody
            .also { assertThat(it).isNotNull() }
    }

    @Test
    fun `WebTestClient not working with JSON`() {

        webTestClient
            .get()
            .uri(jsonUri)
            .exchange()
            .expectBody<String>()
            .returnResult()
            .responseBody
            .also { assertThat(it).isNotNull() }
    }

    @Test
    fun `WebClient working with JSON`() {

        webClient
            .get()
            .uri(jsonUri)
            .exchange()
            .flatMap { it.bodyToMono<String>() }
            .block()
            .also { assertThat(it).isNotNull() }
    }

    @Test
    fun `WebClient working`() {

        webClient
            .get()
            .uri(uri)
            .exchange()
            .flatMap { it.bodyToMono<String>() }
            .block()
            .also { assertThat(it).isNotNull() }
    }
}