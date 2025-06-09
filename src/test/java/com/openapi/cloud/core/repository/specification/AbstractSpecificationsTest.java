package com.openapi.cloud.core.repository.specification;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractSpecificationsTest {

    @Test
    void testDeleteFlagValue() {
        // Test that DELETE_FLAG has the expected value
        assertEquals(0, AbstractSpecifications.DELETE_FLAG);
    }
}