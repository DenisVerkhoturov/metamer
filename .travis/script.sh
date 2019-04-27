#!/usr/bin/env bash
set -e

if [[ ${TRAVIS_OS_NAME} == 'windows' ]]; then
    bash gradlew check
fi
