# Image Generator and Storer

**Table of Contents**
- [Image Generator and Storer](#image-generator-and-storer)
- [Build and push image](#build-and-push-image)
  - [OpenAPI](#openapi)


# Build and push image
```console
# Change image to your image name depending on your registry
export IMAGE_GEN_STORE_IMAGE=<image-gen-store:v1>
docker build -t $IMAGE_GEN_STORE_IMAGE .
docker push $IMAGE_GEN_STORE_IMAGE
```

## OpenAPI
Create an OpenAPI key

Base64 encode your key:
```console
echo -n '<open-api-key>' | base64
```

Edit `k8s/k8s/secret-open-api-key.yaml` and put this key inside as your `open-api-key`.

Create a secret with your key:
```console
kubectl apply -f k8s/secret-open-api-key.yaml
```