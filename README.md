# atm-service-application

#Technology Stack
Java 8
Spring Boot 2.0.4

#Build Tool
Maven 3

#Import instruction
This is a maven project, can be imported to your favorite IDE as a maven project

#Build and run instruction
- mvn clean install
- To run on console, java -jar target/atm-service-0.0.1-SNAPSHOT.jar

#Test instruction
- The code base includes an integration tests which are in ATMServiceIntegrationTest.java, in order to see the result of requirements, 
  simply run each tests through and IDE. The implementation's logs print out each step result, please check logs on console.  

#Assumptions
- Cash machine always has enough notes for disbursing, the logic in implementation does not check that the withdraw amount is payable by atm. 

- Number of notes is always stable and enough to deliver to customer.   

- Dao, Controller layers are already implemented and the next step is to integrate this implementation to them. 
  After this integration, we can introduce BDD, and other tools for manuel end to end testing, such as swagger, curl scripts etc.

- The application has global exception handler and any exception of validations are handled by it, return a clear response to the client.
  I included a basic implementation,  ATMServiceExceptionHandler.java