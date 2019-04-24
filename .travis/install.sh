#!/bin/bash
set -e

if [[ $TRAVIS_OS_NAME == 'windows' ]]; then

    case "${JDK}" in
        j11)
	    choco install jdk11
	    export PATH=$PATH:"/c/Program Files/Java/jdk-11.0.2/bin" 

	    set JAVA_HOME=C:/program\ files/java/jdk-11.0.2         
	   
	    ./gradlew check
            ;;
    esac
fi
