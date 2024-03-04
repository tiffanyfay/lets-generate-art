#!/bin/sh

for prompt in "$@"; do
    
    echo "\nCreating job for prompt: $prompt"

    # Create job name with a random random string
    random_string=$(cat /dev/urandom | LC_ALL=C tr -dc 'a-z0-9' | head -c 5)
    job_name="image-gen-$random_string"
    
    # Create job
    kubectl create job $job_name --image=busybox

    # Add prompt to annotation
    kubectl annotate job $job_name prompt="$prompt"
done