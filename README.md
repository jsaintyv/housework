# Housework
HouseWork project is small spring-boot server which supplying a web application to register who done house work.
I use it to reward my children with ratio 10 points == 8€.


![alt text](https://github.com/jsaintyv/housework/raw/master/images/front.png "Front page")

# Environment requirements

To use it, you need to install :
- Java https://java.com/en/download/
- nodejs https://nodejs.org/en/
- npm https://www.npmjs.com/
- Vue CLI https://cli.vuejs.org/

# To build it
./mvnw install

# To run server

You should run jar which was build in target from server repository. 
'''
cd server
/usr/bin/java -jar ./target/server-0.0.1-SNAPSHOT.jar 
'''

Once, it was started, you can connect on it on:
'''
http://localhost:8080
'''

The server use embedded Apache Derby to store data. It will store data in mydb directory.

The server is ready, you could connect with user admin//admin. To create the first place.
If you register a new user or invit an existing user in house without specify configuration. You will found validation url in execution log. 

To receive confirming mail, you should specify configuration.json in directory server :
'''
{
	"publicUrl": "http://yourdomain",
	"smtpServer": "smtp.example.com",
	"smtpPort": 587,
	"smtpTls": true,
	"smtpLogin": "loginName@mail.com",
	"smtpPassword": "secret", 
	"senderEmail": "senderMail@mail.com"
}
''' 


# To run server in development mode
Run this from server repository
'''
../mvnw spring-boot:run
'''

# To build front
Run this command from front repository
'''
npm run-script build
'''

# To run front test
Run this command from front repository
'''
npm run-script test
'''