#!/bin/bash

./gradlew clean test jacocoTestReport && \
./gradlew sonar --info \
  -Dsonar.projectKey=auth-service \
  -Dsonar.projectName='auth-service' \
  -Dsonar.host.url=http://192.168.3.9:9000 \
  -Dsonar.token=sqp_854e80d286d1553eedd1243ddde573f239c2b61b