FROM openshift/base-centos7
MAINTAINER Chengchang Wang <chengchang.wang@vipshop.com>

EXPOSE 8587

ENV IMAGE_VERSION 1.0

LABEL io.k8s.description="Virtual Vehicle Authentication Service" \
      io.k8s.display-name="Authentication" \
      io.openshift.expose-services="8587:http" \
      io.openshift.tags="builder,service,authentication" \
      io.openshift.s2i.destination="/opt/s2i/destination"
    
ADD Authentication  /apps/
#ADD Vehicle /apps/
#ADD Maintenance /apps/
#ADD Valet /apps/
RUN chmod 755 /apps/startAuth.sh && \
    chmod -R 755 /apps && \
	echo "mongodb.uri = mongodb://$MONGODB_SERVICE_HOST:$MONGODB_SERVICE_PORT/virtual_authentication" >> /apps/config/dev/environment.properties
	
WORKDIR /apps
ENTRYPOINT ["/bin/bash", "./startAuth.sh"]
