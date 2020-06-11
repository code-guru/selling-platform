# Selling-Platform
A Kotlin demo REST API, FTW! 

##Usage
```shell script
docker-compose up
```

##Highlights
- Stack: Kotlin, Ktor, Exposed, HikariCP, Postgres, Docker
- API designed based on best practices using HTTP verbs, and treating data as resources
- Validation of input from API request
- Database connection pooling using Hikari
- Using Exposed as ORM for communicating with DB
- Error handling and return errors with correct HTTP status code and message and standard JSON format

##Endpoint
|　　　　　　　　　　|   |   |
|---|---|---|
| Create a New User |POST | http://localhost:8181/users| 
| Get All Users | GET | http://localhost:8181/users| 
| Get User by Id |GET | http://localhost:8181/users/1| 
| Edit a User by Id |PUT | http://localhost:8181/users/1| 
| Delete a User by Id |DELETE | http://localhost:8181/users/1| 
| Create a New Ad |POST | http://localhost:8181/ads| 
| Get All Ads |GET | http://localhost:8181/ads| 
| Get Ad by Id |GET | http://localhost:8181/ads/1| 
| Get Ads using Filters |GET | http://localhost:8181/ads?category=kategori11&from=2019-01-01&to=2019-11-01| 
| Edit an Ad by Id |PUT | http://localhost:8181/ads/1| 
| Delete an Ad by Id | DELETE | http://localhost:8181/ads/1| 


[API Documentation](https://documenter.getpostman.com/view/2204/Szzg9ydZ)

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/f9c26ecdb352b91b5f4f)

