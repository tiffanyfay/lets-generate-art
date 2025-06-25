#!/usr/bin/env python3

import sys
import os
import subprocess
import random
import string
import requests
from pathlib import Path

def generate_random_string(length=5):
    """Generate a random string of lowercase letters and numbers."""
    return ''.join(random.choices(string.ascii_lowercase + string.digits, k=length))

def get_image_database_ip():
    """Get the IP of the image-database service from Kubernetes."""
    try:
        # Run kubectl command to get service IP
        result = subprocess.run([
            'kubectl', 'get', 'services', '-n', 'gen', 'image-database',
            '--output', 'jsonpath={.status.loadBalancer.ingress[0].ip}'
        ], capture_output=True, text=True, check=True)
        
        ip = result.stdout.strip()
        return ip if ip else None
    except subprocess.CalledProcessError:
        print("Could not get service IP from kubectl")
        return None

def check_url_reachable(url):
    """Check if a URL is reachable."""
    try:
        response = requests.get(url, timeout=5)
        return response.status_code < 400
    except requests.RequestException:
        return False

def apply_kubernetes_job(yaml_file):
    """Apply Kubernetes job using envsubst and kubectl."""
    try:
        # Use envsubst to substitute environment variables
        envsubst_process = subprocess.Popen(['envsubst'], 
                                          stdin=subprocess.PIPE, 
                                          stdout=subprocess.PIPE, 
                                          text=True)
        
        # Read the YAML file
        with open(yaml_file, 'r') as f:
            yaml_content = f.read()
        
        # Substitute environment variables
        substituted_yaml, _ = envsubst_process.communicate(input=yaml_content)
        
        if envsubst_process.returncode != 0:
            print(f"Error running envsubst")
            return False
        
        # Apply with kubectl
        kubectl_process = subprocess.Popen(['kubectl', 'apply', '-f', '-'],
                                         stdin=subprocess.PIPE,
                                         text=True)
        
        kubectl_process.communicate(input=substituted_yaml)
        
        return kubectl_process.returncode == 0
        
    except Exception as e:
        print(f"Error applying Kubernetes job: {e}")
        return False

def main():
    """Main function to process prompts and create jobs."""
    if len(sys.argv) < 2:
        print("Usage: python launcher.py <prompt1> [prompt2] ...")
        sys.exit(1)
    
    prompts = sys.argv[1:]
    
    for prompt in prompts:
        print(f"Creating job for prompt: {prompt}")
        
        # Set environment variables
        os.environ['AI_PROMPT'] = prompt
        os.environ['RANDOM_STRING'] = generate_random_string()
        
        # Get the IP of the image-database service
        ip = get_image_database_ip()
        
        # If the IP is empty, fall back to localhost
        if not ip:
            print("Could not find IP for image-database service, falling back to localhost:8080")
            ip = "localhost"
            
            # Check if localhost:8080 is reachable
            if not check_url_reachable("http://localhost:8080"):
                print("localhost:8080 is not reachable")
                sys.exit(1)
        
        # Set database service URL
        db_service_url = f"http://{ip}:8080/images"
        os.environ['DB_SERVICE_URL'] = db_service_url
        print(f"Database service URL: {db_service_url}")
        
        # TODO: check if DB_SERVICE_URL is reachable
        
        # Apply Kubernetes job
        yaml_file = Path("k8s/job-image-gen-store.yaml")
        if not yaml_file.exists():
            print(f"YAML file {yaml_file} not found")
            sys.exit(1)
        
        if not apply_kubernetes_job(yaml_file):
            print("Failed to apply Kubernetes job")
            sys.exit(1)
        
        print(f"Successfully created job for prompt: {prompt}")

if __name__ == "__main__":
    main() 