1. Start docker-compose up **docker-compose up** with the lastes version to run just dockerized database server
2. If you would like to remove images and start from scratch, stop **docker-compose down** and start again
3. Play individual commands like:
   1. **mvn -pl initdb liquibase:help**
   2. **mvn -pl initdb liquibase:rollback -Dliquibase.rollbackCount=1** do rollback one change
   3. **mvn -pl initdb liquibase:update** to aply all chanegs
   4. **mvn -pl initdb liquibase:rollback -Dliquibase.rollbackTag=v0** do rollback to empty tables
34. Or, test script manually **docker-compose up --build** to  run database and migrations
