# syntax=docker/dockerfile:experimental
FROM openjdk:17.0.2-jdk-slim-bullseye

USER root

WORKDIR /app

RUN echo "PS1='\[\033[01;32m\]quarkus-reactive-mongo-events-error@dev\[\033[00m\]:\[\033[01;34m\]\w\[\033[00m\] \$ '" >> /root/.bashrc
RUN echo "alias mvn=\"/app/mvnw -Dstyle.color=always\"" >> /root/.bashrc
RUN echo "alias mvnd=\"mvn -Dmaven.surefire.debug=\\\"-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=0.0.0.0:5005 -Xnoagent -Djava.compiler=NONE\\\"\"" >> /root/.bashrc
RUN echo "alias startServer=\"mvn compile quarkus:dev -DdebugHost=0.0.0.0\"" >> /root/.bashrc
RUN echo "export PATH=\"/project:$PATH\"" >> /root/.bashrc
