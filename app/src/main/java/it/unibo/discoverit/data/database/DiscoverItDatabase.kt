package it.unibo.discoverit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unibo.discoverit.data.database.daos.AchievementDAO
import it.unibo.discoverit.data.database.daos.CategoryDAO
import it.unibo.discoverit.data.database.daos.FriendshipDAO
import it.unibo.discoverit.data.database.daos.PointOfInterestDAO
import it.unibo.discoverit.data.database.daos.UserDAO
import it.unibo.discoverit.data.database.daos.VisitDAO
import it.unibo.discoverit.data.database.entities.*

/**
 * It's the Room database for the DiscoverIt application.
 * It contains the following entities:
 * - [Achievement]
 * - [Category]
 * - [Friendship]
 * - [PointOfInterest]
 * - [User]
 * - [Visit]
 * - [UserAchievementProgress]
 *
 * @property achievementsDao the [DAO][AchievementDAO] for the [Achievement] entity.
 * @property categoriesDAO the [DAO][CategoryDAO] for the [Category] entity.
 * @property pointsOfInterestDAO the [DAO][PointOfInterestDAO] for the [PointOfInterest] entity.
 * @property usersDAO the [DAO][UserDAO] for the [User] entity.
 * @property visitsDao the [DAO][VisitDAO] for the [Visit] entity.
 * @property friendshipsDao the [DAO][FriendshipDAO] for the [Friendship] entity.
 */
@Database(
    entities = [
        Achievement::class,
        Category::class,
        Friendship::class,
        PointOfInterest::class,
        User::class,
        Visit::class,
        UserAchievementProgress::class
    ],
    version = 10,
    exportSchema = false
)
abstract class DiscoverItDatabase : RoomDatabase() {
    abstract fun categoriesDAO(): CategoryDAO
    abstract fun pointsOfInterestDAO(): PointOfInterestDAO
    abstract fun usersDAO(): UserDAO
    abstract fun achievementsDao(): AchievementDAO
    abstract fun visitsDao(): VisitDAO
    abstract fun friendshipsDao(): FriendshipDAO
}