
# Coding Exercise

Implement a Spring Boot REST API which allows sending an email to one or more recipients. Email delivery should be done via sendgrid.com.

# Purpose

This document contains the information regarding the **rakenemail** app and how to use it.  

# API Implementation Details  
  
  
# Endpoint URL(s)

	http://localhost:8080/sendemail
	http://localhost:8080/sendemail?enrich=true

# HTTP Method	- POST  

**Consumes** - ElectronicMail (request body, json)

**Mandatory Fields**
	
    from - Valid Email Address  
    subject - A String with at least one character  
    toRecipients - One or more valid email addresses  
    content – A String with at least one character

**Optional Fields**

    ccRecipients - One or more valid email addresses  
    bccRecipients - One or more valid email addresses  
    enrich (query param) - boolean

**Produces** - EmailResponse (statusCode, message)

# Sample Input

{  
	
	"from": "test@raken.com",  
    "to": ["emailOne@rakenapp.com ", "emailTwo@test.com"],  
    "cc": ["emailThree@test.com"],  
    "bcc": ["emailFour@test.com", "emailFive@test.com"],  
    "subject": "Test Email",  
    "content": "This is a test email from Raken Email App"  
}

# Sample Output

**Success**

{  
   
    "statusCode": 202,
    "message": "Success”
}

**Failure**

{
    
    "statusCode": 400,
    "message": " Bad Request, to does not contain any valid email addresses"
}

# Config toggle

**Should configure in application.properties file**  

**Enable** - set "raken.email.filter.enable" property to **true**  
**Disable** -  set "raken.email.filter.enable" property to **false**

# API Validations

    • All the email addresses should be in valid format as per RFC 5322.   
    • If Email filter is enabled, To Recipients should contain at least one rakenapp.com domain email address.  
    • If at least one of the To Recipients email address is valid, email will be sent to all the valid email addresses  
      and the request is considered as a partial success and user will be informed via output.  
    • All other validations provided by the SendGrid are intact.

# Assumptions

    • There is no need to persist the data considering the current requirements as we don’t see any need to retrieve the past data or backtrack.

# Enhancements

    • Persist this data in the database for future reference/back tracking if needed as per the future requirements.  
    • Add a common class to declare all the constants in the application.  
    • Integrate a Messaging system from which our app listens to the incoming messages on Topic/Queue and sends the email using our API.  
    • As the functionalities/modules grow, add integration tests as required.  
    • Define a fallback mechanism/approach if any of the external API calls fail due to any downtime, network issues, etc. at their end.  
    • Add a spring config server to pull configurations and avoiding sensitive data (api keys) in source code  

# How to run the application

**Pre-requisites**

    Java 8  
    Maven  
	Get the api key of send grid and setit to the property **sendgrid.api.key** in the application.properties file  
**Run from Eclipse**

    Import project as an Existing Maven Project  
	Resolve all the dependencies  
    Right click on the project - Run as - Spring Boot App  

**Run from command line as a stand alone java app**

	Go to the project folder  
	run: ./mvnw clean package                       - this will clean target folder, compile, run tests and generate the jar  
	java -jar target/rakenemail-0.0.1-SNAPSHOT.jar  - default value of raken.email.filter.enable is false in application.properties  
	java -jar target/rakenemail-0.0.1-SNAPSHOT.jar --raken.email.filter.enable=true    : enabling the filter via command line without the need of changing in application.properties
