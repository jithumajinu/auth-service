package com.openapi.cloud.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PageUtilTest {

    /**
     * Tests the createPageable method with an invalid sort order.
     * Expects the method to default to ascending order.
     */
    @Test
    void testCreatePageableWithInvalidSortOrder() {
        PageUtil pageUtil = new PageUtil();
        Map<String, String> orders = new HashMap<>();
        orders.put("field", "INVALID_ORDER");
        Pageable pageable = pageUtil.createPageable(1, 10, orders);
        assertTrue(pageable.getSort().getOrderFor("field").isAscending());
    }

    /**
     * Tests the createPageable method with a negative page number.
     * Expects the method to use the default page number (0) in this case.
     */
    @Test
    void testCreatePageableWithNegativePageNumber() {
        PageUtil pageUtil = new PageUtil();
        Map<String, String> orders = new HashMap<>();
        Pageable pageable = pageUtil.createPageable(-1, 10, orders);
        assertEquals(0, pageable.getPageNumber());
    }

    /**
     * Tests the createPageable method with a negative page size.
     * Expects the method to use the default page size (10) in this case.
     */
    @Test
    void testCreatePageableWithNegativePageSize() {
        PageUtil pageUtil = new PageUtil();
        Map<String, String> orders = new HashMap<>();
        Pageable pageable = pageUtil.createPageable(1, -5, orders);
        assertEquals(10, pageable.getPageSize());
    }

    /**
     * Test case where sortKey and sortOrder are empty lists.
     * This scenario is explicitly handled in the method implementation.
     */
    @Test
    void testGetSortFields_emptyLists() {
        Map<String, String> result = PageUtil.getSortFields(Arrays.asList(), Arrays.asList(), "defaultKey", "defaultValue");
        assertEquals(1, result.size());
        assertTrue(result.containsKey("defaultKey"));
        assertEquals("defaultValue", result.get("defaultKey"));
    }

    /**
     * Test case where sortKey is null and sortOrder is not null.
     * This scenario is explicitly handled in the method implementation.
     */
    @Test
    void testGetSortFields_nullSortKey() {
        Map<String, String> result = PageUtil.getSortFields(null, Arrays.asList("ASC"), "defaultKey", "defaultValue");
        assertEquals(1, result.size());
        assertTrue(result.containsKey("defaultKey"));
        assertEquals("defaultValue", result.get("defaultKey"));
    }

    /**
     * Test case where sortKey is not null but sortOrder is null.
     * This scenario is explicitly handled in the method implementation.
     */
    @Test
    void testGetSortFields_nullSortOrder() {
        Map<String, String> result = PageUtil.getSortFields(Arrays.asList("key1"), null, "defaultKey", "defaultValue");
        assertEquals(1, result.size());
        assertTrue(result.containsKey("defaultKey"));
        assertEquals("defaultValue", result.get("defaultKey"));
    }

    /**
     * Tests the toPageable method with empty sort key and order lists.
     * Verifies that default sort values are used when empty lists are provided.
     */
    @Test
    void testToPageableWithEmptySortLists() {
        Pageable pageable = PageUtil.toPageable(1, 20, Collections.emptyList(), Collections.emptyList(), "id", "DESC");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "id"), pageable.getSort());
    }

    /**
     * Tests the toPageable method with invalid page and size values.
     * Verifies that default values are used when invalid values are provided.
     */
    @Test
    void testToPageableWithInvalidPageAndSize() {
        Pageable pageable = PageUtil.toPageable(0, 0, Collections.singletonList("name"), Collections.singletonList("DESC"), "id", "ASC");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "name"), pageable.getSort());
    }

    /**
     * Tests the toPageable method with mismatched sort key and order lists.
     * Verifies that default ASC order is used for extra keys without specified orders.
     */
    @Test
    void testToPageableWithMismatchedSortLists() {
        Pageable pageable = PageUtil.toPageable(1, 15, Arrays.asList("name", "age"), Collections.singletonList("DESC"), "id", "ASC");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(15, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "name").and(Sort.by(Sort.Direction.ASC, "age")), pageable.getSort());
    }

    /**
     * Tests the toPageable method with null page and size values.
     * Verifies that default values are used when null is provided.
     */
    @Test
    void testToPageableWithNullPageAndSize() {
        Pageable pageable = PageUtil.toPageable(null, null, Collections.singletonList("id"), Collections.singletonList("ASC"), "id", "ASC");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "id"), pageable.getSort());
    }

    /**
     * Test case for createPageable method with valid input parameters.
     * Verifies that the method returns a Pageable object with correct page number, size, and sort order.
     */
    @Test
    void test_createPageable_withValidInput() {
        PageUtil pageUtil = new PageUtil();
        Integer number = 2;
        Integer size = 15;
        Map<String, String> orders = new HashMap<>();
        orders.put("name", "ASC");
        orders.put("id", "DESC");

        Pageable result = pageUtil.createPageable(number, size, orders);

        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals(15, result.getPageSize());
        Sort sort = result.getSort();
        assertEquals(2, sort.stream().count());
        assertEquals(Sort.Direction.ASC, sort.getOrderFor("name").getDirection());
        assertEquals(Sort.Direction.DESC, sort.getOrderFor("id").getDirection());
    }

    /**
     * Tests the getSortFields method when sortKey and sortOrder are not null,
     * but result in an empty map, causing the default sort to be applied.
     */
    @Test
    void test_getSortFields_whenSortKeysEmptyDefaultSortApplied() {
        List<String> sortKey = Arrays.asList();
        List<String> sortOrder = Arrays.asList();
        String defaultSortKey = "id";
        String defaultSortValue = "DESC";

        Map<String, String> result = PageUtil.getSortFields(sortKey, sortOrder, defaultSortKey, defaultSortValue);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(defaultSortKey));
        assertEquals(defaultSortValue, result.get(defaultSortKey));
    }

    /**
     * Tests getSortFields method when both sortKey and sortOrder are null, and default values are provided.
     * Verifies that the method returns a map with the default sort key and value.
     */
    @Test
    void test_getSortFields_withNullInputsAndDefaultValues() {
        String defaultSortKey = "id";
        String defaultSortValue = "DESC";

        Map<String, String> result = PageUtil.getSortFields(null, null, defaultSortKey, defaultSortValue);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(defaultSortKey));
        assertEquals(defaultSortValue, result.get(defaultSortKey));
    }

    /**
     * Tests the getSortFields method when valid sortKey and sortOrder lists are provided.
     * Verifies that the method correctly maps the sort keys to their corresponding orders,
     * and does not use the default sort key and value.
     */
    @Test
    void test_getSortFields_withValidSortKeyAndOrder() {
        List<String> sortKey = Arrays.asList("name", "age");
        List<String> sortOrder = Arrays.asList("DESC", "ASC");
        String defaultSortKey = "id";
        String defaultSortValue = "ASC";

        Map<String, String> result = PageUtil.getSortFields(sortKey, sortOrder, defaultSortKey, defaultSortValue);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("DESC", result.get("name"));
        assertEquals("ASC", result.get("age"));
        assertFalse(result.containsKey(defaultSortKey));
    }

    /**
     * Tests the toPageable method when the value in the orders map is empty.
     * Expects the method to return a Pageable object with an ascending order for the given key.
     */
    @Test
    void test_toPageable_emptyOrderValue() {
        Integer number = 1;
        Integer size = 10;
        Map<String, String> orders = new HashMap<>();
        orders.put("testKey", "");

        Pageable result = PageUtil.toPageable(number, size, orders);

        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertTrue(result.getSort().isSorted());
        assertEquals(Sort.Direction.ASC, result.getSort().getOrderFor("testKey").getDirection());
    }

    /**
     * Tests the behavior of toPageable method when given an invalid sort order value.
     * It verifies that the method defaults to ascending order for invalid inputs.
     */
    @Test
    void test_toPageable_invalidSortOrder() {
        Map<String, String> orders = new HashMap<>();
        orders.put("field", "INVALID_ORDER");

        Pageable result = PageUtil.toPageable(1, 10, orders);

        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(Sort.by(Sort.Order.asc("field")), result.getSort());
    }

    /**
     * Tests the behavior of toPageable method when given null or negative values for page number and size.
     * It verifies that the method uses default values in such cases.
     */
    @Test
    void test_toPageable_nullAndNegativeInputs() {
        Pageable result = PageUtil.toPageable(null, -5, new HashMap<>());
        assertEquals(PageUtil.DEFAULT_NUMBER, result.getPageNumber());
        assertEquals(PageUtil.DEFAULT_SIZE, result.getPageSize());
        assertEquals(Sort.unsorted(), result.getSort());
    }

    /**
     * Tests the toPageable method when a non-empty sort order is provided.
     * Verifies that the method correctly creates a Pageable object with descending order.
     */
    @Test
    void test_toPageable_withDescendingOrder() {
        Integer number = 2;
        Integer size = 20;
        Map<String, String> orders = new HashMap<>();
        orders.put("testField", "DESC");

        Pageable result = PageUtil.toPageable(number, size, orders);

        assertEquals(1, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertTrue(result.getSort().isSorted());
        assertEquals(Sort.Direction.DESC, result.getSort().getOrderFor("testField").getDirection());
    }

    /**
     * Test case for toPageable method with default values.
     * Verifies that the method returns a Pageable object with default page number,
     * size, and no sorting when null values are provided.
     */
    @Test
    void test_toPageable_withNullValues() {
        Pageable result = PageUtil.toPageable(null, null);

        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(Sort.unsorted(), result.getSort());
    }

    /**
     * Tests the toPageable method with valid inputs for page, size, sortKey, and sortOrder.
     * Verifies that the returned Pageable object has the correct page number, page size,
     * and sort order based on the provided parameters.
     */
    @Test
    void test_toPageable_withValidInputs() {
        Integer page = 2;
        Integer size = 20;
        List<String> sortKey = Arrays.asList("name", "age");
        List<String> sortOrder = Arrays.asList("ASC", "DESC");
        String defaultSortKey = "id";
        String defaultSortValue = "ASC";

        Pageable result = PageUtil.toPageable(page, size, sortKey, sortOrder, defaultSortKey, defaultSortValue);

        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals(Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age")), result.getSort());
    }

    /**
     * Tests the toPageable method with negative page number.
     * Expects the method to use the default page number (0) when given a negative value.
     */
    @Test
    void test_toPageable_with_negative_page_number() {
        Integer page = -1;
        Integer size = 10;
        Pageable pageable = PageUtil.toPageable(page, size);
        assertEquals(0, pageable.getPageNumber());
    }

    /**
     * Tests the toPageable method with negative page size.
     * Expects the method to use the default page size (10) when given a negative value.
     */
    @Test
    void test_toPageable_with_negative_page_size() {
        Integer page = 1;
        Integer size = -5;
        Pageable pageable = PageUtil.toPageable(page, size);
        assertEquals(10, pageable.getPageSize());
    }

    /**
     * Tests the toPageable method with zero page size.
     * Expects the method to use the default page size (10) when given a zero value.
     */
    @Test
    void test_toPageable_with_zero_page_size() {
        Integer page = 1;
        Integer size = 0;
        Pageable pageable = PageUtil.toPageable(page, size);
        assertEquals(10, pageable.getPageSize());
    }

}
