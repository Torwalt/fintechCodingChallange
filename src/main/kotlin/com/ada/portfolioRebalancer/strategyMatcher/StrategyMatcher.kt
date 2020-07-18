package com.ada.portfolioRebalancer.strategyMatcher

import com.ada.portfolioRebalancer.csvImporter.ImportedCustomer
import com.ada.portfolioRebalancer.csvImporter.ImportedStrategy

class StrategyMatcher(
    private val importedCustomers: List<ImportedCustomer> = listOf(),
    private val importedStrategies: List<ImportedStrategy> = listOf()
) {
    private val customers = mutableListOf<Customer>()
    private val strategies = mutableListOf<Strategy>()

    init {
        importedCustomers.forEach() { importedCustomer: ImportedCustomer ->
            val customer = fromImportedCustomer(importedCustomer)
            if (customer != null) {
                customers.add(customer)
            }
        }
        importedStrategies.forEach() { importedStrategy: ImportedStrategy ->
            val strategy = fromImportedStrategy(importedStrategy)
            if (strategy != null) {
                strategies.add(strategy)
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

