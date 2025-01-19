#A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.   A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).   Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total. 

Application Flow :

Use Postman URL :  http://localhost:8080/{customerId}/rewards

CustomerId to be used : 1,2

Controller : RewardsController
Service : RewardsService
Function name : getRewardsByCustomerId

Logic : Calculated 3 different timeslots i.e previous month , second previous  and third month

H2 db used

Queries used are :

CREATE TABLE CUSTOMER (CUSTOMER_ID int, CUSTOMER_NAME VARCHAR2(20) );
CREATE TABLE TRANSACTION (TRANSACTION_ID int,CUSTOMER_ID int ,TRANSACTION_DATE DATE,AMOUNT int);
INSERT INTO CUSTOMER(CUSTOMER_ID,CUSTOMER_NAME) values (1,'Alice');
INSERT INTO CUSTOMER(CUSTOMER_ID,CUSTOMER_NAME) values (2,'Wonderland');
INSERT INTO TRANSACTION(TRANSACTION_ID,CUSTOMER_ID,TRANSACTION_DATE,AMOUNT) VALUES (1,1,'2024-12-24',90);
INSERT INTO TRANSACTION(TRANSACTION_ID,CUSTOMER_ID,TRANSACTION_DATE,AMOUNT) VALUES (2,1,'2024-11-23',85);
INSERT INTO TRANSACTION(TRANSACTION_ID,CUSTOMER_ID,TRANSACTION_DATE,AMOUNT) VALUES (3,1,'2024-12-25',160);
INSERT INTO TRANSACTION(TRANSACTION_ID,CUSTOMER_ID,TRANSACTION_DATE,AMOUNT) VALUES (4,2,'2024-06-05',90);
COMMIT;


