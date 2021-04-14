package org.dreando.exc1.api

import org.assertj.core.api.Assertions.assertThat
import org.dreando.exc1.BaseRestAssuredTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

private const val TEST_USER_CREDENTIALS = "YWRtaW46YWRtaW4="

internal class CustomerFeeControllerTest : BaseRestAssuredTest() {

    /**
     * In general I am not totally sure whether I understand the business requirement correctly. Assuming we have total
     * value of transactions equal to 17 and fee wages as follows:
     *
     * 1000 - 5%
     * 2000 - 4%
     * 3000 - 3%
     *
     * There are two ways of calculating the fee.
     *
     * 1. We can check in which range is the total and calculate the fee using the percentage value of given range, so
     * 1700 is in the range between 1000 and 2000, meaning the 4% fee would be applied, so the result is 68
     * or
     * 2. We should charge the amount in each range separately, like PIT tax: in case of 1700, 1000 would be applicable
     * for fee of 5% => 50, and then remaining 700 for fee 4% => 28, so the result is 78
     *
     * I had some intuition and decided to go with option 1
     */
    @Test
    fun `test calculate fee for single user of id 1`() {
        val fees = givenAuthenticated(TEST_USER_CREDENTIALS)
            .`when`()
            .queryParam(CUSTOMER_ID_QUERY_PARAM, 1)
            .get(CUSTOMER_FEE_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body().`as`(Array<CustomerFeeResponse>::class.java)

        assertThat(fees).hasSize(1)
        val client1Response = fees[0]
        assertThat(client1Response.customerId).isEqualTo(1)
        assertThat(client1Response.customerFirstName).isEqualTo("Andrzej")
        assertThat(client1Response.customerLastName).isEqualTo("Andrzejowski")
        assertThat(client1Response.lastMonthTransactionsNumber).isEqualTo(3)
        assertThat(client1Response.lastMonthTransactionsValue).isEqualTo(1400)
        assertThat(client1Response.fee).isEqualTo(56)
        assertThat(client1Response.lastTransactionDateTime).isEqualTo(LocalDateTime.of(2020, 12, 13, 13, 22, 12))
    }

    @Test
    fun `test calculate fee for both users 1 and 2 listed in param`() {
        val fees = givenAuthenticated(TEST_USER_CREDENTIALS)
            .`when`()
            .queryParam(CUSTOMER_ID_QUERY_PARAM, 1, 2)
            .get(CUSTOMER_FEE_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body().`as`(Array<CustomerFeeResponse>::class.java)

        assertThat(fees).hasSize(2)
        assertBothCustomersAreCalculatedCorrectly(fees)
    }

    @Test
    fun `test calculate fee for ALL users stated in param`() {
        val fees = givenAuthenticated(TEST_USER_CREDENTIALS)
            .`when`()
            .queryParam(CUSTOMER_ID_QUERY_PARAM, "ALL")
            .get(CUSTOMER_FEE_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body().`as`(Array<CustomerFeeResponse>::class.java)

        assertThat(fees).hasSize(2)
        assertBothCustomersAreCalculatedCorrectly(fees)
    }

    @Test
    fun `test calculate fee for with no param provided, resulting in ALL`() {
        val fees = givenAuthenticated(TEST_USER_CREDENTIALS)
            .`when`()
            .get(CUSTOMER_FEE_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body().`as`(Array<CustomerFeeResponse>::class.java)

        assertThat(fees).hasSize(2)
        assertBothCustomersAreCalculatedCorrectly(fees)
    }

    private fun assertBothCustomersAreCalculatedCorrectly(fees: Array<CustomerFeeResponse>) {
        val client1Response =
            fees.find { it.customerId == 1 } ?: throw AssertionError("Response contains no customer of id 1!")
        assertThat(client1Response.customerId).isEqualTo(1)
        assertThat(client1Response.customerFirstName).isEqualTo("Andrzej")
        assertThat(client1Response.customerLastName).isEqualTo("Andrzejowski")
        assertThat(client1Response.lastMonthTransactionsNumber).isEqualTo(3)
        assertThat(client1Response.lastMonthTransactionsValue).isEqualTo(1400)
        assertThat(client1Response.fee).isEqualTo(56)
        assertThat(client1Response.lastTransactionDateTime).isEqualTo(LocalDateTime.of(2020, 12, 13, 13, 22, 12))

        val client2Response =
            fees.find { it.customerId == 2 } ?: throw AssertionError("Response contains no customer of id 2!")
        assertThat(client2Response.customerId).isEqualTo(2)
        assertThat(client2Response.customerFirstName).isEqualTo("Konstantyn")
        assertThat(client2Response.customerLastName).isEqualTo("Owski")
        assertThat(client2Response.lastMonthTransactionsNumber).isEqualTo(2)
        assertThat(client2Response.lastMonthTransactionsValue).isEqualTo(2001)
        assertThat(client2Response.fee).isEqualTo(60.01)
        assertThat(client2Response.lastTransactionDateTime).isEqualTo(LocalDateTime.of(2020, 12, 2, 13, 22, 11))
    }
}