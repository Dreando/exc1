package org.dreando.exc1

import io.restassured.RestAssured
import io.restassured.http.Header
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseRestAssuredTest {

    @LocalServerPort
    var port = 0

    @PostConstruct
    fun setup() {
        RestAssured.port = port
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }

    protected fun givenAuthenticated(base64LoginAndPassword: String): RequestSpecification {
        return RestAssured.given().header(Header("Authorization", "Basic $base64LoginAndPassword"))
    }
}