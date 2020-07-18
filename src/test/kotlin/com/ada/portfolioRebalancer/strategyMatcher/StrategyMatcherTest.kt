package com.ada.portfolioRebalancer.strategyMatcher

import com.ada.portfolioRebalancer.csvImporter.ImportedCustomer
import com.ada.portfolioRebalancer.csvImporter.ImportedStrategy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class StrategyMatcherTest {
    @Test
    fun `importedCustomer to Customer`() {
        val importedCustomer = createImportedCustomer()
        val customer = fromImportedCustomer(importedCustomer)

        Assertions.assertNotNull(customer)
    }

    @Test
    fun `importedCustomer with nulls to Customer`() {
        val importedCustomer = createImportedCustomer(riskLevel = null)
        val customer = fromImportedCustomer(importedCustomer)

        Assertions.assertNull(customer)
    }

    @Test
    fun `importedCustomer with wrong date format to Customer`() {
        val importedCustomer = createImportedCustomer(dateOfBirth = "1980/01/01")
        val customer = fromImportedCustomer(importedCustomer)

        Assertions.assertNull(customer)
    }

    @Test
    fun `importedCustomer to Customer correct yearsToRetirement`() {
        val importedCustomer = createImportedCustomer(dateOfBirth = "1994-09-13", retirementAge = "65")
        val customer = fromImportedCustomer(importedCustomer, currentYear = 2020)

        Assertions.assertEquals(customer!!.yearsToRetirement, 39)
    }

    @Test
    fun `importedStrategy to Strategy`() {
        val importedStrategy = createImportedStrategy()
        val strategy = fromImportedStrategy(importedStrategy)

        Assertions.assertNotNull(strategy)
    }

    @Test
    fun `importedStrategy with nulls to Strategy`() {
        val importedStrategy = createImportedStrategy(minYearsToRetirement = null)
        val strategy = fromImportedStrategy(importedStrategy)

        Assertions.assertNull(strategy)
    }

    @Test
    fun `customer and strategy are matched`() {
        val customer1 = createImportedCustomer()
        val customer2 =
            createImportedCustomer(id = "2", dateOfBirth = "1994-09-13", retirementAge = "65", riskLevel = "5")
        val strategy1 = createImportedStrategy()
        val strategy2 = createImportedStrategy(
            id = "2",
            minRiskLevel = "2",
            maxRiskLevel = "5",
            minYearsToRetirement = "15",
            maxYearsToRetirement = "40",
            cashPercentage = "20",
            bondsPercentage = "70",
            stocksPercentage = "10"
        )
        val customers = listOf(customer1, customer2)
        val strategies = listOf(strategy1, strategy2)
        val strategyMatcher = StrategyMatcher(customers, strategies)
        val matchedCustomers = strategyMatcher.match()

        Assertions.assertEquals(matchedCustomers.count(), 2)
        Assertions.assertEquals(matchedCustomers[0].strategy!!.id, 1)
        Assertions.assertEquals(matchedCustomers[1].strategy!!.id, 2)
    }
}

fun createImportedCustomer(
    id: String? = "1",
    email: String? = "test@test.de",
    dateOfBirth: String? = "1980-02-10",
    riskLevel: String? = "1",
    retirementAge: String? = "65"
) = ImportedCustomer(
    id = id,
    email = email,
    dateOfBirth = dateOfBirth,
    riskLevel = riskLevel,
    retirementAge = retirementAge
)

fun createImportedStrategy(
    id: String? = "1",
    minRiskLevel: String? = "1",
    maxRiskLevel: String? = "5",
    minYearsToRetirement: String? = "10",
    maxYearsToRetirement: String? = "50",
    stocksPercentage: String? = "0",
    cashPercentage: String? = "100",
    bondsPercentage: String? = "0"
) = ImportedStrategy(
    id = id,
    minRiskLevel = minRiskLevel,
    maxRiskLevel = maxRiskLevel,
    minYearsToRetirement = minYearsToRetirement,
    maxYearsToRetirement = maxYearsToRetirement,
    stocksPercentage = stocksPercentage,
    cashPercentage = cashPercentage,
    bondsPercentage = bondsPercentage
)
