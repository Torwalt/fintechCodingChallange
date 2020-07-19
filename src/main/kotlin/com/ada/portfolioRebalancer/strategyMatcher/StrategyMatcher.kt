package com.ada.portfolioRebalancer.strategyMatcher

import com.ada.portfolioRebalancer.csvImporter.ImportedCustomer
import com.ada.portfolioRebalancer.csvImporter.ImportedStrategy
import org.slf4j.LoggerFactory

class StrategyMatcher(
    private val importedCustomers: List<ImportedCustomer> = listOf(),
    private val importedStrategies: List<ImportedStrategy> = listOf()
) {
    private val customers = mutableListOf<Customer>()
    private val strategies = mutableListOf<Strategy>()
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        importedCustomers.forEach() { importedCustomer: ImportedCustomer ->
            val customer = fromImportedCustomer(importedCustomer)
            if (customer != null) {
                customers.add(customer)
            } else {
                logger.warn("Corrupted $importedCustomer, could not map to Customer.")
            }
        }
        importedStrategies.forEach() { importedStrategy: ImportedStrategy ->
            val strategy = fromImportedStrategy(importedStrategy)
            if (strategy != null) {
                strategies.add(strategy)
            } else {
                logger.warn("Corrupted $importedStrategy, could not map to Strategy.")
            }
        }
    }

    fun match(): MutableList<Customer> {
        val matchedCustomers = mutableListOf<Customer>()

        customers.forEach() { customer: Customer ->
            var matchedCustomer = Customer()

            strategies.forEach() { strategy: Strategy ->
                if (customer.riskLevel in strategy.minRiskLevel..strategy.maxRiskLevel
                    && customer.yearsToRetirement in strategy.minYearsToRetirement..strategy.maxYearsToRetirement
                ) {
                    matchedCustomer = customer.copy(strategy = strategy)
                }
            }

            matchedCustomers.add(matchedCustomer)
        }
        return matchedCustomers
    }
}

