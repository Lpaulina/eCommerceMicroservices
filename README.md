# eCommerceMicroservices

## 🚀 Prerequisites

Before running the system, make sure you have installed:

Docker  
Docker Compose  
Java 21  
Maven

## ⚙️ Deployment

Clone the repository

`git clone https://github.com/Lpaulina/ecommerce-microservices.git`  
`cd eCommerceMicroservices`

Build the services

`mvn clean package jib:dockerBuild`

Start the system

`docker-compose up -d`

## KeyCloak
Go to http://localhost:8080/admin/master/console/#/ecommerce
Log in with username: admin, password: admin
Create a user

Set its password and set temporary to off

## Phase 3
Phase 3 of the project involved deploying the microservice to AWS

As of 12/05/2025 all of the resources in the branch have been deleted
and therefore were not merged into the main branch

However, this branch also demonstrates the utilization of tools like logstash
kibana, redis, etc, so feel free to pull down branch `phase3` to see their configuration
