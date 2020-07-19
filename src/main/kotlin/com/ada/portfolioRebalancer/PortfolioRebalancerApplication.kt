package com.ada.portfolioRebalancer

import com.ada.portfolioRebalancer.csvImporter.Importer
import com.ada.portfolioRebalancer.fpsHandler.FpsHandler
import com.ada.portfolioRebalancer.strategyMatcher.StrategyMatcher
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PortfolioRebalancerApplication

fun main(args: Array<String>) {
    runApplication<PortfolioRebalancerApplication>(*args)
    val importer = Importer(CsvReader())
    importer.import()
    val strategyMatcher = StrategyMatcher(importer.importedCustomers, importer.importedStrategies)
    val matchedCustomers = strategyMatcher.match()
    val fpsHandler = FpsHandler("http://localhost:8080/fps/", "execute", "customer", matchedCustomers)
    fpsHandler.calculateTrades()
    fpsHandler.rebalancePortfolios()
}
