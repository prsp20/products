# products
A simple application created as the part of technical test from a company.

This application calls a product service to fetch product data and returns list
of products having reduced price sorted by price reduction.

### How to use this application
- Install Java 8 or above
- Clone the source code from this repository and go inside the root folder from terminal/command line
- Run ```./mvnw clean verify```
- After the build is successful go inside the folder target. You can do it by typing this command ```cd target```
- Then ```java -jar products-0.0.1-SNAPSHOT.jar``` or if you have changed the project version, corresponding
  jar file.  Before running this step you will need to make sure that the port 8000 is unallocated in your computer.
- This will start the application
- Visit ```http://localhost:8000/v1/products```

**Note!** This application was developed in a shortest time possible without considering all scenarios 
and edge cases. It was developed just to give a rough idea of how my code structure would look
like. That said, it does not have an extensive test coverage covering all edge cases. Also,
code was not reviewed by anyone - neither by myself taking long time. So please expect
some errors and typos here and there.
