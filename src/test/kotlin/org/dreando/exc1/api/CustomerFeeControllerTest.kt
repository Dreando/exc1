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
        assertCustomer1IsCalculatedCorrectly(fees[0])
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
        assertCustomer1IsCalculatedCorrectly(
            fees.find { it.customerId == 1 } ?: throw AssertionError("Response contains no customer of id 1!")
        )

        val customer2Response =
            fees.find { it.customerId == 2 } ?: throw AssertionError("Response contains no customer of id 2!")
        assertThat(customer2Response.customerId).isEqualTo(2)
        assertThat(customer2Response.customerFirstName).isEqualTo("Konstantyn")
        assertThat(customer2Response.customerLastName).isEqualTo("Owski")
        assertThat(customer2Response.lastMonthTransactionsNumber).isEqualTo(2)
        assertThat(customer2Response.lastMonthTransactionsValue).isEqualTo(2001.0)
        assertThat(customer2Response.fee).isEqualTo(60.03)
        assertThat(customer2Response.lastTransactionDateTime).isEqualTo(LocalDateTime.of(2020, 12, 2, 13, 22, 11))
    }

    private fun assertCustomer1IsCalculatedCorrectly(customerResponse: CustomerFeeResponse) {
        assertThat(customerResponse.customerId).isEqualTo(1)
        assertThat(customerResponse.customerFirstName).isEqualTo("Andrzej")
        assertThat(customerResponse.customerLastName).isEqualTo("Andrzejowski")
        assertThat(customerResponse.lastMonthTransactionsNumber).isEqualTo(3)
        assertThat(customerResponse.lastMonthTransactionsValue).isEqualTo(1400.0)
        assertThat(customerResponse.fee).isEqualTo(56.0)
        assertThat(customerResponse.lastTransactionDateTime).isEqualTo(LocalDateTime.of(2020, 12, 13, 13, 22, 12))
    }
}