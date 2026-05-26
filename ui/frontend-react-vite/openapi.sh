#!/usr/bin/env bash

set -e

npx @openapitools/openapi-generator-cli version-manager set 7.7.0
rm -rf src/shared/generated
npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:3001/api-json \
  -g typescript-fetch \
  -o ./src/shared/generated
