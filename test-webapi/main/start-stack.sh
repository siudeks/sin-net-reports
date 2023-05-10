# IMPORTANT
# In local development, all application should be configured to use same configration values as defined here
# In other words, the file in the golder source of configuration parameters to run application as local stack

cwd=$(pwd)
export LOG_LEVEL=warn
export DATABASE_NAME=devlocaldb
export DAPR_PORT_HTTP=3500
export DATABASE_JDBC=jdbc:postgresql://localhost:5432/devlocaldb

# Base dev ports:
# http: 11010
# grpc: N/A
# dapr sidecar from vscode: 11019
cd ../../uservice-webapi/main
dapr --app-id webapi \
--components-path ../../.components run \
--app-port 11010 \
-- \
mvn quarkus:dev -pl host &
P1=$!



# Base dev ports:
# http: 11020
# grpc: 11021
# dapr sidecar from vscode: 11029
cd ../../uservice-timeentries/main
# rebuild dependant modules before starting spring boot
# mvn -Dmaven.test.skip=true install -pl host -am
export DATABASE_USERNAME=uservice_timeentries_role_name && \
export DATABASE_PASSWORD=uservice_timeentries_role_password && \
export DATABASE_SCHEMA=uservice_timeentries && \
export SPRING_PROFILES_ACTIVE=dev && \
dapr --app-id activities-app-id \
--components-path ../../.components \
run \
--app-port 11021 --app-protocol grpc \
-- \
mvn spring-boot:run -pl host -am &
P2=$!


# Base dev ports:
# http: 11030
# grpc: 11031
# debug: 11039
# cd ../../uservice-projects/main
# export DATABASE_NAME=devlocaldb && export DATABASE_SCHEMA=uservice_projects &&  \
# dapr --app-id projects \
# --components-path ../../.components run \
# --app-port 11031 --app-protocol grpc \
# -- \
# mvn quarkus:dev -pl host -Ddebug=11039 &
# P3=$!


cd $cwd
wait $P1 $P2 $P3
