#!/usr/bin/env bash

set -e

openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g kotlin \
  -o generated-client
