#!/bin/bash

echo "Stopping Gradle daemons..."
./gradlew --stop 2>/dev/null

echo "Killing leftover Java/Gradle processes..."
pkill -f Gradle 2>/dev/null
pkill -f Kotlin 2>/dev/null

echo "Deleting project build dirs..."
find . -type d -name "build" -prune -exec rm -rf {} +

echo "Deleting Gradle local caches..."
rm -rf ~/.gradle/caches
rm -rf ~/.gradle/daemon
rm -rf ~/.gradle/native
rm -rf ~/.gradle/kotlin

echo "Deleting project-local Gradle cache..."
rm -rf .gradle

echo "Done. Rebuilding from scratch..."
./gradlew clean build --refresh-dependencies