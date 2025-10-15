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
    version = 5
)
abstract class DiscoverItDatabase : RoomDatabase() {
    abstract fun categoriesDAO(): CategoryDAO
    abstract fun pointsOfInterestDAO(): PointOfInterestDAO
    abstract fun usersDAO(): UserDAO
    abstract fun achievementsDao(): AchievementDAO
    abstract fun visitsDao(): VisitDAO
    abstract fun friendshipsDao(): FriendshipDAO
}