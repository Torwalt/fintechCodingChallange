package com.ada.portfolioRebalancer.fpsHandler

import com.ada.portfolioRebalancer.strategyMatcher.Customer
import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class FpsHandler(
    var url: String,
    var postEndpoint: String,
    var getEndpoint: String,
    var customers: List<Customer>,
    private val restTemplate: RestTemplate = RestTemplate()
) {

    private val jsonConverter = Gson()
    val customerTrades = mutableListOf<CustomerPortfolio>()
    val responses = mutableListOf<ResponseEntity<String.Companion>>()

    // auth is out of scope
    private fun getCustomerPortfolio(id: Int): CustomerPortfolio? {
        var getUrl = "$url$getEndpoint:$id"
        return restTemplate.getForObject(getUrl, CustomerPortfolio::class.java)
    }

    fun calculateTrades() {
        customers.forEach() { customer: Customer ->
            val customerPortfolio = getCustomerPortfolio(customer.id) ?: return@forEach
            var customerCurrentPortfolioDistribution = getCustomerPortfolioDistribution(customerPortfolio)
            var customerGoalPortfolioDistribution = PortfolioDistribution(
                customer.strategy.stocksPercentage - customerCurrentPortfolioDistribution.stocks,
                customer.strategy.bondsPercentage - customerCurrentPortfolioDistribution.bonds,
                customer.strategy.cashPercentage - customerCurrentPortfolioDistribution.cash
            )
            var tradeToExecute = CustomerPortfolio(
                customerId = customer.id,
                stocks = (customerPortfolio.stocks * customerGoalPortfolioDistribution.stocks).toInt(),
                bonds = (customerPortfolio.bonds * customerGoalPortfolioDistribution.bonds).toInt(),
                cash = (customerPortfolio.cash * customerGoalPortfolioDistribution.cash).toInt()
            )
            customerTrades.add(tradeToExecute)
        }
    }

    fun rebalancePortfolios(inBatchesOf: Int = 1) {
        var headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        var postUrl = url + postEndpoint
        val batchedTrades = customerTrades.chunked(inBatchesOf)

        batchedTrades.forEach() { customerTrades: List<CustomerPortfolio> ->
            var postBody = jsonConverter.toJson(customerTrades)
            var request = HttpEntity(postBody.toString(), headers)
            var responseEntity = restTemplate.postForEntity(postUrl, request, String.javaClass)
            responses.add(responseEntity)
        }
    }

    private fun getCustomerPortfolioDistribution(customerPortfolio: CustomerPortfolio): PortfolioDistribution {
        val totalPortfolio = customerPortfolio.bonds + customerPortfolio.cash + customerPortfolio.stocks
        return PortfolioDistribution(
            customerPortfolio.stocks.toDouble() / totalPortfolio.toDouble(),
            customerPortfolio.bonds.toDouble() / totalPortfolio.toDouble(),
            customerPortfolio.cash.toDouble() / totalPortfolio.toDouble()
        )
    }
}