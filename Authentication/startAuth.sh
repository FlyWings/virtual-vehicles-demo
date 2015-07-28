## get the mongodb service host and port information using envrionement 
echo "mongodb.uri = mongodb://$MONGODB_SERVICE_HOST:$MONGODB_SERVICE_PORT/virtual_authentication" >> /root/apps/config/dev/environment.properties

## start mvn build and the application
cd /root/apps/
mvn clean package exec:java