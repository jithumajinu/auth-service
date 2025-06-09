#!/bin/bash

./gradlew clean test jacocoTestReport && \
./gradlew sonar --info \
  -Dsonar.projectKey=auth-service \
  -Dsonar.projectName='auth-service' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_4c58cab2cf8b7fe83680caf66f82235b83d71a53