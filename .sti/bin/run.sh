cd /opt/wildfly/source/Authentication

echo "mongodb.uri = mongodb://$MONGODB_SERVICE_HOST:$MONGODB_SERVICE_PORT/virtual_authentication" >> config/dev/environment.properties

mvn clean package

java -jar target/Authentication-1.0-SNAPSHOT.jar
