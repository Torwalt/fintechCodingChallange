package com.ada.portfolioRebalancer.csvImporter

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths
import kotlin.reflect.full.memberProperties

class Importer(val path: String = "/", val csvReader: CsvReader = CsvReader()) {
    var importedStrategies: MutableList<ImportedStrategy> = mutableListOf()
    var importedCustomers: MutableList<ImportedCustomer> = mutableListOf()

    private val files = listOf("strategies.csv", "customers.csv")
    private val logger = LoggerFactory.getLogger(javaClass)

    fun import() {
        this.files.forEach { fileName: String ->
            val rows = getRows(fileName) ?: return
            if (fileName == "strategies.csv") createStrategy(rows) else createCustomer(rows)
        }
    }

    private fun getRows(fileName: String): List<Map<String, String>>? {
        val filePath = Paths.get(path, fileName)
        return try {
            val file = File(filePath.toString())
            csvReader.readAllWithHeader(file)
        } catch (e: FileNotFoundException) {
            logger.error("$fileName not found in $path!")
            null
        }
    }

    private fun createStrategy(rows: List<Map<String, String>>): Unit {
        rows.forEach() { row: Map<String, String> ->
            val strategy = ImportedStrategy(
                row["strategyId"],
                row["minRiskLevel"],
                row["maxRiskLevel"],
                row["minYearsToRetirement"],
                row["maxYearsToRetirement"],
                row["stocksPercentage"],
                row["cashPercentage"],
                row["bondsPercentage"]
            )
            this.importedStrategies.add(strategy)
        }
    }

    private fun createCustomer(rows: List<Map<String, String>>): Unit {
        rows.forEach() { row: Map<String, String> ->
            val customer = ImportedCustomer(
                row["customerId"],
                row["email"],
                row["dateOfBirth"],
                row["riskLevel"],
                row["retirementAge"]
            )
            this.importedCustomers.add(customer)
        }
    }

    // Todo
    private fun validate_header(header: Array<String>, className: String): Unit {
        if (className == "strategies.cvs") {
            ImportedStrategy::class.memberProperties
        }
    }
}