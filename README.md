
# keycloak-springboot+react And docker

## DEMO [YOUTUBE VIDEO](https://www.youtube.com/watch?v=4g_lcqMzzZw&feature=youtu.be).
This project is showing how we can integrate springboot application with Keycloak Server. Operations like creating user in kecyloak, Login a user, authenticating http api request, getting access token using credentials or refreshtoken, logout user. 

## Components Version ##
* Java 1.8
* Maven 3.x
* Springboot 2
* Keycloak 8.0.0
* react 16
* redux 4

## Pre-requisities
* Kyecloak Server is up and running
* Mysql Server is up and running
* Springboot server is up and running 
* cloudinary API configured correctly 

NOTE: dont forget to configure your keycloak configurations from localhost:8080 in my case . all keycloak configurations will be persisted in the database .

## Change in application.properties
```
application.properties file :-
spring.datasource.url= <Replace with your mysql url and database name>

keycloak.realm = <Replace with your realm name>
keycloak.auth-server-url = http://127.0.0.1:8080/auth  <Remain same>
keycloak.resource = service <Replace with created client name>

#replace secret with your key
keycloak.credentials.secret =XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  <Replace with created secret key>
keycloak.bearer-only = true
cloudinary.api_key=423126184268163
cloudinary.api_secret=oUDNz6XyhFQunCqm206itr1I13g

cloudinary.cloud_name=<replace with your application name>

```
## Running application
run from below from command line
```
1-docker-compose up (it will run keycloak image and mysql image)
2-run the springboot server from your editor because i didnot create docker image for it :P 
3-npm start 
```

