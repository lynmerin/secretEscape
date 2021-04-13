# SecretEscape
Service which exposes the account and transaction details . It also enable transfer of money between accounts.

# Exposed services
 **Get Accounts** : To get all the account details. _/accounts_ 

 **Get Account by account ID** : To get account details of 1 account. _/accounts/{acct ID}_

 **Get Transactions** : Get all Transactions that has happened in an Account. _/accounts/{acct ID}/transactions_

 **Get Transaction by Transaction Id** : Get one transaction in a particular account. _/accounts/{acct ID}/transactions/{trans ID}_

 **Perform transfer** : To perform a transfer of money from one account to another.This is a post operation with a request body.  _/accounts/transfer_

             request body sample: 
                                    _{

                                    "FromAccountId" : 100,

                                    "ToAccountId" :101,

                                    "Amount": 18.79

                                   }_

# Module structure
  aspect : Aspect around controller which is used for email service.

  controller : To place all the rest controller classes

  model : Holds all the model/ entity classes (POJO classes)

  repo : Holds all the repository interfaces for performing JPA operations

  service : Holds the service layer interfaces 

  service.impl : Holds the implementation classes corresponding to service layer interfaces

  specs : Holds all the Specification classes for adding predicates on repo calls

# Tech stacks :
  Spring Rest Controller for exposing the endpoints via HTTP methods.

  H2 db used for saving the data.

  JPA repo used for connecting to H2 DB.

  Flyway framework used to run scripts in H2 DB for initial account set up.

  Spring JavaMailer used to enable email functionality.

  Junit 5 with Mockito used for unit testing.


# Local Set Up 
 
Clone the git repo.

Run Maven clean build.  _mvn clean build_

Run as a spring Boot application. 

Application will be up and running on port _8821_.

To Enable email : If there are no firewall set up in your system by uncommenting line 34 and 42 in com.example.se.aspect.EmailAspect.java class.
Application.properties file holds the sender email and password. Password should be the app password while using gmail as the mail entity. 
To generate the app password : Go to sender gmail account -> manage google accounts --> security --> app passwords . Create a new app password for mail based on the OS the application is running. 

# Run junits

Run as Junit or using maven test : _mvn clean install test -Dtest=com.example.se.**.*Test -DfailIfNoTests=false_

## Could have - 

1. Develop UI for the services using grails (time constraint ,since grails is new for me) 
2. Add proper logging 
3. Add Junit for email testing
4. Add Integration junits






