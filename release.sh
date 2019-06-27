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

set -e


function set_version {
  local version=$1

  # Change API version
  mvn -f relinkr-api versions:set versions:commit -DnewVersion=${version}

  # Change UI version
  sed -i "0,/\"version\"\: \".*\"/s/\(\"version\"\: \"\).*\(\"\)/\1${version}\2/g" relinkr-ui/package.json
  sed -i "0,/\"version\"\: \".*\"/s/\(\"version\"\: \"\).*\(\"\)/\1${version}\2/g" relinkr-ui/package-lock.json
}


function main {
  release_version=$1
  next_dev_version=$2

  if [[ -z ${release_version} || -z ${next_dev_version} ]]; then
    echo "Usage: {0} <release version> <next dev version>."
    exit 1
  fi

  # Set release version
  set_version ${release_version}
  git commit -am "Release ${release_version}"
  git tag "v${release_version}"

  # Set next dev version
  set_version ${next_dev_version}
  git commit -am "Next development version"

  echo "That's all folks!"
}


main "$@"

exit 0
