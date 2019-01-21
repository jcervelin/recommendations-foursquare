# Recommendations API

Micro service responsible for return places or recommendations given a name.

## How do I build?
mvn clean install

## How do I run it?
java -jar -DCLIENT_ID=[YOUR CLIENT ID] -DCLIENT_SECRET=[YOUR CLIENT SECRET] recommendations.jar

Accessing the root you will be redirected to the swagger.

http://localhost:8080/swagger-ui.html

## Endpoint:
GET /api/recommendations

Example:
curl -X GET "http://localhost:8080/api/recommendations?limit=10&name=narmia%2Clondon" -H "accept: */*"

For further details about the request, see wiki. https://github.com/jcervelin/recommendations-foursquare/wiki/Recommendations

## Tests
mvn test
