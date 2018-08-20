# atm-service-application

#Technology Stack
Java 8
Spring Boot 2.0.4

#Build Tools
Maven 3

#Import instruction
This is a maven project, can be imported to your favorite IDE as a maven project

#Build and run instruction
- mvn clean install
- To run on console, java -jar target/atm-service-0.0.1-SNAPSHOT.jar

#Test instruction
- The code base includes an integration tests, ATMServiceIntegrationTest.java, in order to see the result of requirements, simply run each tests. 
  params can be change..


#Assumptions
- Assume that the data layer is already exist, for testing purpose basic data layer which includes two maps is used. 
- Basic Exception handler to show a sample, in the application assume that it is already exist 

- Notes of denominations 5, 10, 20 and 50, is always in the atm.
- not include that check notes are not available.
- atm could serve fully.
- 