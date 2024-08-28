#!/bin/sh

for prompt in "$@"; do

    echo "Creating job for prompt: $prompt\n"
    export AI_PROMPT=$prompt
    # Create job name with a random random string
    export RANDOM_STRING=$(cat /dev/urandom | LC_ALL=C tr -dc 'a-z0-9' | head -c 5)

    # Get the IP of the image-database service
    kubectl get services -n gen image-database \
          --output jsonpath='{.status.loadBalancer.ingress[0].ip}'

    IP=$(kubectl get services -n gen image-database \
      --output jsonpath='{.status.loadBalancer.ingress[0].ip}')

    # If the IP is empty, fall back to localhost
    if [ -z "$IP" ]; then
      echo "Could not find IP for image-database service, falling back to localhost:8080\n"
      IP="localhost:8080"
      # Check if localhost:8080 is reachable
      if ! curl -s -o /dev/null -w "%{http_code}" $IP; then
        echo "$IP is not reachable\n"
        exit 1
      fi
    fi

    export DB_SERVICE_URL="http://$IP:8080/images"
    echo "Database service URL: $DB_SERVICE_URL\n"

    # TODO: check if DB_SERVICE_URL is reachable

    # Get the OpenAI API key
    # TODO: Replace this with a secret
    export OPENAI_API_KEY=$(kubectl get secret openai-api-key -o jsonpath="{.data.key}" | base64 -d)

    # Create job
#    envsubst < k8s/job-image-gen-store.yaml
    envsubst < k8s/job-image-gen-store.yaml | kubectl apply -f -
done