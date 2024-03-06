# Launcher

**Table of Contents**
- [Launcher](#launcher)
- [Build and push image](#build-and-push-image)
  - [RBAC](#rbac)
  - [Resources](#resources)
    - [RBAC](#rbac-1)


# Build and push image
```shell
# Change image to your image name depending on your registry
export IMAGE_GEN_STORE_LAUNCHER_IMAGE=<image-gen-store-launcher:v1>
docker build -t $IMAGE_GEN_STORE_LAUNCHER_IMAGE .
docker push $IMAGE_GEN_STORE_LAUNCHER_IMAGE
```

## RBAC
First we need RBAC permissions to be able to create jobs so we will create a role and rolebinding.
TODO: explain what these do, and also show how we can't get jobs or services prior

TODO: create a service account too vs using the default

```console
kubectl apply -f k8s/role-create-jobs.yaml
kubectl apply -f k8s/rolebinding-create-jobs.yaml
```

TODO: about getting the loadbalancer svc for the db
```
kubectl apply -f k8s/role-get-services.yaml
kubectl apply -f k8s/rolebinding-get-services.yaml
```

TODO: solve warning `warning: couldn't attach to pod/tester, falling back to streaming logs: unable to upgrade connection: container tester not found in pod tester_gen`

Verify you now have permissions to see jobs in the gen namespace:
```console
kubectl -n gen run -it tester --rm --image=cgr.dev/chainguard/kubectl -- get jobs
```

Run the app:
```shell
kubectl -n gen run image-gen-store-launcher \
    --image=$IMAGE_GEN_STORE_LAUNCHER_IMAGE \
    --restart=OnFailure \
    --env IMAGE_GEN_STORE_IMAGE=$IMAGE_GEN_STORE_IMAGE \
    -- "baby raccoons" "snow in the mountains"
```

Look at your jobs:
```console
kubectl -n gen get jobs
```

Check your annotations happened:
```console
kubectl -n get jobs --output=jsonpath='{range .items[*]}{.metadata.name}: {.metadata.annotations}{"\n"}{end}'
```

## Resources
### RBAC
[Talk I gave](https://www.youtube.com/watch?v=mD-Dng2QbQ0&ab_channel=DevoxxFR)
