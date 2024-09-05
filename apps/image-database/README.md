# Image Database

Table of Contents
- [Image Database](#image-database)
  - [Building image-database app](#building-image-database-app)
    - [Cloud Native Buildpacks](#cloud-native-buildpacks)
    - [JIB](#jib)
  - [Push App to registry](#push-app-to-registry)
    - [Export PostgreSQL environment variables](#export-postgresql-environment-variables)
      - [With DigitalOcean Databases](#with-digitalocean-databases)
    - [Push image to your container registry](#push-image-to-your-container-registry)
    - [\[Optional\] Testing](#optional-testing)
    - [Taking a direct look into the database](#taking-a-direct-look-into-the-database)
      - [For DigitalOcean](#for-digitalocean)
  - [Resources](#resources)

  
## Building image-database app
```shell
cd apps/image-database
```

First, ensure you're logged into the Container Registry you expect to be in.

```shell
export IMAGE_DATABASE_IMAGE=<image-database:v1>
```

### Export PostgreSQL environment variables
If you're not using DigitalOcean, replace these with your values.
```shell
export CLICKHOUSE_HOST=<host>
export CLICKHOUSE_PORT=<5432>
export CLICKHOUSE_DATABASE=<defaultdb>
export CLICKHOUSE_USERNAME=<doadmin>
export CLICKHOUSE_PASSWORD=<password>
```

#### With DigitalOcean Databases
```shell
export DO_DATABASE_ID=<id-goes-here> 
```
```shell
export POSTGRES_HOST=$(doctl database get $DO_DATABASE_ID -o json | jq -r '.[0].connection.host')
echo $POSTGRES_HOST
export POSTGRES_PORT=25060
export POSTGRES_DATABASE=defaultdb
export POSTGRES_USERNAME=doadmin
export POSTGRES_PASSWORD=$(doctl database get $DO_DATABASE_ID -o json | jq -r '.[0].connection.password')
```
#### With ClickHouse Cloud
> Note: This requires making the database mydb
```shell
export CLICKHOUSE_HOST=<clickhouse-host> 
echo $CLICKHOUSE_HOST
export CLICKHOUSE_PORT=8443
export CLICKHOUSE_DATABASE=mydb
export CLICKHOUSE_USERNAME=<default>
export CLICKHOUSE_PASSWORD=<password>
```

### Cloud Native Buildpacks
Build and push image:
```console
# Replace image with your image name
./mvnw spring-boot:build-image \
    -Dspring-boot.build-image.imageName=$IMAGE_DATABASE_IMAGE \
    -DskipTests
```

### JIB
```console
./mvnw compile com.google.cloud.tools:jib-maven-plugin:3.3.2:dockerBuild \
    -Dimage=<image-database:v1>
```

## Push App to registry
This is only needed if the image isn't automatically pushed up.

```shell
docker push $IMAGE_DATABASE_IMAGE
```

Verify variables are filled in:
```shell
envsubst < k8s/deployment-image-database.yaml
```

*TODO: use secrets for password*

Create deployment and service:
```shell
envsubst < k8s/deployment-image-database.yaml | kubectl apply -f -
kubectl apply -f k8s/service-image-database.yaml
```

Look for loadbalancer service external IP:
```shell
kubectl -n gen get service image-database -w
```

For testing purposes, if you don't feel like waiting on the LoadBalancer (it's about 5 minutes on DigitalOcean), you can port-forward:
```shell
kubectl -n gen port-forward deployment/image-database 8080:8080
```

### [Optional] Testing
Test your containerized app works:
```shell
docker run -p 8080:8080 \
  -e CLICKHOUSE_HOST=$CLICKHOUSE_HOST \
  -e CLICKHOUSE_PORT=$CLICKHOUSE_PORT \
  -e CLICKHOUSE_DATABASE=$CLICKHOUSE_DATABASE \
  -e CLICKHOUSE_USERNAME=$CLICKHOUSE_USERNAME \
  -e CLICKHOUSE_PASSWORD=$CLICKHOUSE_PASSWORD \
  $IMAGE_DATABASE_IMAGE
```

### Taking a direct look into the database
TODO: give install instructions for `psql`

#### For DigitalOcean
```shell
PGPASSWORD=$CLICKHOUSE_PASSWORD psql -U doadmin -h $CLICKHOUSE_HOST -p 25060 -d defaultdb --set=sslmode=require

Get tables (there should be one called image_prompt):
```shell
\dt
```

See columns in image_prompt table:
```shell
\d image_prompt
```

See values in table:
```
SELECT * FROM image_prompt;
```

[**Next steps ->**](../image-gen-store/README.md)

## Resources
Spring Data
- https://spring.io/guides/gs/accessing-data-jpa
- https://spring.io/guides/gs/accessing-data-mysql/
- https://hackernoon.com/using-postgres-effectively-in-spring-boot-applications

DigitalOcean Managed PostgreSQL Database
- https://docs.digitalocean.com/products/databases/postgresql/