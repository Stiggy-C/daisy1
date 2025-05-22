FROM apache/spark:4.0.0-preview2

USER root

# Add necessary Linux packages
# Update packages list
RUN apt-get update && apt-get upgrade -y

# Install necessary dependencies
RUN apt-get install liblz4-1 && apt-get install liblzf1 && apt-get install libzstd1

# Install dependencies for debug purposes:
RUN apt-get -y install telnet

# Add AWS packages:
ADD --chown=spark https://repo1.maven.org/maven2/software/amazon/awssdk/bundle/2.24.6/bundle-2.24.6.jar /opt/spark/jars/bundle-2.24.6.jar

# Add Delta (Lake) packages:
ADD --chown=spark https://repo1.maven.org/maven2/com/google/api/grpc/proto-google-common-protos/2.29.0/proto-google-common-protos-2.29.0.jar /opt/spark/jars/proto-google-common-protos-2.29.0.jar
ADD --chown=spark https://repo1.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar /opt/spark/jars/jsr305-3.0.2.jar
ADD --chown=spark https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/3.25.5/protobuf-java-3.25.5.jar /opt/spark/jars/protobuf-java-3.25.5.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/delta/delta-connect-common_2.13/4.0.0rc1/delta-connect-common_2.13-4.0.0rc1.jar /opt/spark/jars/delta-connect-common_2.13-4.0.0rc1.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/delta/delta-connect-server_2.13/4.0.0rc1/delta-connect-server_2.13-4.0.0rc1.jar /opt/spark/jars/delta-connect-server_2.13-4.0.0rc1.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/delta/delta-spark_2.13/4.0.0rc1/delta-spark_2.13-4.0.0rc1.jar /opt/spark/jars/delta-spark_2.13-4.0.0rc1.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/delta/delta-storage/4.0.0rc1/delta-storage-4.0.0rc1.jar /opt/spark/jars/delta-storage-4.0.0rc1.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/grpc/grpc-api/1.62.2/grpc-api-1.62.2.jar /opt/spark/jars/grpc-api-1.62.2.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/grpc/grpc-protobuf/1.62.2/grpc-protobuf-1.62.2.jar /opt/spark/jars/grpc-protobuf-1.62.2.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/grpc/grpc-protobuf-lite/1.62.2/grpc-protobuf-lite-1.62.2.jar /opt/spark/jars/grpc-protobuf-lite-1.62.2.jar
ADD --chown=spark https://repo1.maven.org/maven2/io/grpc/grpc-stub/1.62.2/grpc-stub-1.62.2.jar /opt/spark/jars/grpc-stub-1.62.2.jar
ADD --chown=spark https://repo1.maven.org/maven2/javax/annotation/javax.annotation-api/1.3.2/javax.annotation-api-1.3.2.jar /opt/spark/jars/javax.annotation-api-1.3.2.jar

# Add Hadoop packages:
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.4.1/hadoop-aws-3.4.1.jar /opt/spark/jars/hadoop-aws-3.4.1.jar

# Add Kafka packages:
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.12.0/commons-pool2-2.12.0.jar /opt/spark/jars/commons-pool2-2.12.0.jar
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/kafka/kafka-clients/3.8.0/kafka-clients-3.8.0.jar /opt/spark/jars/kafka-clients-3.8.0.jar

# Add Spark packages:
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/spark/spark-connect_2.13/4.0.0-preview2/spark-connect_2.13-4.0.0-preview2.jar /opt/spark/jars/spark-connect_2.13-4.0.0-preview2.jar
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/spark/spark-sql-kafka-0-10_2.13/4.0.0-preview2/spark-sql-kafka-0-10_2.13-4.0.0-preview2.jar /opt/spark/jars/spark-sql-kafka-0-10_2.13-4.0.0-preview2.jar
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/spark/spark-tags_2.13/4.0.0-preview2/spark-tags_2.13-4.0.0-preview2.jar /opt/spark/jars/spark-tags_2.13-4.0.0-preview2.jar
ADD --chown=spark https://repo1.maven.org/maven2/org/apache/spark/spark-token-provider-kafka-0-10_2.13/4.0.0-preview2/spark-token-provider-kafka-0-10_2.13-4.0.0-preview2.jar /opt/spark/jars/spark-token-provider-kafka-0-10_2.13-4.0.0-preview2.jar
ADD --chown=spark https://repo1.maven.org/maven2/org/spark-project/spark/unused/1.0.0/unused-1.0.0.jar /opt/spark/jars/unused-1.0.0.jar

# Add Spark related environment variables
ENV _JAVA_OPTIONS="--add-opens=java.base/java.nio=org.apache.arrow.memory.core,ALL-UNNAMED"
ENV SPARK_NO_DAEMONIZE=true

# Replace OpenJDK with GraalVM
ENV JAVA_VERSION=21.0.2+13.1

RUN rm -rf /opt/java/openjdk
RUN apt-get install -y curl
RUN curl -LJO https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-21.0.2/graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz
RUN tar -xvzf graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz
RUN mv graalvm-community-openjdk-21.0.2+13.1 /opt/java/openjdk
RUN apt-get autoremove --purge -y curl
RUN rm graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz

# Last bit of heavy lifting
RUN mkdir /home/spark
RUN chown spark:spark /home/spark

COPY --chown=spark --chmod=500 ./start-master-only.sh /home/spark/start-master-only.sh
COPY --chown=spark --chmod=500 ./start-single-node-cluster.sh /home/spark/start-single-node-cluster.sh
COPY --chown=spark --chmod=500 ./start-worker-only.sh /home/spark/start-worker-only.sh

# Start a Spark single node standalone cluster
USER spark

CMD /home/spark/start-single-node-cluster.sh