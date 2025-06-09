package com.openapi.cloud.core.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Service for recording custom metrics in the application
 */
@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    @Autowired
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Increment a counter with the given name and tags
     *
     * @param name the name of the counter
     * @param tags the tags to associate with the counter (must be even number of arguments: key1, value1, key2, value2, etc.)
     */
    public void incrementCounter(String name, String... tags) {
        Counter counter = Counter.builder(name)
                .tags(tags)
                .register(meterRegistry);
        counter.increment();
    }

    /**
     * Record the execution time of a function
     *
     * @param name the name of the timer
     * @param function the function to time
     * @param tags the tags to associate with the timer (must be even number of arguments)
     * @return the result of the function
     */
    public <T> T recordTimer(String name, Supplier<T> function, String... tags) {
        Timer timer = Timer.builder(name)
                .tags(tags)
                .register(meterRegistry);
        
        long start = System.nanoTime();
        try {
            return function.get();
        } finally {
            timer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Record the execution time of a runnable
     *
     * @param name the name of the timer
     * @param runnable the runnable to time
     * @param tags the tags to associate with the timer (must be even number of arguments)
     */
    public void recordTimer(String name, Runnable runnable, String... tags) {
        Timer timer = Timer.builder(name)
                .tags(tags)
                .register(meterRegistry);
        
        timer.record(runnable);
    }
}