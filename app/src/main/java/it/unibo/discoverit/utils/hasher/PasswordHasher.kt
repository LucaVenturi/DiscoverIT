package it.unibo.discoverit.utils.hasher

interface PasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}