#!/bin/bash

set -e

git clone https://github.com/flash-freezing-lava/intellij-directory-tests.git
cd intellij-directory-tests
git checkout v0.2.0
gradle wrapper
./gradlew publishToMavenLocal
cd ..
rm -rf intellij-directory-tests