package com.ada.portfolioRebalancer.fpsHandler

import com.ada.portfolioRebalancer.strategyMatcher.Customer
import com.ada.portfolioRebalancer.strategyMatcher.Strategy
import com.ada.portfolioRebalancer.strategyMatcher.defaultStrategy
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FpsHandlerTest {
    private val url = "http://localhost:8000/"
    private val postEndpoint = "execute"
    private val getEndpoint = "customer"
    private var restTemplate = mockk<RestTemplate>()
    private val customers = listOf(
        createCustomer(
            strategy = createStrategy(
                stocksPercentage = 0.2,
                bondsPercentage = 0.4,
                cashPercentage = 0.4
            )
        )
    )

    @Test
    fun `customer trades are calculated`() {
        every {
            restTemplate.getForObject(
                any<String>(),
                CustomerPortfolio::class.java
            )
        } returns createCustomerPortfolio(cash = 1000, stocks = 1000, bonds = 1000)

        val fpsHandler = FpsHandler(
            url, postEndpoint, getEndpoint, customers, restTemplate
        )
        fpsHandler.calculateTrades()
        Assertions.assertEquals(fpsHandler.customerTrades[0].bonds, 66)
        Assertions.assertEquals(fpsHandler.customerTrades[0].stocks, -133)
        Assertions.assertEquals(fpsHandler.customerTrades[0].cash, 66)
    }

    @Test
    fun `customer portfolio is rebalanced`() {
        val trade = createCustomerPortfolio(stocks = -500, bonds = -200, cash = 900)
        val uri = URI(url + postEndpoint)
        every {
            restTemplate.postForEntity(
                any<String>(),
                any<String>(),
                String.javaClass
            )
        } returns ResponseEntity<String.Companion>(HttpStatus.CREATED)
        val fpsHandler = FpsHandler(
            url, postEndpoint, getEndpoint, customers, restTemplate
        )
        fpsHandler.customerTrades.add(trade)
        fpsHandler.rebalancePortfolios()
        Assertions.assertEquals(fpsHandler.responses[0], ResponseEntity<String.Companion>(HttpStatus.CREATED))
    }
}

fun createCustomer(
    id: Int = 1,
    email: String = "test@test.de",
    dateOfBirth: LocalDate = LocalDate.parse("1980-02-10"),
    riskLevel: Int = 1,
    retirementAge: Int = 65,
    yearsToRetirement: Int = 0,
    strategy: Strategy = defaultStrategy
) = Customer(
    id = id,
    email = email,
    dateOfBirth = dateOfBirth,
    riskLevel = riskLevel,
    retirementAge = retirementAge,
    yearsToRetirement = yearsToRetirement,
    strategy = strategy
)

fun createCustomerPortfolio(
    customerId: Int = 1,
    stocks: Int = 6700,
    bonds: Int = 1200,
    cash: Int = 400
) = CustomerPortfolio(
    customerId = customerId,
    stocks = stocks,
    bonds = bonds,
    cash = cash
)

fun createStrategy(
    id: Int = 0,
    minRiskLevel: Int = 0,
    maxRiskLevel: Int = 0,
    minYearsToRetirement: Int = 0,
    maxYearsToRetirement: Int = 0,
    stocksPercentage: Double = 0.0,
    cashPercentage: Double = 1.0,
    bondsPercentage: Double = 0.0
) = Strategy(
    id = id,
    minRiskLevel = minRiskLevel,
    maxRiskLevel = maxRiskLevel,
    minYearsToRetirement = minYearsToRetirement,
    maxYearsToRetirement = maxYearsToRetirement,
    stocksPercentage = stocksPercentage,
    cashPercentage = cashPercentage,
    bondsPercentage = bondsPercentage
)