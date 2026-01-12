package it.unibo.discoverit.utils.hasher

/**
 * Helper interface for hashing passwords.
 */
interface PasswordHasher {
    /**
     * Hashes the given password.
     *
     * @param password The password to hash.
     * @return The hashed password.
     */
    fun hashPassword(password: String): String

    /**
     * Verifies if the given password matches the given hashed password.
     *
     * @param password The password to verify.
     * @param hashedPassword The hashed password to compare against.
     */
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}