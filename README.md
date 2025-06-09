## auth-service JWT (JSON Web Token) provider, handles the issuance, validation, and management of JWTs used for authentication and authorization in applications

1. **Start your Spring Boot application**

   ```bash
   # For auth-service
   cd auth-service
   ./gradlew bootRun --args='--spring.profiles.active=monitoring'

     Options 
    ./gradlew bootRun --stacktrace
    ./gradlew clean bootRun
    ./gradlew clean
    ./gradlew clean build
   ```

     Access application Actuator endpoints:
   - http://localhost:1007/actuator
   - http://localhost:1007/actuator/health
   - http://localhost:1007/actuator/info
   - http://localhost:1007/actuator/metrics
   - http://localhost:1007/actuator/metrics/system.cpu.usage
   - http://localhost:1007/actuator/prometheus

   ```bash
     # /actuator/health: Shows application health information
     # /actuator/info: Displays application information
     # /actuator/metrics: Shows metrics information
     # /actuator/env: Exposes environment properties
     # /actuator/loggers: Shows and modifies logger configurations
     # /actuator/prometheus: Exposes metrics in Prometheus format     
   ```

   ```bash
   # to stop
      docker-compose -f docker-compose-monitoring.yml down
   # Start Prometheus, Alertmanager, and Grafana:
      docker-compose -f docker-compose-monitoring.yml up -d
   ```
   

2. **Open API (Swagger UI)**
Access application endpoints:

   - http://localhost:1007/swagger-ui/index.html
  
   - curl -i http://localhost:1007/


1. **Locale settings (i18)**

   ```bash
   # Open Chrome settings → Language → Preferred languages.Add 日本語 (Japanese) and move it to top.

   OR use Chrome Developer Tools

   #Open DevTools → Network tab → Right-click the request → "Copy as cURL". You'll see Accept-Language: ja or Accept-Language: en-US,en;q=0.9 etc 

   ```  

2. **h2-database console**

   ```bash
   # default path: h2-console  : http://localhost:1007/h2-ui/

   #JDBC URL: jdbc:h2:file:./YOUR_DB_NAME

   ```
