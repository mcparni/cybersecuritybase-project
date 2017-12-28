# cybersecuritybase-project

## Introduction

This project is vulnerable by design and should never be put to any server ever. To be safe, only use on your local computer. The project has two brances: master and fixed. Master branch contains the project with described vulnerabilities listed below and the fixed branch contains all the fixes provided below. From commit history of the project, it is very easy to follow the changes in code. The best way to approch this is to clone the whole project: https://github.com/mcparni/cybersecuritybase-project.git to your local computer and from there just run it and point your browser to http://localhost:8080 . You can swap branches with ease for example: git checkout fixed (change to fixed) and so on.

The master branch (vulnerable) : https://github.com/mcparni/cybersecuritybase-project/tree/master  
The fixed branch (vulnerabilities fixed) :
https://github.com/mcparni/cybersecuritybase-project/tree/fixed

## The case

The story behind of the application is that some of the clients of the International Doge Show has been invited by email to participate to the event. They have been sent a password and a username through that mail. Using those credentials they are to login to the application and then submit their details: name, address and a credit card number for billing purposes. After submission users are greeted with a thank you page for succesfull registration to the event. Your job is to confirm that the site is secure and only those with proper credentials are able to register to the event.

## The vulnerabilities
 
The application has five vulnerabilities from Owasp Top 10 2013 (https://www.owasp.org/index.php/Top_10_2013-Top_10):

1. Missing Function Level Access Control
2. Cross-Site Request Forgery (CSRF)
3. Cross-Site-Scripting (XSS)
4. Security Misconfiguration
5. Sensitive Data Exposure

Note that there are most probably big number of more vulnerabilities, but our main focus is those five above.

## The identification

Start by running the Java application. Indentifying could be done with many tools, for example Owasp ZAP, but because this is extremely vulnerable and carelessly made, you might want to check out the low hanging fruits first.
In here it would be simply by checking what is in the robots.txt file in the root localhost:8080/robots.txt. From there there are a few things we can investigate a bit further:
...
Disallow: /form
Disallow: /h2-console
Disallow: /done
We see here that the developer has explicitly asked from site crawler robots not to look in to those folders. So we check them manually.

1. **Missing Function Level Access Control**  
Visit localhost:8080/form with your browser
localhost:8080/form turns out to be accessible so there is no checks whether we are authenticated or not and we have access to that form where people with proper credentials are supposed to. You can fill the form with some mockup data and send it if you like.

2. **Cross-Site Request Forgery (CSRF)**  
Visit localhost:8080/form with your browser
Open browser developer tools
Check attribute of each text field’s name attribute.
Open terminal program (or use zap or other tool to make a POST)
curl -X POST -F 'name=Name' -F 'address=Street 6' -F 'ccnr=1234' http://localhost:8080/form
Visit localhost:8080/done and confirm that you have just sent data from outside the site and it has been saved

3. **Cross-Site-Scripting (XSS)**   
Visit localhost:8080/form with your browser
In the text input field after ‘Name:’, write: <script>alert(1);</script>
Fill the other two fields with whatever strings you like.
Submit the form
You will be greeted an alert box with text ‘1’ in it.


4. **Security Misconfiguration**  
Visit localhost:8080/h2-console with your browser
Confirm that the JDBC URL is: jdbc:h2:mem:testdb
Confirm that the username is sa
Leave password blank
Click Test Connection
You should see message: Test successful


5. **Sensitive Data Exposure**  
Visit localhost:8080/h2-console with your browser
Confirm that the JDBC URL is: jdbc:h2:mem:testdb
Confirm that the username is sa
Leave password blank
Click Connect
You should see two tables, SIGNUP and USERS in the table list.
Select the big text field and type SELECT * FROM SIGNUP and click Run
You should see at least one row what you posted in phase nr. 2 (CSRF) and all the data is in cleartext.
Select the big text field and type SELECT * FROM USERS and click Run
You see one row with username and password in cleartext.

We have identified five vulnerabilities in the application and confirmed that the application in as insecure as possible. Everyone has access to all parts of the site, posts can be made from anywhere outside, there are no string escaping for values coming out of database, there is development tools left for the production site with default credentials and database has all sensitive data in cleartext.

## The fixes

1. **Sensitive Data Exposure**

I will not go to too much detail here with code, because of the length of it. It can be checked from the ‘fixed’ branch. But the idea is to define what columns in your database tables are supposed to contain sensitive data. In my application i have put every column as sensitive. The password itself is bcrypted and all the other fields go through SensitiveDataconverter -class when saved or retrieved from database.

2. **Security Misconfiguration**

From the projects’ pom.xml file after the <artifactId>spring-boot-devtools</artifactId> remove next line: <optional>true</optional>
In the src/main/java/sec/project/config/SecurityConfiguration.java, replace .antMatchers("/h2-console/*").permitAll() with .antMatchers("/h2-console/*").denyAll()

3. **Cross-Site Request Forgery (CSRF)**

In the src/main/java/sec/project/config/SecurityConfiguration.java, remove line http.csrf().disable();

4. **Missing Function Level Access Control**

In the src/main/java/sec/project/config/SecurityConfiguration.java, replace line .anyRequest().permitAll(); with .anyRequest().authenticated(); 

5. **Cross-Site-Scripting (XSS)**

In the src/main/resources/templates/done.html, replace tag: <span>Name: </span><span th:utext="${signup.name}">name</span> with <span>Name: </span><span th:text="${signup.name}">name</span> 

The application is now fixed the way that the steps to identify the vulnerabilities introduced earlier will not work anymore, However it probably contains a great deal more vulnerabilities not listed here. So again, do not use this software anywhere but your own local computer.

