# AnyMind interview test
Hi, This readme file contains the information that I want to share with you about the task.

## API Endpoints
* http://localhost:8080/playground (GraphQL Playground)
* http://localhost:8080/graphql (GraphQl generic endpoint)

### Make Payment Mutation example:
```
mutation {
  makePayment(paymentRequest: { 
    customerId: 12324,
    paymentMethod: "VISA",
    price: 180.00,
    priceModifier: 0.95,
    dateTime: "2022-09-01T00:00:00Z",
    additionalItems: "{\"last4CardNumber\":\"1234\", \"courierService\":\"YAMATO\"}"
  }) {
    finalPrice,
    points
  }
}

```
Consider `additionalItems` field. It is a `String type` that is holding a JSON string.
Because GraphQl schema implementation is not supporting Json as a scalar data type you should put your JSON string here.
Actually, it is possible to do some advanced magics to prevent this but there are some trade-offs:

1. Implementing GraphQl schema and using Polymorphism.
But this way causes more complicated steps to add new payment method compared with the current flow. But this is not our case.
Implementing a custom schema is complicated by itself and takes time, so I ignore that for now.

2. Almost all client applications have good libraries to support JSON encoding and decoding.
Because this API is provided for client applications, we can easily ask them to follow our contract and save ourselves from implementing complicated codes for fancy input formatting in GraphQl.


### Check payment method Query Example:
```
query {
  paymentMethod(name: "VISA") {
    name,
    minPriceModifier,
    maxPriceModifier,
    pointModifier
  }
}
```

### Sales Report Query example:
```
query  {
  hourlySaleReport(input: {
    startDateTime: "2022-12-11T00:00:00Z",
    endDateTime: "2022-12-15T00:00:00Z"
  }) {
    dateTime,
    points,
    sales
  }
}
```

-----
## Architecture
Application implementation is stateless. The state of the application is held in the database. I have chosen Mysql as a database.
Because it is relational, easy to use, has a good community and many companies are using it. Why a relational database?
Because we are developing POS application. The nature of this application requires us to choose a database to handle more writes
and redundant data (That is inevitable in Nosql databases) that may cause problems in the future. also, Mysql is using the **Slave-Master** pattern for scaling.
Also, sharding is a good option in cases the load is going to increase.

Consider the `transaction` table for sharding. possible fields for sharding is **user_id**, then we can load balance user requests easily between shards.
But it may make admins reports generation more tricky.
Another possible field is **date_time** because reports are generated based on time in most cases which will help to keep the size of each shard relatively equal.

Anyway, For the `MakePayment` command, the nature of the system should be **CP** in the **CAP Theorem**. This is another reason why relational databases are better in this case. Because relational databases are supporting ACID.
But based on business logic you may choose **AP** for report requests. I will describe it in the **read scenario** section.
Actually, we need to keep **P** from the **CAP theorem** for scaling and load balancing between many servers. that's why we should choose between **Consistency** and **Availability** in each business scenario.

For the Web API interface, I have chosen `Graphql` because of its advantages and also the auto-documentation that comes with it.

Authorization implementation is skipped due to time limitations, but you can use the authorization token and JWT. check the
`test.anymind.pos.app.AppGraphqlContextFactory` implementation. You can extract the user id and use it in the application.

The application itself is braked down into two sections. `app` and `domain`. All business logic should be encapsulated in the `domain` package.
This architecture is suggested by the `Clean Architecture` book to separate business logic concerns from technology and framework.
But you can see I am using spring service annotate and JPA annotates in the domain. Because these items do not cause domain logic to couple with the Framework in the abstraction. in my opinion, it is ok. Because these annotations do not force us to implement any code for the framework or data source.

The `app` package is holding application and framework codes. This code should not affect business logic. It means we should be able to change the framework and related technologies any time we need without problems with the business logic.

Also, if we are forced to implement some infrastructure code like repository implementations (that in this project, JPA is helping us) we should put them into the `infrastructure` package.

### Patterns that is used
* Factory
* Repository
* Clean Architecture principles
* Dependency Inversion
* Dependency Injection by spring framework

### Add newer payment methods
Because new payment methods are few (at most maybe 20 or even 50), You can easily create a class for this payment method and extend it from `test.anymind.pos.domain.lib.payment.method.APaymentMethod` and implement abstract methods.
Two functions are defined in this class, `calculateFinalPrice` to calculate the final price and `calculateFinalPoints` to calculate points.

In cases where the calculation for `finalPrice` or `points` is different from the base implementation, you can overwrite each of them and implement your own code.
But why I decided to separate these functions instead of implementing them in one method? because of the Single Responsibility principle in the OOP.

Also, you can add a `suspend` keyword and use the coroutine scope if the implementation is costly. I did not implement this scenario to prevent over-engineering but the code style will support it.

The payment method classes use the factory pattern to build each one required by the name. So you can pass the user input to the factory class to get a suitable `PaymentMethod` implementation.

Also, `addAdditionalData` function is used to modify additional items provided by the user and validate them. Then another method is used to get the `Json string` for future usages.

### Write scenario
![Write scenario UML](./write-scenario-uml.jpg "Write scenario UML")

In this application write scenario is straightforward. Because we have Chosen **CP** from **CAP**, On user request to make a payment request we will save data in atomic mode into the database.

But we can improve our design by adding a message queue and background workers to create materialized view for future usage in the read scenario.
I didn't implement this architecture due to prevent over-engineering.


### Read scenario
![Read scenario UML](./read-scenario-uml.jpg "Read scenario UML")

The read model is straightforward too. In the repository, we can generate a simple SQL query to retrieve data from MySQL.
To reduce database load, we can also cache the result for specific TTL on our cache cluster. This will help us to prevent repeating queries to the database for frequent sale report requests.
To make the cache more consistent, we can remove the cache for reports that are filtering recent sales when a payment request happens.

> I didn't implement the cache scenario in the repository but it should be simple.


### Error handling
Due to using `GraphQl` as API gateway technology, an Error response is well-defined by GraphQl standard. I stick to it.
Also, because GraphQl is using `Schema` to validate user input, in most cases, user inputs are typesafe. for logical
error handling I am using the `check` method in the Kotlin and the message will define in each case.
Because I am developing web API, I believe we can omit translation. It is a client-side concern in most cases.

## Unit Testing
I am using JUnit as expected :). Some dependencies are added to support Junit integration with the Spring framework. So for repositories,
I can use the H2 database for the test purpose. Also, I limit testing to UnitTesting. Integration testing is an advanced topic and requires more detail about the business logic and etc.
So there is no test for GraphQl API that I believe, it is pointless in this project since we are defining a schema as an interface
between our clients and the server.

Due to time limitations, I have implemented some `PaymentMethod` tests for various implementations.
There is no need to write tests for all scenarios because it is not possible to find them correctly, and it is time-wasting.
Instead, we can implement tests for known and possible scenarios that we are aware of them and let the time help us to cover more complicated cases when we find them.
But it is necessary to keep the test synced with the code.

> I believe the best pattern to implement tests is TDD. But because this project is my first Spring project with Kotlin, I explored it first, and then I wrote the tests.

> I have settled up GitHub action to run tests. check https://github.com/aminkt/anymind-kotlin-pos-app/actions/workflows/tests.yml

## Deploying on the production
I have prepared 3 ways to run the application on your server:

### Run by using the Docker Image
There is a GitHub action to build and push the docker image after each commit on the main branch of the repository.
You can easily run the application server using the docker image. Run the below command on your server to run the application:

```
docker run -p 80:8080 --env MYSQL_HOST="localhost:3306" --env MYSQL_DATABASE=anymind_pos aminkt/anymind-kotlin-pos-app:latest
```

You can find the docker image at https://hub.docker.com/repository/docker/aminkt/anymind-kotlin-pos-app

### Download the .jar file from the GitHub repository
After each commit on the main branch of the repository, A GitHub action will run to build the application artifacts.
Check https://github.com/aminkt/anymind-kotlin-pos-app/actions/workflows/gradle.yml to download the related .jar files.


### Docker Compose file
Easily clone the repository and run `docker-compose up -d` to run the application on your server.
The Mysql database is settled up in `docker-compose.yml`.

## Interview Objectives
* You have to use Git [✓]
* Your code has to be clear [✓]
* You know good coding practices and patterns [✓]
* Your API can handle incorrect data [✓]
* Your architecture is extendable/testable [✓]
* Your system can be executed easily [✓]
* You can consider multi-thread, many servers [✓]
* Your system should be tested [✓]
* Your system should be able to scale with newer payment methods [✓]

Please let me know if you think I have an issue with my task. I will be thankful to learn from you.
Please help me to improve my skills by sharing your opinion.