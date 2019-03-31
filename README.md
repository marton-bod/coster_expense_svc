## Coster.io - Expense service

Microservice responsible for listing, saving, editing and deleting expense items. Developed in spring-boot.

### Build the app:
* Prerequisites: Maven, JDK11
* `mvn clean install -Pdocker` - if you have docker engine
* `mvn clean install` - if not

### REST Interface:
- Swagger UI: localhost:9000/swagger-ui.html

### Actuator endpoints:
- Health: localhost:9000/actuator/health
- Beans: localhost:9000/actuator/beans
- Env vars: localhost:9000/actuator/env
- Status: localhost:9000/actuator/status
