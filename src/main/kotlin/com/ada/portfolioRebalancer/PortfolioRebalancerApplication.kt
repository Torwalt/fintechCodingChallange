package com.ada.portfolioRebalancer

import com.ada.portfolioRebalancer.csvImporter.Importer
import com.ada.portfolioRebalancer.fpsHandler.FpsHandler
import com.ada.portfolioRebalancer.strategyMatcher.StrategyMatcher
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PortfolioRebalancerApplication

fun main(args: Array<String>) {
    runApplication<PortfolioRebalancerApplication>(*args)

    val batchPost = Key("batchPost", intType)
    val config = ConfigurationProperties.fromResource("application.properties")

    val importer = Importer(CsvReader())
    importer.import()
    val strategyMatcher = StrategyMatcher(importer.importedCustomers, importer.importedStrategies)
    val matchedCustomers = strategyMatcher.match()
    val fpsHandler = FpsHandler("http://localhost:8080/fps/", "execute", "customer", matchedCustomers)
    fpsHandler.calculateTrades()
    fpsHandler.rebalancePortfolios(config[batchPost])
}
