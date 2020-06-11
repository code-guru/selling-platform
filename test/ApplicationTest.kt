package lvls

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import java.util.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun `Create a New User`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """{
                        "firstName": "Tuva", 
                        "lastName": "Lindberg", 
                        "email": "${UUID.randomUUID().toString()}@gmail.com"
                    }"""
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                val content = response.content
                assertNotNull(content)
                assertTrue(content.contains("\"success\" : \"true\""))
            }
        }
    }


    @Test
    fun `Edit a User by Id`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/users/1") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """{
                        "firstName": "Tuva", 
                        "lastName": "Tirgar", 
                        "email": "${UUID.randomUUID().toString()}@gmail.com"
                    }"""
                )
                `Create a New User`()
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val content = response.content
                assertNotNull(content)
                assertTrue(content.contains("\"success\" : \"true\""))
            }
        }
    }

    @Test
    fun `Create a New Ad`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/ads") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """{
                        "title": "brun lampa",
                        "description": "det är perfekt",
                        "category": "kategori1",
                        "creationDate": "2019-05-20",
                        "ownerId": 1
                    }"""
                )
                `Create a New User`()
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                val content = response.content
                assertNotNull(content)
                assertTrue(content.contains("\"success\" : \"true\""))
            }
        }
    }

    @Test
    fun `Edit an Ad by Id`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/ads/1") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """{
                        "title": "brun lampa",
                        "description": "det är perfekt",
                        "category": "kategori1",
                        "creationDate": "2019-05-20",
                        "ownerId": 1
                    }"""
                )
                `Create a New Ad`()
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val content = response.content
                assertNotNull(content)
                assertTrue(content.contains("\"success\" : \"true\""))
            }
        }
    }
}
