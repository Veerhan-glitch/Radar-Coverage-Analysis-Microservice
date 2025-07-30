# setup

## create a db in psql radar_ecm_db
## username: issa  pasword:  admin

psql -U issa
CREATE DATABASE radar_ecm_db;



psql -U issa -d radar_ecm_db

# to drop
DROP DATABASE radar_ecm_db;

## build all services in terminal (in case of changes in any microservice, build it again)

cd Backend

cd AbsorptionLossService
mvn -s ../settings.xml -o clean install
cd ..
cd ECMModelService
mvn -s ../settings.xml -o clean install
cd ..
cd RadarModelService
mvn -s ../settings.xml -o clean install
cd ..
cd AnalysisManagerService
mvn -s ../settings.xml -o clean install
cd ..
cd AuthenticationService
mvn -s ../settings.xml -o clean install
cd ..

cd ..

## run frontend (hosted at http://127.0.0.1:8080)

# Terminal 1
cd Frontend
ng serve

## run all services in a new terminal for each

# Terminal 2
cd Backend/AbsorptionLossService
mvn spring-boot:run --offline --settings ../settings.xml

# Terminal 3
cd Backend/ECMModelService
mvn spring-boot:run --offline --settings ../settings.xml

# Terminal 4
cd Backend/RadarModelService
mvn spring-boot:run --offline --settings ../settings.xml

# Terminal 5
cd Backend/AnalysisManagerService
mvn spring-boot:run --offline --settings ../settings.xml

# Terminal 5
cd Backend/AuthenticationService
mvn spring-boot:run --offline --settings ../settings.xml

# test endpoints using Postman or curl at:
# http://localhost:8081/api/absorption/calculate
# http://localhost:8082/api/ecm/calculate
# http://localhost:8083/api/radar/calculate
# http://localhost:8084/api/analysis/request
# http://localhost:8085/api/auth/login
# http://localhost:8085/api/auth/register

# in case of any port is occupied error, go to application.properties for that service and change the port to any available port (other than 8081 to 8085). For example, for AbsorptionLossService:
# nano AbsorptionLossService/src/main/resources/application.properties
# Change server.port=8081 to server.port=8091
# Save and restart: mvn spring-boot:run

# to go offline used 

cd Frontend
npm init -y
npm install http-server --save-dev

and downloded stuff in libs folder of frontend


cd Backend

cd AbsorptionLossService
mvn -s ../settings.xml dependency:resolve dependency:resolve-plugins dependency:go-offline
mvn -s ../settings.xml dependency:go-offline dependency:resolve dependency:resolve-plugins org.apache.maven.plugins:maven-clean-plugin:3.2.0:help org.apache.maven.plugins:maven-compiler-plugin:3.11.0:help
cd ..
cd ECMModelService
mvn -s ../settings.xml dependency:resolve dependency:resolve-plugins dependency:go-offline
mvn -s ../settings.xml dependency:go-offline dependency:resolve dependency:resolve-plugins org.apache.maven.plugins:maven-clean-plugin:3.2.0:help org.apache.maven.plugins:maven-compiler-plugin:3.11.0:help
cd ..
cd RadarModelService
mvn -s ../settings.xml dependency:resolve dependency:resolve-plugins dependency:go-offline
mvn -s ../settings.xml dependency:go-offline dependency:resolve dependency:resolve-plugins org.apache.maven.plugins:maven-clean-plugin:3.2.0:help org.apache.maven.plugins:maven-compiler-plugin:3.11.0:help
cd ..
cd AnalysisManagerService
mvn -s ../settings.xml dependency:resolve dependency:resolve-plugins dependency:go-offline
mvn -s ../settings.xml dependency:go-offline dependency:resolve dependency:resolve-plugins org.apache.maven.plugins:maven-clean-plugin:3.2.0:help org.apache.maven.plugins:maven-compiler-plugin:3.11.0:help
cd ..
cd AuthenticationService
mvn -s ../settings.xml dependency:resolve dependency:resolve-plugins dependency:go-offline
mvn -s ../settings.xml dependency:go-offline dependency:resolve dependency:resolve-plugins org.apache.maven.plugins:maven-clean-plugin:3.2.0:help org.apache.maven.plugins:maven-compiler-plugin:3.11.0:help
cd ..

cd ..






?host=/var/run/issaql
