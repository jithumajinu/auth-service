package com.openapi.cloud.core.security;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserPrincipal userPrincipal;

    /**
     * Tests the generateToken method when the authentication object is null.
     * This test verifies that a NullPointerException is thrown when attempting
     * to generate a token with a null authentication.
     */
    @Test
    void testGenerateTokenWithNullAuthentication() {
        assertThrows(NullPointerException.class, () -> {
            jwtTokenProvider.generateToken(null);
        });
    }

    /**
     * Test case for an empty JWT token.
     * This test verifies that the getUserIdFromJWT method throws an IllegalArgumentException
     * when provided with an empty JWT token.
     */
    @Test
    void testGetUserIdFromJWT_EmptyToken() {
        String emptyToken = "";
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.getUserIdFromJWT(emptyToken);
        });
    }


    /**
     * Test case for an invalid JWT token.
     * This test verifies that the getUserIdFromJWT method throws a MalformedJwtException
     * when provided with an invalid JWT token.
     */
    @Test
    void testGetUserIdFromJWT_InvalidToken() {
        // Set the jwtSecret field to avoid NullPointerException
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String invalidToken = "invalid.jwt.token";

        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenProvider.getUserIdFromJWT(invalidToken);
        });
    }


    /**
     * Tests the validateToken method with an empty JWT claims string.
     * This test case verifies that the method returns false when presented with a token that has an empty claims string.
     */
    @Test
    void test_validateToken_emptyClaimsString() {
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String emptyClaimsToken = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        assertFalse(jwtTokenProvider.validateToken(emptyClaimsToken));
    }

    /**
     * Tests the validateToken method with an expired JWT token.
     * This test case verifies that the method returns false when presented with a token that has expired.
     */
    @Test
    void test_validateToken_expiredToken() {
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String expiredToken = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }

    /**
     * Tests the validateToken method with an invalid JWT signature.
     * This test case verifies that the method returns false when presented with a token that has an invalid signature.
     */
    @Test
    void test_validateToken_invalidSignature() {
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String invalidSignatureToken = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        assertFalse(jwtTokenProvider.validateToken(invalidSignatureToken));
    }

    /**
     * Tests the validateToken method with a malformed JWT token.
     * This test case verifies that the method returns false when presented with a token that is not properly formed.
     */
    @Test
    void test_validateToken_malformedToken() {
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String malformedToken = "this.is.not.a.valid.jwt.token";
        assertFalse(jwtTokenProvider.validateToken(malformedToken));
    }

    /**
     * Tests the validateToken method with an unsupported JWT token.
     * This test case verifies that the method returns false when presented with a token that uses an unsupported algorithm or format.
     */
    @Test
    void test_validateToken_unsupportedToken() {
        jwtTokenProvider.jwtSecret = "testSecretKeyWithAtLeast32Characters";
        String unsupportedToken = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        assertFalse(jwtTokenProvider.validateToken(unsupportedToken));
    }

}
