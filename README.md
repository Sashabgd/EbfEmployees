# EbfEmployees

## About

EbfEmployees is microservice that is part of the existing environment.

It is composed of two applications, backend and frontend.

Backend application is built on spring boot 2.6.1, and front application is built on angular 13.1.0.

There is a third backend application that is a mock auth server. 

## Flow

![EBF FLOW](https://github.com/Sashabgd/EbfEmployees/raw/main/ebf_employee_flow.png)

EbfEmployee front application depends on auth server and EbfEmployee web api server.

When EbfEmployee got error response 401 or 403 from web api server, it will try to fetch JWT access token from auth server.

Then in will inject that access token in Authorization header, and it will retry the request. Below is an example of the Authorization header:

```Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJST0xFIjoiYWRtaW4iLCJzdWIiOiJhZG1pbiIsImV4cCI6MTY0MDg2ODM0M30.9ZQ_8iqOxp1IM7NDIJQc9Oj0-Rz4QN9lV3_ZEQNsbFr2bms10VADrQWMHq7Bv3cnnzMpNwCsboUEpP2maEOtwg```

After EbfEmployee receive request, it will look for Authorization header, and if header is present then it will try to validate access token signature
from Authorization header.

If signature is valid, it will fetch subject and roles from the jwt token, and then it will
authenticate request with that subject and roles.

If authorization requirements are satisfied for target endpoint, it will proceed with CRUD operations on the database.

For more details about authentication and authorization see :

```com.itekako.EbfEmployee.auth.JwtFilter.java```
```com.itekako.EbfEmployee.configurations.WebSecurityConfiguration.java```



## Requirements

This project depends on:

* openjdk-11-jdk
* maven
* docker
* docker-compose
* nodejs
* angular
* postgres

### Install ubuntu 20.04


```
sudo apt install -y openjdk-11-jdk docker docker-compose maven postgresql-12
```

For nodejs installation follow next steps

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
source ~/.bashrc
nvm ls-remote
nvm install v16.13.1 
```

### Setup
First we need to setup database.
```
sudo -u postgres psql
postgres=# create user ebf_db_user with password 'ebf_db_password';
postgres=# create database ebf_employees owner ebf_db_user;
```

Right now we have everything ready and setup for backend app.

To setup front application we need to do following:
```
cd PROJECT_DIRECTORY/EbfEmployeesFront/
npm install --save @angular/material @angular/cdk
npm install
```

Now everything is setup and ready for development.

### Compile and run applications

To compile and run backend employee and auth application follow next steps:

Employee app:

```
cd PROJECT_DIRECTORY/EbfEmployees
mvn package
java -jar target/EbfEmployees-0.0.1-SNAPSHOT.jar
```

Just one notice, EbfEmployees-0.0.1-SNAPSHOT.jar depends on application version, and it can be changed.

To run auth application server do following:

```
cd PROJECT_DIRECTORY/EbfAuthServer/
mvn package
java -jar target/EbfAuthServer-0.0.1-SNAPSHOT.jar
```

To run front application do following:

```
cd PROJECT_DIRECTORY/EbfEmployeesFront/
npm start
```

### Usage

EbfEmployee service runes port http:8080.

EbfAuthServer runs on port http:8088

EbfEmployeesFront runs on default port http:4200

EbfEmployee service supports Swagger documentation and it's available on url : http://localhost:8080/swagger-ui.html


Auth server only provides one endpoint http-post on url http://localhost:8088/api/login

It accepts json body:

```json
{
  "username": "admin",
  "password": "adminpass"
}
```

The example of login body contains default username and password with role "admin"

Login server provides following users:

With admin role:

* username : admin
* password : adminpass

With user role:

* username : user
* password : userpass

Those credentials we can use to generate access token, that we can later use to access EbfEmployee protected resource.

### Run with docker

This project we can run with a docker.

To do this follow next steps:

```
cd PROJECT_DIRECTORY
docker-compose up
```

If you have permission error, please run following command

```
sudo usermod -aG docker <user>
```

Where ```<user>``` is your current user. After running this command please re-login to your system.

