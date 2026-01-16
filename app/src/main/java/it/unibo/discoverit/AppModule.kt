package it.unibo.discoverit

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import it.unibo.discoverit.data.database.DatabaseData
import it.unibo.discoverit.data.database.DiscoverItDatabase
import it.unibo.discoverit.data.repositories.*
import it.unibo.discoverit.ui.screens.account.AccountSettingsViewModel
import it.unibo.discoverit.ui.screens.categorydetails.CategoryDetailsViewModel
import it.unibo.discoverit.ui.screens.home.HomeViewModel
import it.unibo.discoverit.ui.screens.login.LoginViewModel
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.ui.screens.poidetails.POIDetailsViewModel
import it.unibo.discoverit.ui.screens.registration.RegistrationViewModel
import it.unibo.discoverit.ui.screens.sessioncheck.SessionCheckViewModel
import it.unibo.discoverit.ui.screens.settings.SettingsViewModel
import it.unibo.discoverit.ui.screens.social.SocialViewModel
import it.unibo.discoverit.ui.screens.userdetail.UserDetailViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import it.unibo.discoverit.utils.accountservice.AccountServiceImpl
import it.unibo.discoverit.utils.hasher.BCryptHasher
import it.unibo.discoverit.utils.hasher.PasswordHasher
import it.unibo.discoverit.utils.location.LocationService
import it.unibo.discoverit.utils.profilepic.ProfilePicStorageHelper
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

    // Helper per salvare le foto profilo degli utenti
    single { ProfilePicStorageHelper(get()) }

    // Servizio di geolocalizzazione per ottenere la posizione dell'utente
    single { LocationService(get()) }

    // Servizio che gestisce login registrazione e logout.
    single<AccountService> { AccountServiceImpl(get(), get()) }


    // Database
    single {
        Room.databaseBuilder(
            get(),
            DiscoverItDatabase::class.java,
            "discoverit_database_v10",
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Popola SOLO quando il database viene creato per la prima volta
                    populateDatabase(get())
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
    single<SessionRepository> { SessionRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<PointOfInterestRepository> { PointOfInterestRepositoryImpl( get(), get() ) }
    single<UserRepository> { UserRepositoryImpl( get(), get(), get() ) }
    single<AchievementRepository> { AchievementRepositoryImpl( get(), get() ) }
    single<SettingsRepository> { SettingsRepositoryImpl( get() ) }
    single<AccountSettingsRepository> { AccountSettingsRepositoryImpl( get(), get() ) }

    // ViewModel
    single { UserViewModel() }
    viewModel { LoginViewModel(get(), get())}
    viewModel { RegistrationViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { (categoryId: Long) ->
        CategoryDetailsViewModel(get(), get(), categoryId)
    }
    viewModel { (poiId: Long) ->
        POIDetailsViewModel(get(), get(), poiId, get(), get())
    }
    viewModel { (currentUserId: Long) ->
        SocialViewModel(get(), currentUserId)
    }
    viewModel { (userId: Long) ->
        UserDetailViewModel(userId, get())
    }
    single { SettingsViewModel(get()) }
    viewModel { AccountSettingsViewModel(get(), get(), get(), get()) }
    viewModel { SessionCheckViewModel(get(), get(), get()) }
}

private fun populateDatabase(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val json = context.assets.open("database_init.json")
            .bufferedReader().use { it.readText() }

        val data = Json.decodeFromString<DatabaseData>(json)
        val database = org.koin.core.context.GlobalContext.get().get<DiscoverItDatabase>()

        database.categoriesDAO().insertAll(data.categories)
        database.pointsOfInterestDAO().insertAll(data.pointsOfInterest)
        database.usersDAO().insertAll(data.users)
        database.achievementsDao().insertAll(data.achievements)
        database.visitsDao().insertAll(data.visits)
        database.friendshipsDao().insertAll(data.friendships)
    }
}