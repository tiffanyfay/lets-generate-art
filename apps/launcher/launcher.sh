#!/bin/sh

for prompt in "$@"; do

    echo "Creating job for prompt: $prompt"
    export AI_PROMPT=$prompt
    # Create job name with a random random string
    export RANDOM_STRING=$(cat /dev/urandom | LC_ALL=C tr -dc 'a-z0-9' | head -c 5)

    # Get the IP of the image-database service
    # TODO: add check that you have permissions to get services
    IP=$(kubectl get services -n gen image-database \
      --output jsonpath='{.status.loadBalancer.ingress[0].ip}')

    # If the IP is empty, fall back to localhost
    if [ -z "$IP" ]; then
      echo "Could not find IP for image-database service, falling back to localhost:8080"
      IP="localhost:8080"
      # Check if localhost:8080 is reachable
      if ! curl -s -o /dev/null -w "%{http_code}" $IP; then
        echo "$IP is not reachable"
        exit 1
      fi
    fi

    export DB_SERVICE_URL="http://$IP:8080/images"
    echo "Database service URL: $DB_SERVICE_URL"

    # TODO: check if DB_SERVICE_URL is reachable

    # Create job
#    envsubst < k8s/job-image-gen-store.yaml
    envsubst < k8s/job-image-gen-store.yaml | kubectl apply -f -
done