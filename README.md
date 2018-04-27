# Welcome to Line Server!

### How does your system work?

My system has the following components:

* Apache Cassandra NoSQL database.
  It stores text lines from file (each line is a separate record), with line index as a primary key

* Spring Boot-based Java application.
  It starts embedded instance of Tomcat server and handles HTTP GET requests for specific lines.

During application start up, it reads text file provided as the first command line argument and loads it into
Cassandra database.

**Note:** this loading step is optional, and is skipped if there are no command line arguments provided. In this case,
application will use data that was previously loaded in Cassandra database.

When processing HTTP request for specific line, application fetches the line from Cassandra database and returns it back to caller.

### What do we need to build your system?

1. Prerequisites

    * Download and install Apache Cassandra from: http://cassandra.apache.org/download/

      I was using the latest available release - 3.11.2

    * Execute the following CQL script using Cassandra "cqlsh" utility: **src/main/resources/keyspace.cql**

      For example:
      >cqlsh -f src/main/resources/keyspace.cql

      Note: cqlsh requires Python 2.7 installed

2. Building the system.

    * Default application settings assume that Cassandra is installed on the same machine as the application server.

      If you want to modify Cassandra connection settings, please modify this file: **src/main/resources/application.properties**

    * Run the following command to compile, unit test and jar the application:
      >build.sh clean install

      This script is using Maven internally. Some of unit tests are actually integration tests that verify end-to-end workflow. Such tests will start
      embedded instance of Cassandra - you will see this in logs.

3. Running the system.

    * To start application and load the file into Cassandra:
      >run.sh file_path

    * To start application without loading the file:
      >run.sh

2.4. Navigate to http://localhost:8080/lines/{line_index} for testing

### How will your system perform with a 1 GB file? a 10 GB file? a 100 GB file?

There are two aspects:

1. Initial file loading

   Loading time will grow linearly as the file size increase (though this is one-time hit at the server startup).
   I'd like to investigate it furter and explore possible solutions to optimize loading. See the question below.

2. Database scalability

   Access for the particular line is performed by the primary key and should take close to constant time.
   However we'll need to add more nodes to Cassandra cluster to support constant-time access on larger volumes of data.


### How will your system perform with 100 users? 10000 users? 1000000 users?

In the single server installation, response time will grow with the number of concurrent requests.
We'll need to start cluster of multiple instances of Tomcat to handle larger number of requests.

Also we may need to add more nodes to Cassandra cluster with replication factor greater than 1 (to have the same record present on multiple nodes).

### What documentation, websites, papers, etc did you consult in doing this assignment?

* Cassandra help: http://cassandra.apache.org/doc/latest/
* Several guides for Spring from: https://spring.io/
* Cassandra-unit help: https://github.com/jsevellec/cassandra-unit/wiki

### What third-party libraries or other tools does the system use? How did you choose each library or framework you used?

* Apache Cassandra

  I decided to use NoSQL database because application requires fast data access, but doesn't need to update the data
  (except initial load) and doesn't require DB transactions.
  I haven't performed detailed research of different NoSQL engines but I know that Apache Cassandra is highly rated
  for performance and scalability, and is widely used. Hence I decided to try it.

* Spring (Spring Core, Spring Boot, Spring Web, Spring Data Cassandra)

  This is widely used framework for rapid application development.
  I used it for dependency injection, rest services, database access.
  It helped me to eliminate most of boilerplate code and focus on the application logic.

* Cassandra-Unit (https://github.com/jsevellec/cassandra-unit)

  Embedded Cassandra for unit tests. I wanted to have some kind of integration tests developed to verify
  that everything works end-to-end. I found cassandra-unit project. It seems as widely used approach for mocking
  Cassandra database.


### How long did you spend on this exercise? If you had unlimited more time to spend on this, how would you spend it and how would you prioritize each item?

I've worked in small increments (about 2 hours a day). I spend around 8 hours total on this exercise.

If I had more time, I'd work on the following tasks (most important tasks first):

1. Run performance tests.

   Both for initial file loading, and HTTP requests processing.

2. Investigate how to do initial file load faster.

   I think that initial file load is currently a bottle-neck for large files. I'd like to explore this topic.
   Try adding more nodes in Cassandra cluster, see if there are any bulk loading tools, try to multi-thread loading process, etc.

3. Add LRU cache to the application to avoid fetching Cassandra on most-recently requested lines.

4. Cache **maximum line index** in the application to avoid fetching Cassandra if we know upfront that requested line is beyond file limit.

5. Allow specifying Cassandra connection properties as arguments to **run.sh** script.

6. Externalize error messages.

### If you were to critique your code, what would you have to say about it?

Reasonable prototype. Need to invest more time in performance and scalability testing.