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