package com.openapi.cloud.core.util;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class LocaleUtilsTest {


    @Test
    void getLocaleBasedOnProvidedLangJapanese() {
        Locale locale = LocaleUtils.fromLang("ja");

        assertNotNull(locale);
        assertEquals("ja", locale.getLanguage());
        assertEquals("JP", locale.getCountry());
    }

    @Test
    void getLocaleBasedOnProvidedLangEnglish() {
        Arrays.stream(Locale.getAvailableLocales()).forEach(System.out::println);
        Locale locale = LocaleUtils.fromLang("en");

        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
    }
}