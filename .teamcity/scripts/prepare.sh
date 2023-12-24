#!/usr/bin/env bash
set -e

mkdir -p app_sources
cp .teamcity/scripts/launch.sh app_sources/
cp target/demo-*.jar app_sources/app.jar