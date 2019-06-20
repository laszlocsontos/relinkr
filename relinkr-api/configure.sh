#!/usr/bin/env bash
#
#  Copyright [2018-2019] Laszlo Csontos (sole trader)
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

readonly APP_NAME="relinkr_api"

readonly GCP_SQL_INSTANCE="relinkr-db4"
readonly GCP_SQL_VERSION="POSTGRES_9_6"
readonly GCP_SQL_TIER="db-f1-micro"

readonly DEFAULT_PGSQL_USERNAME="postgres"

readonly E_NO_PROFILE=1
readonly E_NO_ENV_VAR=2
readonly E_NO_ACCOUNT=3
readonly E_NO_PROJECT=4
readonly E_NO_CONNECTION_NAME=5
readonly E_DB_ERROR=6


function check_required {
  local name=$1
  local value=${!name}
  if [[ -z "${value}" ]]; then
    echo "Set ${name} Luke!"
    exit ${E_NO_ENV_VAR}
  fi
}


function check_gcloud_config {
  local name=$1
  local value=$(gcloud config list --format "value(${name})")
  local fix=$2
  local exit_status=$3
  if [[ -z "${value}" ]]; then
    echo "Use \"${fix}\" Luke!"
    exit ${exit_status}
  fi
}


function enable_service {
  local service_name=$1
  local enabled_service_name=$(gcloud services list  --enabled --filter NAME=${service_name} --format 'value(NAME)')
  if [[ -z "${enabled_service_name}" ]]; then
    echo "Enabling service ${service_name}..."
    gcloud services enable ${service_name}
  else
    echo "Service ${service_name} has already been enabled."
  fi
}


function ensure_db {
  local instance_name=$1
  local version=$2
  local tier=$3
  local db_name=$4
  local db_username=$5
  local db_password=$6

  local connection_name=$(gcloud sql instances describe ${instance_name} --format 'value(connectionName)')
  if [[ -z "${connection_name}" ]]; then
    echo "Database instance ${instance_name} is being created, patience..."

    gcloud sql instances create ${instance_name} \
      --database-version=${version} \
      --tier=${tier}

    local state=$(gcloud sql instances describe ${instance_name} --format 'value(state)')
    if [[ "${state}" != "PENDING_CREATE" && "${state}" != "RUNNABLE" ]]; then
      echo "Aborting; database instance ${instance_name} creation failed; state is ${state}."
      exit ${E_DB_ERROR}
    fi

    # Wait for the DB instance become available
    local retries=0
    while [[ "${state}" == "PENDING_CREATE" && ${retries} -lt 30 ]]; do
      echo "Database instance creation is still in progress; waiting for 60s..."
      sleep 60
      retries=$((retries + 1))
      state=$(gcloud sql instances describe ${instance_name} --format 'value(state)')
    done

    if [[ "${state}" != "RUNNABLE" ]]; then
      echo "Aborting; database instance ${instance_name} is in ${state} state after ${retries} retries."
      exit ${E_DB_ERROR}
    fi

    connection_name=$(gcloud sql instances describe ${instance_name} --format 'value(connectionName)')
  else
    echo "Database instance ${instance_name} has already been created."
  fi

  declare -g GCP_SQL_CONNECTION=${connection_name}

  echo "Setting password for user ${db_username} on instance ${instance_name}."
  gcloud sql users set-password ${db_username} \
    --instance=${instance_name} \
    --password=${db_password}

  gcloud sql databases describe ${db_name} --instance=${instance_name} &>/dev/null
  if [[ $? -ne 0 ]]; then
    echo "Creating database ${db_name}..."
    gcloud sql databases create ${db_name} --instance ${instance_name}
  else
    echo "Database ${db_name} has already been created."
  fi
}


function ensure_runtime_config {
  local config_name=$1
  gcloud beta runtime-config configs describe ${config_name} --format "value(name)" &>/dev/null
  if [[ $? -ne 0 ]]; then
    echo "Creating runtime config ${config_name}..."
    gcloud beta runtime-config configs create ${config_name}
  else
    echo "Runtime config ${config_name} has already been created."
  fi
}


function set_runtime_config_variable {
  local config_name=$1
  local var_name=$2
  local var_value=${!var_name}
  echo "Setting ${var_name} for runtime config ${config_name}..."
  gcloud beta runtime-config configs variables set ${var_name} "${var_value}" --config-name ${config_name}
}


function main {
  local profile=$1
  if [[ -z "${profile}" ]]; then
    echo "Usage: ${0} <profile>"
    exit ${E_NO_PROFILE}
  fi

  # Check OAuth2 configuration vars
  check_required OAUTH2_GOOGLE_CLIENT_ID
  check_required OAUTH2_GOOGLE_CLIENT_SECRET
  check_required OAUTH2_FACEBOOK_CLIENT_ID
  check_required OAUTH2_FACEBOOK_CLIENT_SECRET

  # Check if a DB name was given, otherwise set a default
  if [[ -z "${GCP_SQL_DB}" ]]; then
    GCP_SQL_DB="${APP_NAME}_${profile}"
  fi


  # Check if a DB user name was given; default to postgres otherwise
  if [[ -z "${PGSQL_USERNAME}" ]]; then
    PGSQL_USERNAME=${DEFAULT_PGSQL_USERNAME}
  fi

  # Check if a DB user password was given; generate a random password otherwise
  if [[ -z "${PGSQL_PASSWORD}" ]]; then
    PGSQL_PASSWORD=$(openssl rand -hex 16)
  fi

  # Check if JWT keys were given; generate new key pair otherwise
  if [[ -z "${JWT_PRIVATE_KEY}" ]]; then
    private_key=$(mktemp)
    openssl genrsa -out ${private_key} 2048 &>/dev/null

    JWT_PRIVATE_KEY=$(openssl pkcs8 -topk8 -nocrypt -in /tmp/privatekey.pem -outform der | base64 -w0)
    JWT_PUBLIC_KEY=$(openssl rsa -in /tmp/privatekey.pem -outform der -pubout | base64 -w0)

    rm ${private_key}
  fi

  # Check if visitor cookie key was given; generate new key otherwise
  if [[ -z "${COOKIE_VISITOR_SECRET_KEY}" ]]; then
    COOKIE_VISITOR_SECRET_KEY=$(openssl rand -base64 32)
  fi

  # Check if OAuth2 cookie key was given; generate new key otherwise
  if [[ -z "${COOKIE_OAUTH2_REQUEST_SECRET_KEY}" ]]; then
    COOKIE_OAUTH2_REQUEST_SECRET_KEY=$(openssl rand -base64 32)
  fi

  # Check if an active account and a project have been selected
  check_gcloud_config core.account "gcloud auth login" ${E_NO_ACCOUNT}
  check_gcloud_config core.project "gcloud config set project" ${E_NO_PROJECT}

  # Enable required GCP services
  enable_service "sqladmin.googleapis.com"
  enable_service "sql-component.googleapis.com"
  enable_service "runtimeconfig.googleapis.com"

  # Create DB instance and check if we have a connection string after that
  ensure_db ${GCP_SQL_INSTANCE} ${GCP_SQL_VERSION} ${GCP_SQL_TIER} ${GCP_SQL_DB} ${PGSQL_USERNAME} ${PGSQL_PASSWORD}
  if [[ -z "${GCP_SQL_CONNECTION}" ]]; then
    echo "${GCP_SQL_CONNECTION} is empty; DB creation must have failed."
    exit ${E_NO_CONNECTION_NAME}
  fi

  # Create configuration for the app's profile if it hasn't existed yet
  config_name="${APP_NAME}_${profile}"
  ensure_runtime_config ${config_name}

  # Set runtime configuration variables
  set_runtime_config_variable ${config_name} JWT_PRIVATE_KEY
  set_runtime_config_variable ${config_name} JWT_PUBLIC_KEY
  set_runtime_config_variable ${config_name} COOKIE_VISITOR_SECRET_KEY
  set_runtime_config_variable ${config_name} COOKIE_OAUTH2_REQUEST_SECRET_KEY
  set_runtime_config_variable ${config_name} OAUTH2_GOOGLE_CLIENT_ID
  set_runtime_config_variable ${config_name} OAUTH2_GOOGLE_CLIENT_SECRET
  set_runtime_config_variable ${config_name} OAUTH2_FACEBOOK_CLIENT_ID
  set_runtime_config_variable ${config_name} OAUTH2_FACEBOOK_CLIENT_SECRET
  set_runtime_config_variable ${config_name} GCP_SQL_DB
  set_runtime_config_variable ${config_name} GCP_SQL_CONNECTION
  set_runtime_config_variable ${config_name} PGSQL_USERNAME
  set_runtime_config_variable ${config_name} PGSQL_PASSWORD
}


main "$@"

exit 0
