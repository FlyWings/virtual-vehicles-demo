## get the mongodb service host and port information using envrionement 
echo "mongodb.uri = mongodb://admin:passw0rd@$MONGODB_SERVICE_HOST:$MONGODB_SERVICE_PORT/virtual_authentication" >> /apps/config/dev/environment.properties

## start mvn build and the application
cd /apps
mvn clean package
java -jar target/Authentication-1.0-SNAPSHOT.jar
