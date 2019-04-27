#!/usr/bin/env bash
set -e

if [[ ${TRAVIS_OS_NAME} == 'windows' ]]; then
    for package in ${CHOCO_PACKAGES//,/ }; do
        choco install ${package}
    done

    export PATH="$(powershell -Command '("Process", "Machine" | % {
        [Environment]::GetEnvironmentVariable("PATH", $_) -Split ";" -Replace "\\$", ""
    } | Select -Unique | % { cygpath $_ }) -Join ":"')"
    echo "PATH=${PATH}"
fi
