package com.ada.portfolioRebalancer.strategyMatcher

import com.ada.portfolioRebalancer.csvImporter.ImportedCustomer
import com.ada.portfolioRebalancer.csvImporter.ImportedStrategy
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.Calendar

data class Strategy(
    val id: Int = 0,
    val minRiskLevel: Int = 0,
    val maxRiskLevel: Int = 0,
    val minYearsToRetirement: Int = 0,
    val maxYearsToRetirement: Int = 0,
    val stocksPercentage: Double = 0.0,
    val cashPercentage: Double = 1.0,
    val bondsPercentage: Double = 0.0

)

var defaultStrategy = Strategy()


data class Customer(
    val id: Int = 0,
    val email: String = "",
    val dateOfBirth: LocalDate = LocalDate.parse("1980-01-01"),
    val riskLevel: Int = 0,
    val retirementAge: Int = 0,
    val yearsToRetirement: Int = 0,
    val strategy: Strategy = defaultStrategy
) {}

fun fromImportedStrategy(importedStrategy: ImportedStrategy): Strategy? {
    val anyFieldNull = listOf(
        importedStrategy.id,
        importedStrategy.minRiskLevel,
        importedStrategy.maxRiskLevel,
        importedStrategy.minYearsToRetirement,
        importedStrategy.maxYearsToRetirement,
        importedStrategy.stocksPercentage,
        importedStrategy.cashPercentage,
        importedStrategy.bondsPercentage
    ).any() { it == null }
    if (anyFieldNull) {
        return null
    }
    val stocks = importedStrategy.stocksPercentage!!.toDouble()
    val cash = importedStrategy.cashPercentage!!.toDouble()
    val bonds = importedStrategy.bondsPercentage!!.toDouble()
    val total = stocks + cash + bonds

    return Strategy(
        importedStrategy.id!!.toInt(),
        importedStrategy.minRiskLevel!!.toInt(),
        importedStrategy.maxRiskLevel!!.toInt(),
        importedStrategy.minYearsToRetirement!!.toInt(),
        importedStrategy.maxYearsToRetirement!!.toInt(),
        stocks / total,
        cash / total,
        bonds / total
    )
}

fun fromImportedCustomer(
    importedCustomer: ImportedCustomer,
    currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
): Customer? {
    val anyFieldNull = listOf(
        importedCustomer.id,
        importedCustomer.email,
        importedCustomer.dateOfBirth,
        importedCustomer.riskLevel,
        importedCustomer.retirementAge
    ).any() { it == null }
    if (anyFieldNull) {
        return null
    }

    var parsedDateOfBirth: LocalDate
    try {
        parsedDateOfBirth = LocalDate.parse(importedCustomer.dateOfBirth)
    } catch (e: DateTimeParseException) {
        return null
    }
    val currentAge = currentYear - parsedDateOfBirth.year
    val retirementAge = importedCustomer.retirementAge!!.toInt()
    val yearsToRetirement = retirementAge - currentAge

    return Customer(
        importedCustomer.id!!.toInt(),
        importedCustomer.email!!,
        parsedDateOfBirth,
        importedCustomer.riskLevel!!.toInt(),
        retirementAge,
        yearsToRetirement
    )
}
