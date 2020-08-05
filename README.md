# Coding challenge for fintech startup

## Task

Your goal is to write a service that does the following (the order may differ slightly in your implementation):

- Consumes customers and strategy CSV files
- For each customer, find which strategy applies to them using their risk level and years until retirement
- Retrieves the customer portfolios from the Financial Portfolio Service
- Use a customer's selected strategy's asset percentages, calculate the trades that must be made to rebalance their
  portfolio
- Batch customer trades and send them to the Financial Portfolio Service - the max amount of trades per batch should be
  configurable

## General

You can start the application as usual with, e.g. Intellij.

The `resources` folder contains the `customers.csv` and the `strategies.csv`, which can be adapted to add more customers
and strategies.

The application provides a dummy API for mocking the FPS.

The application will automatically load the data provided in `customers.csv` and `strategies.csv` and perform the
necessary steps before POSTing the data to the dummy FPS service.

The amount of trades per batch is configurable in `src/main/resources/application.properties`.

This challenge was interesting and fun, as I worked with kotlin for the first time.
