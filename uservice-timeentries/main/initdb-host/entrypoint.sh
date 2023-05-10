MVN_OPTIONS="-Dmaven.repo.local=/.m2/repository --quiet"
./mvnw $MVN_OPTIONS spring-boot:run -pl initdb -am
