#!/usr/bin/env bash

set -e

npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:3000/api-json \
  -g typescript-angular \
  -o ./src/generated/openapi
  