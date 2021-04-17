## Before running

A running instance (not the cluster) of MongoDB is required. By default the application will look for it on localhost,
port 27017. To override set the environmental variable:
`SPRING_DATA_MONGODB_URI=mongodb://{your-host}:{your-port}/fee-db`
or simply edit the application.yml file.

### Building & running

To just build, type in the terminal `./gradlew build` from the project directory. This also runs tests. Tu build and
run, type in the terminal `./gradlew bootRun` from the project directory.

### API

There's a single endpoint: `/customer-fee` with an optional list parameter `customerIds`. It takes either the
keyword `ALL` or comma separated list of customer ids, for which the API should calculate the fee.