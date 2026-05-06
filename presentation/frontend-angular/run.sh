#!/usr/bin/env bash

set -e

if [ ! -d "node_modules" ]; then
  npm install
fi

if [ ! -f "proxy.conf.json" ]; then
  echo "Warning: proxy.conf.json not found"
fi

ng serve --proxy-config proxy.conf.json --verbose
