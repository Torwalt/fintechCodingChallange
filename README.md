# Coding challenge for fintech startup

You can start the application as usual with, e.g. Intellij.

The `resources` folder contains the `customers.csv` and the `strategies.csv`, which can be adapted to add more customers and strategies.

The application provides a dummy API for mocking the FPS.

The application will automatically load the data provided in `customers.csv` and `strategies.csv` and perform the necessary steps before POSTing the data to the dummy FPS service.

The amount of trades per batch is configurable in `src/main/resources/application.properties`.

This challenge was interesting and fun, as I worked with kotlin for the first time.
