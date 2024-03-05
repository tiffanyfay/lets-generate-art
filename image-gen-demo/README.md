# Image Gen Demo

**Table of Contents**
- [Image Gen Demo](#image-gen-demo)
  - [Prerequisites](#prerequisites)
  - [Infrastructure](#infrastructure)
    - [Set up DigitalOcean Infrastructure](#set-up-digitalocean-infrastructure)
  - [Applications](#applications)
    - [Database](#database)
    - [Launcher](#launcher)
    - [Image Generator and Database store](#image-generator-and-database-store)
  - [Things to do](#things-to-do)
  - [Resources](#resources)
    - [RBAC](#rbac)
  - [TODO](#todo)


## Prerequisites
Have the following CLIs:
- doctl
- docker
- kubectl
- jq

TODO: add kubens to not need `-n gen`

You also need some sort of OpenAI API access. There's things like OpenAI, Azure OpenAI, and SpringAI (this might need to use Spring code though--need to research).

In my case, I'm using OpenAI and generated an API key.

TODO: add docs link

## Infrastructure
We will need a:
- Kubernetes cluster
- Container registry
- PostgreSQL database

### Set up DigitalOcean Infrastructure
Clone https://github.com/hivenetes/k8s-bootstrapper

Follow https://github.com/hivenetes/k8s-bootstrapper/blob/main/infrastructure/terraform/README.md

Enable the database in bootstrapper.tfvars. You can also enable the container registry if you want to use it instead of Docker Hub.

## Applications

### Database
This is a Spring application that creates a database table to store 


### Launcher
This application takes in prompts for OpenAI and launches jobs for each prompt.


### Image Generator and Database store


## Things to do
TODO: Create gen namespace 

1. Readme for database

2. Readme for image-gen-store

3. Readme for launcher


## Resources


### RBAC
https://tanzu.vmware.com/developer/blog/beyond-cluster-admin/


## TODO
[ ] Use DOCR
[ ] Display prompts and URLs as images on webpage
[ ] Create service account vs using default one
[ ] Have a webpage to put prompts into vs running a pod
[ ] Add image creation dates so the database can drop anything that has a timestamp before a current subset -- e.g. OpenAI Dall-E only stores images for an hour, so after this, the URLs don't work.
