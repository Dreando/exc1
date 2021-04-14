package org.dreando.exc1.api

import org.dreando.exc1.BaseRestAssuredTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

private const val TEST_USER_CREDENTIALS = "YWRtaW46YWRtaW4="

internal class CustomerFeeControllerTest : BaseRestAssuredTest() {

    @Test
    fun testAuthenticate() {
        givenAuthenticated(TEST_USER_CREDENTIALS)
                .`when`()
                .get(CUSTOMER_FEE_ENDPOINT)
                .then()
                .statusCode(HttpStatus.OK.value())
    }
}