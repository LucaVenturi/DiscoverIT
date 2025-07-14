package it.unibo.discoverit

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import it.unibo.discoverit.data.database.DatabaseData
import it.unibo.discoverit.data.database.DiscoverItDatabase
import it.unibo.discoverit.data.repositories.*
import it.unibo.discoverit.ui.screens.categorydetails.CategoryDetailsViewModel
import it.unibo.discoverit.ui.screens.home.HomeViewModel
import it.unibo.discoverit.ui.screens.login.LoginViewModel
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.ui.screens.poidetails.POIDetailsViewModel
import it.unibo.discoverit.ui.screens.registration.RegistrationViewModel
import it.unibo.discoverit.ui.screens.settings.SettingsViewModel
import it.unibo.discoverit.ui.screens.social.SocialViewModel
import it.unibo.discoverit.ui.screens.userdetail.UserDetailViewModel
import it.unibo.discoverit.utils.hasher.BCryptHasher
import it.unibo.discoverit.utils.hasher.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    //Datastore
    single { get<Context>().dataStore }

    // Password Hasher BCrypt
    single<PasswordHasher> { BCryptHasher() }

    // Database
    single {
        Room.databaseBuilder(
            get(),
            DiscoverItDatabase::class.java,
            "discoverit_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val json = get<Context>().assets.open("database_init.json")
                                .bufferedReader().use { it.readText() }

                            val data = Json.decodeFromString<DatabaseData>(json)

                            with(get<DiscoverItDatabase>()) {
                                categoriesDAO().insertAll(data.categories)
                                pointsOfInterestDAO().insertAll(data.pointsOfInterest)
                                usersDAO().insertAll(data.users)
                                achievementsDao().insertAll(data.achievements)
                                visitsDao().insertAll(data.visits)
                                friendshipsDao().insertAll(data.friendships)
                            }
                            Log.d("DB_INIT", "Database popolato con successo!")
                        } catch (e: Exception) {
                            Log.e("DB_INIT", "Errore nel popolamento", e)
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single { get<DiscoverItDatabase>().categoriesDAO() }
    single { get<DiscoverItDatabase>().pointsOfInterestDAO() }
    single { get<DiscoverItDatabase>().usersDAO() }
    single { get<DiscoverItDatabase>().achievementsDao() }
    single { get<DiscoverItDatabase>().visitsDao() }
    single { get<DiscoverItDatabase>().friendshipsDao() }

    // Repository
    single { CategoryRepository(get(), get()) }
    single { PointOfInterestRepository(get(), get()) }
    single { UserRepository(get(), get()) }
    single { AchievementRepository(get(), get()) }
    single { SettingsRepository(get()) }

    // ViewModel
    single { UserViewModel(get()) }
    viewModel { LoginViewModel(get(), get())}
    viewModel { RegistrationViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoryDetailsViewModel(get()) }
    viewModel { POIDetailsViewModel(get(), get()) }
    viewModel { (currentUserId: Long) ->
        SocialViewModel(get(), get(), currentUserId)
    }
    viewModel { (userId: Long) ->
        UserDetailViewModel(userId, get())
    }
    viewModel { SettingsViewModel(get()) }
}