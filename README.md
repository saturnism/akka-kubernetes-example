Introduction
============
This is an example code and configuration to deploy Akka in Kubernetes using Statefulset for Akka seed nodes, and Deployment for Akka worker nodes.

Special thanks to [Yohan](https://twitter.com/apnylle) and [Ola](https://twitter.com/gotoOla) for teaching me about Akka at JFokus 2017.

Prerequisite
============
You have a working Kubernetes 1.5 cluster. You can try [Minikube](https://github.com/kubernetes/minikube), or [Google Container Engine](https://cloud.google.com/container-engine/) for multi-node deployment.

Installation
============
1. Deploy the seed nodes: `kubectl apply -f kubernetes/akka-seed.yaml`
1. Deploy the worker nodes: `kubectll apply -f kubernetes/akka-workers.yaml`

Or, deploy everything in one shot: `kubectl apply -f kubernetes/

Example Usage
=============
* Scale seed nodes: `kubectl scale statefulset akka-seed --replicas=3`
* Scale worker nodes: `kubectl scale deployment akka-worker --replicas=4`
* See logs: `kubectl logs -f akka-seed-0`

