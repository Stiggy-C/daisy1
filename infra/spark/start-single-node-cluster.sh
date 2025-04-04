#!/bin/sh

/opt/spark/sbin/start-master.sh | \
/opt/spark/sbin/start-worker.sh spark://$(hostname):7077 | \
/opt/spark/sbin/start-connect-server.sh \
  --packages org.apache.spark:spark-connect_2.13:4.0.0-preview2,io.delta:delta-connect-server_2.13:4.0.0rc1,io.delta:delta-connect-common_2.13:4.0.0rc1,com.google.protobuf:protobuf-java:3.25.5 \
  --master spark://$(hostname):7077 \
  --conf "spark.connect.extensions.relation.classes=org.apache.spark.sql.connect.delta.DeltaRelationPlugin" \
  --conf "spark.connect.extensions.command.classes=org.apache.spark.sql.connect.delta.DeltaCommandPlugin" \
  --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
  --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog" | \
/opt/spark/sbin/start-thriftserver.sh \
  --master spark://$(hostname):7077 \
  --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
  --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"