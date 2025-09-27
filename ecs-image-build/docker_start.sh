#!/bin/bash

PORT=8080

printf "\nSTARTING IMAGE...\n" && \

exec java -jar -Dserver.port="${PORT}" "application.jar"