#!/bin/sh

number_of_cores=$(getconf _NPROCESSORS_ONLN)

export SPARK_MASTER_OPTS="-Dspark.deploy.defaultCores=$((number_of_cores/4))"

/opt/spark/sbin/start-master.sh | \
/opt/spark/sbin/start-connect-server.sh \
  --master spark://$(hostname):7077 \
  --conf "spark.connect.extensions.relation.classes=org.apache.spark.sql.connect.delta.DeltaRelationPlugin" \
  --conf "spark.connect.extensions.command.classes=org.apache.spark.sql.connect.delta.DeltaCommandPlugin" \
  --conf "spark.cores.max=4" \
  --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
  --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog" | \
/opt/spark/sbin/start-thriftserver.sh \
  --master spark://$(hostname):7077 \
  --conf "spark.cores.max=4" \
  --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
  --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog"
