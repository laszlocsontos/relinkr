# reLinkR API

## Development

### Project setup

1. Create a PostgreSQL user and database
```
=> create user relinkr with password 'relinkr';
=> create database relinkr with owner relinkr;
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

5. Obtain OAuth 2.0 credentials from the [Facebook for Developers](https://developers.facebook.com/apps/).

Obtain client ID and client secret and set the following environment variables accordingly.

```
% export OAUTH2_FACEBOOK_CLIENT_ID=...
% export OAUTH2_FACEBOOK_CLIENT_SECRET=...
```

### Customize configuration

You can customize other environment variables like `PGSQL_HOST`, but they'll have sensible defaults for running reLinkR in development mode.
See [application.yml](src/main/resources/application.yml).

### Compile and run all unit tests

```
% ./mvnw clean install
```

### Run API locally
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Integration testing

### Automated configuration

Create the database instance, runtime configuration variables for integration testing is automated
with `configure.sh <profile>`. If you rather prefer to do it manually, refer to next section.

```
% ./configure it
```

### Manual configuration

1. Create a PostgreSQL instance

```
% gcloud sql instances create relinkr-db --database-version=POSTGRES_9_6 --tier=db-f1-micro
% gcloud sql users set-password postgres --instance=relinkr-db --password=secret
```

2. Create a PostgreSQL database for the integration tests

```
gcloud sql databases create relinkr-it --instance relinkr-db
```

3. Set GCP database properties

```
% export GCP_SQL_DB=relinkr-it
% export GCP_SQL_CONNECTION=$(gcloud sql instances describe relinkr-db --format 'value(connectionName)')
```

4. Set GCP database credentials

```
export PGSQL_USERNAME=postgres
export PGSQL_PASSWORD="secret"
```

_Note: use that password you picked at instance creation time._
 
5. Set the rest of the environment variables as described above

6. Create GCP Runtime Configuration for the integration test's profile

```
% gcloud services enable runtimeconfig.googleapis.com
% gcloud beta runtime-config configs create relinkr_api_it
```

7. Set variables to GCP Runtime Configuration

```
% gcloud beta runtime-config configs variables set JWT_PRIVATE_KEY "${JWT_PRIVATE_KEY}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set JWT_PUBLIC_KEY "${JWT_PUBLIC_KEY}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set COOKIE_VISITOR_SECRET_KEY "${COOKIE_VISITOR_SECRET_KEY}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set COOKIE_OAUTH2_REQUEST_SECRET_KEY "${COOKIE_OAUTH2_REQUEST_SECRET_KEY}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set OAUTH2_GOOGLE_CLIENT_ID "${OAUTH2_GOOGLE_CLIENT_ID}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set OAUTH2_GOOGLE_CLIENT_SECRET "${OAUTH2_GOOGLE_CLIENT_SECRET}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set GCP_SQL_DB "${GCP_SQL_DB}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set GCP_SQL_CONNECTION "${GCP_SQL_CONNECTION}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set PGSQL_USERNAME "${PGSQL_USERNAME}" --config-name relinkr_api_it
% gcloud beta runtime-config configs variables set PGSQL_PASSWORD "${PGSQL_PASSWORD}" --config-name relinkr_api_it
```

### Run the integration tests

```
% ./mvnw integration-test verify -P integration-test
```
