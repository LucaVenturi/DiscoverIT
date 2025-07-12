package it.unibo.discoverit.utils.hasher

import org.mindrot.jbcrypt.BCrypt

class BCryptHasher : PasswordHasher {
    override fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}