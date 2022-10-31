Terra services are generally organized into 3 layers, controller, service and DAO (data access object).
Controllers handle api requests and responses.
Services implement all business logic.
DAOs implement data persistence and querying.

# Controller
Controllers are the api entry point to the service. [OpenAPI](../service/src/main/resources/api/openapi.yml) is used to [generate](../service/generators.gradle) `*API` interfaces
which are implemented by controller classes. In this way we can craft a service's api and have the implementation
flow from there. Alternatively we could use java annotations on controller classes to generate OpenAPI
but that tends to lead to a poor API, we think about the api second or not at all.

Controllers are generally responsible for
* Resolving the user if required
* Checking access control
* Any translation between API model objects and service model objects
* Making service calls
* Translating service responses and errors to API responses and status codes

Controllers talk to services. It should be an exceptional situation that they talk directly to DAOs.

Note on access control: There can be some debate on whether this could be elsewhere. Certainly
more complicated or cross resource access control checks can be elsewhere. Pushing access control
checks down to the service layer generally make services less reusable. The most important thing
about access control checks is that it are implemented consistently and readably to avoid mistakes.

# Service
The service layer handles all business logic. 
If it is interesting, the code for it probably lives here.
If it is coordinating anything, the code for it probably lives here.
If it is making a decision, the code for it probably lives here.

The service layer is the transaction boundary (see [transactions](transactions.md) for more details).

Services talk to other services and DAOs.

# DAOs
DAOs or data access objects know how to do data things. Reading or writing to databases. 
Accessing other Terra services. DAOs do not make decisions, they get information for the service
layer to use to make decisions. These should be stupid that unconditionally do what they are told
and return undigested information.

DAOs are the bottom layer and should not call other DAOs or services.