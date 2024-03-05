#!/bin/bash

OPENAI_API_KEY="$(cat /etc/secret-volume/open-api-key)"
PROMPT=$1

# Check if prompt is empty
if [ -z "$PROMPT" ]; then
  echo "Prompt is empty. Please provide a prompt."
  exit 1
fi

# Make request to OpenAI to generate image based on prompt
URL=$(curl -s https://api.openai.com/v1/images/generations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
      "model": "dall-e-3",
      "prompt": "'"$PROMPT"'",
      "n": 1,
      "size": "1024x1024"
  }' \
  | jq -r '.data[0].url')

echo "Adding to database:"
echo "Prompt: $PROMPT"
echo "URL: $URL"

# Get the IP of the image-database service
IP=$(kubectl get services -n gen image-database \
  --output jsonpath='{.status.loadBalancer.ingress[0].ip}')

# If the IP is empty, fall back to localhost
# TODO: validate that localhost:8080 works
if [ -z "$IP" ]; then
  echo "Could not find IP for image-database service, falling back to localhost:8080"
  IP="localhost"
fi

# Add prompt and URL to database
curl -s -v -X POST $IP:8080/images \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "'"$PROMPT"'", 
    "url": "'"$URL"'"
  }'

