package com.ada.portfolioRebalancer.api

import com.ada.portfolioRebalancer.fpsHandler.CustomerPortfolio
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("fps")
class FPSApi {
    @GetMapping("/customer")
    @ResponseBody
    fun getCustomer(@RequestParam id: Int = 1) = CustomerPortfolio(id, 1000, 1000, 1000)

    @PostMapping("/execute")
    fun postTrade() =
        ResponseEntity<String.Companion>(HttpStatus.CREATED)
}
