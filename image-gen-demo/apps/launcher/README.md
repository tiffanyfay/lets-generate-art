First we need RBAC permissions to be able to create jobs:

```console
kubectl apply -f k8s/role-create-jobs.yaml
kubectl apply -f k8s/rolebinding-create-jobs.yaml
```

Run the app:

```console
kubectl run launcher --image=tiffanyfay/launcher:mar2-817p --restart=OnFailure -- "elephants" "some lizards"
```

Check your annotations happened:
```
kubectl get jobs --output=jsonpath='{range .items[*]}{.metadata.name}: {.metadata.annotations}{"\n"}{end}'
```