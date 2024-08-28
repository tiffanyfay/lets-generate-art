# Image Generator and Storer

**Table of Contents**
- [Image Generator and Storer](#image-generator-and-storer)
- [Build and push image](#build-and-push-image)
  - [OpenAPI](#openapi)

```shell
cd ../image-gen-store
```

# OpenAI API Key

Create an OpenAI API key

Base64 encode your key:
```shell
echo -n '<open-api-key>' | base64
```

Copy the file so you don't accidentally commit it up.
```console
cp k8s/secret-open-api-key.yaml k8s/my-secret-open-api-key.yaml
```
Edit `k8s/my-secret-open-api-key.yaml` and put this key inside as your `open-api-key`.

Create a secret with your key:
```console
kubectl apply -f k8s/my-secret-open-api-key.yaml
```

# Build and push image
```console
# Change image to your image name depending on your registry
export IMAGE_GEN_STORE_IMAGE=<image-gen-store:v1>
```

## Bash
```console
docker build -t $IMAGE_GEN_STORE_IMAGE .
```

## Java
```console
# Replace image with your image name
./mvnw spring-boot:build-image \
    -Dspring-boot.build-image.imageName=$IMAGE_GEN_STORE_IMAGE \
    -DskipTests
```

## Push App to registry
This is only needed if the image isn't automatically pushed up.

```shell
docker push $IMAGE_DATABASE_IMAGE
```


[**Next steps ->**](../launcher/README.md)