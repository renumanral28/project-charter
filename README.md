# Charter/Retailer Customer Rewards Program

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

Given a record of every transaction during a three-month period, calculate the reward points earned for each customer per month and total.
· Make up a data set to best demonstrate your solution
· Check solution into GitHub

# Sample data
1. Using Spring Boot in memory H2 DB for creation of sample data. 
2. There are one schema - Transaction schema
3. Queries to create these schemas are present in script.sql under src/main/resources.
4. Sample data to above schemas are written under data.sql

# Test Cases
Test Cases are  under \src\test\java\com\rewards\service , \src\test\java\com\rewards\controller


# To run
- git clone https://github.com/renumanral28/project-charter


# Urls:
1. To get rewards for customers  -> /{customerId}/rewards
2. To get Rewards for Customer for a particular month  -> /{customerId}/rewards?month={month}

After the program is run, user can enter either of the following link in the URL:
http://localhost:8081/1/rewards
http://localhost:8081/1/rewards?month=2024-12

# Database 

To log into the H2 database to check the data in tables use following link:
http://localhost:8081/h2  
JDBC URL =jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL

# Postman Snapshots 
![Screenshot 2025-01-23 094648](https://github.com/user-attachments/assets/f0665c00-d7f5-4359-8b96-2a13529c782b)
![Screenshot 2025-01-23 115334](https://github.com/user-attachments/assets/64839601-0353-4e41-83dc-4f22371d82da)
![Screenshot 2025-01-23 134109](https://github.com/user-attachments/assets/ae6f0de4-bc9a-461f-9b15-110f2a4d9033)
