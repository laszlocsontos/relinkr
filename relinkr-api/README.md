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
% openssl genrsa -out /tmp/privatekey.pem 2048
```

3. Create a file `.env.development.local` with the following content

```
JWT_PRIVATE_KEY=
JWT_PUBLIC_KEY=

COOKIE_VISITOR_SECRET_KEY=
COOKIE_OAUTH2_REQUEST_SECRET_KEY=

OAUTH2_GOOGLE_CLIENT_ID=
OAUTH2_GOOGLE_CLIENT_SECRET=

OAUTH2_FACEBOOK_CLIENT_ID=
OAUTH2_FACEBOOK_CLIENT_SECRET=
```

4. Set required environment variables

- Set the value of `JWT_PRIVATE_KEY` with the following command.

```
% openssl pkcs8 -topk8 -nocrypt -in privatekey.pem -outform der | base64 -w0
```

- Set the value of `JWT_PUBLIC_KEY` with the following command.

```
% openssl rsa -in privatekey.pem -outform der -pubout | base64 -w0
```

- Set the value of `COOKIE_VISITOR_SECRET_KEY` and `COOKIE_OAUTH2_REQUEST_SECRET_KEY` with the following command.

```
% openssl rand -base64 32
```

5. Obtain OAuth 2.0 credentials from the [Google API Console](https://console.developers.google.com/apis/credentials).

Obtain client ID and client secret and set variables `OAUTH2_GOOGLE_CLIENT_ID` and `OAUTH2_GOOGLE_CLIENT_SECRET` accordingly.

6. Obtain OAuth 2.0 credentials from the [Facebook for Developers](https://developers.facebook.com/apps/).

Obtain client ID and client secret and set variables `OAUTH2_FACEBOOK_CLIENT_ID` and `OAUTH2_FACEBOOK_CLIENT_SECRET` accordingly.

### Customize configuration

You can customize other environment variables like `PGSQL_HOST`, but they'll have sensible defaults for running reLinkR in development mode.
See [application.yml](src/main/resources/application.yml).

### Compile and run all unit tests

```
% ./mvnw clean install
```

### Run API locally
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=development
```

## Integration testing

### Automated configuration

Creating the database instance, runtime configuration variables for integration testing is automated
with `configure.sh <profile>`. If you rather prefer to do it manually, refer to next section.

1. Create a file `.env.integration.local` with the following content and configure the environment
variables accordingly. 

```
OAUTH2_GOOGLE_CLIENT_ID=
OAUTH2_GOOGLE_CLIENT_SECRET=

OAUTH2_FACEBOOK_CLIENT_ID=
OAUTH2_FACEBOOK_CLIENT_SECRET=

PGSQL_PASSWORD=

FRONTEND_BASE_URL=https://localhost:9443
SHORT_LINK_DOMAIN=localhost
```

_Note: the integration test doesn't use actually `FRONTEND_BASE_URL` and `SHORT_LINK_DOMAIN`,
it's safe to leave them on defaults._ 

2. Configure `integration` environment

```
% ./configure.sh integration
```

### Manual configuration

1. Create a PostgreSQL instance

```
% gcloud sql instances create relinkr-db --database-version=POSTGRES_9_6 --tier=db-f1-micro
% gcloud sql users set-password postgres --instance=relinkr-db --password=secret
```

2. Create a PostgreSQL database for the integration tests

```
gcloud sql databases create relinkr_api_integration --instance relinkr-db
```

3. Set GCP database properties

```
% export GCP_SQL_DB=relinkr_api_integration
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
% gcloud beta runtime-config configs create relinkr_api_integration
```

7. Set variables to GCP Runtime Configuration

```
% gcloud beta runtime-config configs variables set JWT_PRIVATE_KEY "${JWT_PRIVATE_KEY}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set JWT_PUBLIC_KEY "${JWT_PUBLIC_KEY}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set COOKIE_VISITOR_SECRET_KEY "${COOKIE_VISITOR_SECRET_KEY}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set COOKIE_OAUTH2_REQUEST_SECRET_KEY "${COOKIE_OAUTH2_REQUEST_SECRET_KEY}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set OAUTH2_GOOGLE_CLIENT_ID "${OAUTH2_GOOGLE_CLIENT_ID}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set OAUTH2_GOOGLE_CLIENT_SECRET "${OAUTH2_GOOGLE_CLIENT_SECRET}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set GCP_SQL_DB "${GCP_SQL_DB}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set GCP_SQL_CONNECTION "${GCP_SQL_CONNECTION}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set PGSQL_USERNAME "${PGSQL_USERNAME}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set PGSQL_PASSWORD "${PGSQL_PASSWORD}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set FRONTEND_BASE_URL "${FRONTEND_BASE_URL}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set SHORT_LINK_SCHEME "${SHORT_LINK_SCHEME}" --config-name relinkr_api_integration
% gcloud beta runtime-config configs variables set SHORT_LINK_DOMAIN "${SHORT_LINK_DOMAIN}" --config-name relinkr_api_integration
```

### Run the integration tests

```
% ./mvnw integration-test verify -P integration
```

## GCP Deployment

There are Maven two profiles setup for deploying to GCP `staging` and `production`. In this last section
steps will refer to a `staging` deployment.

1. Create a file `.env.staging.local` with the following content and configure the environment
variables accordingly. 

```
OAUTH2_GOOGLE_CLIENT_ID=
OAUTH2_GOOGLE_CLIENT_SECRET=

OAUTH2_FACEBOOK_CLIENT_ID=
OAUTH2_FACEBOOK_CLIENT_SECRET=

PGSQL_PASSWORD=

FRONTEND_BASE_URL=
SHORT_LINK_DOMAIN=
COOKIE_AUTH_TOKEN_DOMAIN=
```

_Note: now you need to set valid values for both `FRONTEND_BASE_URL` and `SHORT_LINK_DOMAIN`. Set
`COOKIE_AUTH_TOKEN_DOMAIN` only in that case when you plan to use a custom domain._


1. Configure `staging` environment

```
% ./configure.sh staging
```

2. Package the application before deployment

```
% ./mvnw package appengine:stage -P staging
```

_Note: This step requires environment setup for integration tests. Should you have skipped that,
refer to previous section how to do that._

3. Initialize an App Engine application within the project

```
% gcloud app create
```

4. Initiate deployment

```
% ./mvnw appengine:deploy
```
