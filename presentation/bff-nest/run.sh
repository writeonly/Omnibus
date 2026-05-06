#!/usr/bin/env bash

set -e

if [ ! -d "node_modules" ]; then
  npm install
fi

npx nest start --watch --verbose
