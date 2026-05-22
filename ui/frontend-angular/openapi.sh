#!/usr/bin/env bash

set -e

npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:3001/api-json \
  -g typescript-angular \
  -o ./src/generated/openapi
  