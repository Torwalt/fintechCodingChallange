package com.ada.portfolioRebalancer.importer

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import java.io.File
import java.nio.file.Paths
import kotlin.reflect.full.memberProperties

class Importer(val path: String = "/", val csvReader: CsvReader = CsvReader()) {
    var strategies: MutableList<Strategy> = mutableListOf()
    var customers: MutableList<Customer> = mutableListOf()

    private val files = listOf("strategies.csv", "customers.csv")

    fun import() {
        this.files.forEach { fileName: String ->
            val rows = getRows(fileName)
            if (fileName == "strategies.csv") createStrategy(rows) else createCustomer(rows)
        }
    }

    private fun getRows(fileName: String): List<Map<String, String>> {
        val filePath = Paths.get(path, fileName)
        val file = File(filePath.toString())
        return csvReader.readAllWithHeader(file)
    }

    private fun createStrategy(rows: List<Map<String, String>>): Unit {
        rows.forEach() { row: Map<String, String> ->
            val strategy = Strategy(
                row["strategyId"],
                row["minRiskLevel"],
                row["maxRiskLevel"],
                row["minYearsToRetirement"],
                row["maxYearsToRetirement"],
                row["stocksPercentage"],
                row["cashPercentage"],
                row["bondsPercentage"]
            )
            this.strategies.add(strategy)
        }
    }

    private fun createCustomer(rows: List<Map<String, String>>): Unit {
        rows.forEach() { row: Map<String, String> ->
            val customer = Customer(
                row["customerId"],
                row["email"],
                row["dateOfBirth"],
                row["riskLevel"],
                row["retirementAge"]
            )
            this.customers.add(customer)
        }
    }

    private fun validate_header(header: Array<String>, className: String): Unit {
        if (className == "strategies.cvs") {
            Strategy::class.memberProperties
        }
    }
}