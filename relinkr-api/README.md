# reLinkR API

## Project setup

1. Create a PostgreSQL user and database
```
# create user relinkr with unencrypted password 'relinkr';
# create database relinkr with owner relinkr;
```

2. Generate RSA key pair for signing JWT authentication tokens
```
% openssl genrsa -out privatekey.pem 2048
```

3. Set required environment variables
```
% export JWT_PRIVATE_KEY=$(openssl pkcs8 -topk8 -nocrypt -in privatekey.pem -outform der | base64 -w0)
% export JWT_PUBLIC_KEY=$(openssl rsa -in privatekey.pem -outform der -pubout | base64 -w0)
% export COOKIE_VISITOR_SECRET_KEY=$(openssl rand -base64 32)
% export COOKIE_OAUTH2_REQUEST_SECRET_KEY=$(openssl rand -base64 32)
```

4. Obtain OAuth 2.0 credentials from the [Google API Console](https://console.developers.google.com/apis/credentials).

Obtain client ID and client secret and set the following environment variables accordingly.

```
% export OAUTH2_GOOGLE_CLIENT_ID=...
% export OAUTH2_GOOGLE_CLIENT_SECRET=...
```

4. Obtain OAuth 2.0 credentials from the [Facebook for Developers](https://developers.facebook.com/apps/).

Obtain client ID and client secret and set the following environment variables accordingly.

```
% export OAUTH2_FACEBOOK_CLIENT_ID=...
% export OAUTH2_FACEBOOK_CLIENT_SECRET=...
```

### Customize configuration

You can customize other environment variables like `PGSQL_HOST`, but they'll have sensible defaults for running reLinkR in development mode.
See [application.yml](src/main/resources/application.yml).

### Compiles and runs all unit tests

```
% ./mvnw clean install
```

### Runs API locally
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
