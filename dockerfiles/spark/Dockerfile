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

# Start a local cluster
USER spark

CMD \
/opt/spark/sbin/start-master.sh | \
/opt/spark/sbin/start-worker.sh spark://$(hostname):7077 | \
/opt/spark/sbin/start-connect-server.sh \
    --packages org.apache.spark:spark-connect_2.13:4.0.0-preview2,io.delta:delta-connect-server_2.13:4.0.0rc1,io.delta:delta-connect-common_2.13:4.0.0rc1,com.google.protobuf:protobuf-java:3.25.5 \
    --master spark://$(hostname):7077 \
    --conf "spark.connect.extensions.relation.classes=org.apache.spark.sql.connect.delta.DeltaRelationPlugin" \
    --conf "spark.connect.extensions.command.classes=org.apache.spark.sql.connect.delta.DeltaCommandPlugin" \
    --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
    --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog" | \
/opt/spark/sbin/start-thriftserver.sh