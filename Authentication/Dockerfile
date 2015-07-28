FROM openshift/base-centos7
MAINTAINER Chengchang Wang <chengchang.wang@vipshop.com>

EXPOSE 8587

ENV IMAGE_VERSION 1.0

LABEL io.k8s.description="Platform for building and running JEE applications on WildFly 8.1" \
      io.k8s.display-name="WildFly 8.1" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,wildfly,wildfly81" \
      io.openshift.s2i.destination="/opt/s2i/destination"
      
RUN yum install -y --enablerepo=centosplus \
    tar unzip bc which lsof java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
    yum clean all -y && \
    (curl -0 http://mirror.sdunix.com/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz | \
    tar -zx -C /usr/local) && \
    ln -sf /usr/local/apache-maven-3.0.5/bin/mvn /usr/local/bin/mvn && \
    mkdir -p /wildfly && \
    (curl -0 http://download.jboss.org/wildfly/8.1.0.Final/wildfly-8.1.0.Final.tar.gz | tar -zx --strip-components=1 -C /wildfly) && \
    mkdir -p /opt/app-root/source && \
    mkdir -p /opt/s2i/destination
    
ADD ./src/    /root/apps/src/
ADD ./config/ /root/apps/config/
ADD ./pom.xml /root/apps/pom.xml
ADD ./zip-with-dependencies.xml /root/apps/zip-with-dependencies.xml
ADD ./startAuth.sh /usr/local/bin/startAuth.sh

USER 1001

ENTRYPOINT ["/usr/local/bin/startAuth"]
