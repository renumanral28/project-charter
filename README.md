# Charter/Retailer Customer Rewards Program

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

Given a record of every transaction during a three-month period, calculate the reward points earned for each customer per month and total.
· Make up a data set to best demonstrate your solution
· Check solution into GitHub


# Tech Stack
- **Java 8**  
- **Spring Boot**  
- **Spring Data JPA**  
- **H2 (Database)**  
- **JUnit 5 & Mockito (Testing)**   
- **Gradle** 



# Sample data
1. Using Spring Boot in memory H2 DB for creation of sample data. 
2. There are one schema - Transaction schema
3. Queries to create these schemas are present in script.sql under src/main/resources.
4. Sample data to above schemas are written under data.sql

# Test Cases
Test Cases are under :
  \src\test\java\com\rewards\service 
  \src\test\java\com\rewards\controller


# To run
- git clone https://github.com/renumanral28/project-charter


# Urls:
1. To get rewards for customers  -> /rewards/{customerId}
2. To get Rewards for Customer for a particular month  -> /rewards/{customerId}/{year}/{month}

After the program is run, user can enter either of the following link in the URL:
http://localhost:8081/rewards/1
http://localhost:8081/rewards/1/2024/12

# Database 

To log into the H2 database to check the data in tables use following link:
http://localhost:8081/h2  
JDBC URL =jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL

