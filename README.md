Real-time Transaction System
============================
## Overview
Welcome to my real-time transaction system! This application is a simple bank ledger system that utilizes the [event sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) pattern to maintain a transaction history. It allows users to perform basic banking operations such as depositing funds, withdrawing funds, and checking balances. The ledger maintains a complete and immutable record of all transactions, enabling auditability and reconstruction of account balances at any point in time.

## Details
The service accepts two types of transactions:
1) Loads: This operation adds money to a user's account (credit).

2) Authorizations: This operation conditionally removes money from a user's account (debit).

Every load or authorization PUT returns the updated balance following the transaction. Authorization declines are saved, even if they do not impact balance calculation.

The event sourcing pattern is implemented to record all banking transactions as immutable events. Each event captures relevant information such as transaction type, amount, timestamp, and account identifier. The structure of events is defined in a way that ensures they can be easily serialized and persisted to a data store. For the current version of the application, an in-memory object is used for data storage. The application can be easily bootstrapped locally for testing and exploration.## Bootstrap instructions
In order to run the service, replace the JAVA_PATH below with the path to your Java.EXE file and run the following 
command:

```bash
JAVA_PATH -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true "-Dmanagement.endpoints.jmx.exposure.include=*" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.4\lib\idea_rt.jar=52073:C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.4\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\Users\antho\IdeaProjects\CodeScreen_mq82t4kh\target\classes;C:\Users\antho\.m2\repository\com\google\guava\guava\28.0-jre\guava-28.0-jre.jar;C:\Users\antho\.m2\repository\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;C:\Users\antho\.m2\repository\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;C:\Users\antho\.m2\repository\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;C:\Users\antho\.m2\repository\org\checkerframework\checker-qual\2.8.1\checker-qual-2.8.1.jar;C:\Users\antho\.m2\repository\com\google\errorprone\error_prone_annotations\2.3.2\error_prone_annotations-2.3.2.jar;C:\Users\antho\.m2\repository\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;C:\Users\antho\.m2\repository\org\codehaus\mojo\animal-sniffer-annotations\1.17\animal-sniffer-annotations-1.17.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-starter\3.2.5\spring-boot-starter-3.2.5.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot\3.2.5\spring-boot-3.2.5.jar;C:\Users\antho\.m2\repository\org\springframework\spring-context\6.1.6\spring-context-6.1.6.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\3.2.5\spring-boot-autoconfigure-3.2.5.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-starter-logging\3.2.5\spring-boot-starter-logging-3.2.5.jar;C:\Users\antho\.m2\repository\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar;C:\Users\antho\.m2\repository\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar;C:\Users\antho\.m2\repository\org\apache\logging\log4j\log4j-to-slf4j\2.21.1\log4j-to-slf4j-2.21.1.jar;C:\Users\antho\.m2\repository\org\apache\logging\log4j\log4j-api\2.21.1\log4j-api-2.21.1.jar;C:\Users\antho\.m2\repository\org\slf4j\jul-to-slf4j\2.0.13\jul-to-slf4j-2.0.13.jar;C:\Users\antho\.m2\repository\jakarta\annotation\jakarta.annotation-api\2.1.1\jakarta.annotation-api-2.1.1.jar;C:\Users\antho\.m2\repository\org\springframework\spring-core\6.1.6\spring-core-6.1.6.jar;C:\Users\antho\.m2\repository\org\springframework\spring-jcl\6.1.6\spring-jcl-6.1.6.jar;C:\Users\antho\.m2\repository\org\yaml\snakeyaml\2.2\snakeyaml-2.2.jar;C:\Users\antho\.m2\repository\org\slf4j\slf4j-api\2.0.13\slf4j-api-2.0.13.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-starter-web\3.2.5\spring-boot-starter-web-3.2.5.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-starter-json\3.2.5\spring-boot-starter-json-3.2.5.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.15.4\jackson-databind-2.15.4.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.15.4\jackson-annotations-2.15.4.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.15.4\jackson-core-2.15.4.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.15.4\jackson-datatype-jdk8-2.15.4.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.15.4\jackson-datatype-jsr310-2.15.4.jar;C:\Users\antho\.m2\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.15.4\jackson-module-parameter-names-2.15.4.jar;C:\Users\antho\.m2\repository\org\springframework\boot\spring-boot-starter-tomcat\3.2.5\spring-boot-starter-tomcat-3.2.5.jar;C:\Users\antho\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\10.1.20\tomcat-embed-core-10.1.20.jar;C:\Users\antho\.m2\repository\org\apache\tomcat\embed\tomcat-embed-el\10.1.20\tomcat-embed-el-10.1.20.jar;C:\Users\antho\.m2\repository\org\apache\tomcat\embed\tomcat-embed-websocket\10.1.20\tomcat-embed-websocket-10.1.20.jar;C:\Users\antho\.m2\repository\org\springframework\spring-web\6.1.6\spring-web-6.1.6.jar;C:\Users\antho\.m2\repository\org\springframework\spring-beans\6.1.6\spring-beans-6.1.6.jar;C:\Users\antho\.m2\repository\io\micrometer\micrometer-observation\1.12.5\micrometer-observation-1.12.5.jar;C:\Users\antho\.m2\repository\io\micrometer\micrometer-commons\1.12.5\micrometer-commons-1.12.5.jar;C:\Users\antho\.m2\repository\org\springframework\spring-webmvc\6.1.6\spring-webmvc-6.1.6.jar;C:\Users\antho\.m2\repository\org\springframework\spring-aop\6.1.6\spring-aop-6.1.6.jar;C:\Users\antho\.m2\repository\org\springframework\spring-expression\6.1.6\spring-expression-6.1.6.jar dev.codescreen.Application
```

you could use the following command:
```bash
mvn -f pom.xml spring-boot:run
```

## Testing
To run the tests, run the following command:
```bash
mvn test
```

Or, if you would like to start the application if the tests pass, run the following command:
```bash
mvn test spring-boot:run
```


## Design considerations
- I decided to structure the Model in the following way:
    - UserDetails: This class contains the user's details such as the user's name, email, and password. This class can 
later include the Customer's Social Security, work information, income, birth date, etc. The userDetails class is a 
sensitive class and should not be exposed to the client. Once access is granted, an admin can view all the information 
required to check that the customer is who they are claiming to be (except the password even though it's hashed - an 
important step before releasing such a service).
    - User: This class contains the UserDetails, the user's id, and the user's balance. This class can later have the 
user's reward points balance, interest rate with the bank, or any other information that is not sensitive and may be
recalculated or changed.
    - UserAccount: I hesitated to create this class and separate the User and the UserAccount, but I decided to do so
because I felt the need for a class that would contain any information that is also not sensitive but can only be final.
Of course, the transaction history may grow, and also would a hypothetical reward points history, but it may not be
deleted and the transactions in the transactionLog or the reward exchanges in the rewardPointsHistory may never be 
removed.
    - TransactionEvent: This class contains the timestamp, the transaction type (an enum), the amount, the user's id, 
the message  id, and the currency. Even though I don't allow for multiple currencies in the user's account, I decided to 
include the currency in the transaction event in case such a feature is added in the future. It is important to add all
the information that we believe should be known about a transaction in the future for an audit or a bug fix.
- For the service, I created a UserService Interface that may be implemented however the developer sees fit. I created
a UserServiceImpl that uses the UserAccountRepository to interact with the database.
- The UserAccountRepository is an interface and my implementation is a hashmap. However, in a real-world scenario, one
would create another implementation of the UserAccountRepository that interacts with a database.
- I created the IncorrectCurrencyException to handle the case where the user tries to deposit or withdraw in a currency
that is not their own. I also created the InsufficientBalanceException to handle the case where the user tries to
withdraw more money than they have in their account.
- The dto package contains the Request and Response classes that are used to interact with the client.

## Assumptions
- I assumed that the system would be prepopulated with a list of users and their balances. However, I did create a 
LoadDatabase.java in dev.codescreen.data to prepopulate the database with a list of users and their balances, and I
printed the list of users and their ids to the console for easy testing.
- I assumed that a user may only have one currency in their account. Thus, a user can only deposit or withdraw in the
currency that they have in their account. This can be easily changed by calculating the exchange rate between the two
currencies and converting the amount to the user's currency before depositing or withdrawing. I believe that either way,
the user should still be able to see the balance in their currency and should have 1 default currency.

## Deployment considerations
- If I were to deploy this, I would make sure to implement password hashing and salting to ensure that the user's
password is secure. I would consider Bcrypt, as unlike SHA, it is designed to be slow and I would use a high number of
rounds to make it even slower. This is necessary for a banking application as it is a high-value target for hackers.
- I would also implement a Session Management system to ensure that the user is logged out after a certain period of
time (I would consider a short period of time for a banking application).
- I would also implement a rate limiter to prevent brute force attacks on the login system.
- For storing data, I would use a relational database such as MySQL or PostgreSQL. The additional cost is worth the
security and scalability that comes with it.
- I would use an ORM library such as Spring Data JPA to simplify the interaction with the database.
- There are many important APIs that would need to be implemented such as user registration, login, logout, password
reset, etc. I would implement these APIs with the necessary security measures.
- I would consider VueJS or ViteJS for the front-end as they are both light and easier for big teams to work on than
jsp or thymeleaf. NextJS is also a good option as it allows for server-side rendering if such functionality is needed 
(for example, to calculate the exchange rate between two currencies and convert the amount to the user's currency before
depositing or withdrawing).
