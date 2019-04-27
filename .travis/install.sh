#!/usr/bin/env bash
set -e

if [[ ${TRAVIS_OS_NAME} == 'windows' ]]; then
    for package in $(CHOCO_PACKAGES//,/); do
        choco install ${package}
    done
fi
