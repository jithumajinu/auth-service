package com.openapi.cloud.core.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Task to clean up old log files beyond the retention period.
 * This is a backup mechanism in addition to Logback's built-in log rotation.
 */
@Slf4j
@Component
public class LogCleanupTask {

    
    @Value("${logging.file.path:./logs}")
    private String logPath;
    
    @Value("${logging.retention.days:30}")
    private int retentionDays;

    /**
     * Scheduled task to clean up old log files.
     * Runs at 1:00 AM every day.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupOldLogs() {
        log.info("Starting log cleanup task");
        
        LocalDate cutoffDate = LocalDate.now().minusDays(retentionDays);
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try (Stream<Path> paths = Files.walk(Paths.get(logPath))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".log"))
                 .forEach(path -> {
                     String filename = path.getFileName().toString();
                     
                     // Check if the filename contains a date pattern
                     if (filename.matches(".*\\d{4}-\\d{2}-\\d{2}.*")) {
                         try {
                             // Extract the date from the filename
                             int dateStartIndex = filename.indexOf("20"); // Assuming years start with 20xx
                             if (dateStartIndex >= 0) {
                                 String dateStr = filename.substring(dateStartIndex, dateStartIndex + 10);
                                 LocalDate fileDate = LocalDate.parse(dateStr, datePattern);
                                 
                                 // Delete if older than retention period
                                 if (fileDate.isBefore(cutoffDate)) {
                                     Files.delete(path);
                                     log.info("Deleted old log file: {}", filename);
                                 }
                             }
                         } catch (Exception e) {
                             log.warn("Failed to process log file: {}", filename, e);
                         }
                     }
                 });
            
            log.info("Log cleanup task completed");
        } catch (IOException e) {
            log.error("Error during log cleanup", e);
        }
    }
}