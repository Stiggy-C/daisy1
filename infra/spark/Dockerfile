FROM apache/spark:4.0.0-preview2-scala2.13-java21-python3-r-ubuntu

USER root

# Update packages list
RUN apt-get update && apt-get upgrade -y

# Install necessary dependencies
RUN apt-get install liblz4-1 && apt-get install liblzf1 && apt-get install libzstd1

# Install dependencies for debug purposes:
RUN apt-get -y install telnet

# Replace OpenJDK with GraalVM
ENV JAVA_VERSION=21.0.2+13.1

RUN rm -rf /opt/java/openjdk
RUN apt-get install -y curl
RUN curl -LJO https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-21.0.2/graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz
RUN tar -xvzf graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz
RUN mv graalvm-community-openjdk-21.0.2+13.1 /opt/java/openjdk
RUN apt-get autoremove --purge -y curl
RUN rm graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz

# Add Spark related environment variables
ENV _JAVA_OPTIONS="--add-opens=java.base/java.nio=org.apache.arrow.memory.core,ALL-UNNAMED"
ENV SPARK_NO_DAEMONIZE=true

# Last bit of heavy lifting
RUN mkdir /home/spark
RUN chown spark:spark /home/spark

COPY --chown=spark --chmod=500 ./start-master-only.sh /home/spark/start-master-only.sh
COPY --chown=spark --chmod=500 ./start-single-node-cluster.sh /home/spark/start-single-node-cluster.sh
COPY --chown=spark --chmod=500 ./start-worker-only.sh /home/spark/start-worker-only.sh

# Change to spark user
USER spark

CMD /home/spark/start-single-node-cluster.sh