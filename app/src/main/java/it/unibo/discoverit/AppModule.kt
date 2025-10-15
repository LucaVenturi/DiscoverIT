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
import it.unibo.discoverit.ui.screens.account.AccountSettingsViewModel
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

    // Database
    single {
        Room.databaseBuilder(
            get(),
            DiscoverItDatabase::class.java,
            "discoverit_database_v4" // Cambia nome database per forzare ricreazione
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("DB_INIT", "onCreate callback chiamato")
                    populateDatabase(get())
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d("DB_INIT", "onOpen callback chiamato")
                    // Verifica se il database è vuoto e popolalo se necessario
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val database = get<DiscoverItDatabase>()
                            val categoriesCount = database.categoriesDAO().getCategoriesCount()
                            Log.d("DB_INIT", "Numero categorie trovate: $categoriesCount")

                            if (categoriesCount == 0) {
                                Log.d("DB_INIT", "Database vuoto, procedo con il popolamento")
                                populateDatabase(get())
                            } else {
                                Log.d("DB_INIT", "Database già popolato")
                            }
                        } catch (e: Exception) {
                            Log.e("DB_INIT", "Errore nel controllo/popolamento database", e)
                        }
                    }
                }
            }
        )
        .fallbackToDestructiveMigration() // Usa destructiveMigration solo per debug/sviluppo
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
    single { CategoryRepository(get()) }
    single { PointOfInterestRepository(get(), get()) }
    single { UserRepository(get(), get(), get()) }
    single { AchievementRepository(get(), get()) }
    single { SettingsRepository(get()) }
    single { AccountSettingsRepository(get(), get()) }

    // ViewModel
    single { UserViewModel(get()) }
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
    viewModel { AccountSettingsViewModel(get(), get(), get()) }
}

private fun populateDatabase(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.d("DB_INIT", "Inizio popolamento database")

            // Verifica che il file JSON esista
            val assetManager = context.assets
            val files = assetManager.list("")
            Log.d("DB_INIT", "File in assets: ${files?.joinToString()}")

            val json = context.assets.open("database_init.json")
                .bufferedReader().use { it.readText() }

            Log.d("DB_INIT", "JSON letto: ${json.take(100)}...") // Primi 100 caratteri

            val data = Json.decodeFromString<DatabaseData>(json)
            Log.d("DB_INIT", "JSON decodificato con successo")

            // Ottieni l'istanza del database tramite Koin (se disponibile)
            // o creane una temporanea
            val database = try {
                // Prova a ottenere l'istanza da Koin se disponibile
                org.koin.core.context.GlobalContext.get().get<DiscoverItDatabase>()
            } catch (e: Exception) {
                // Se Koin non è ancora inizializzato, crea un'istanza temporanea
                Room.databaseBuilder(
                    context,
                    DiscoverItDatabase::class.java,
                    "discoverit_database"
                ).build()
            }

            with(database) {
                Log.d("DB_INIT", "Inserimento categorie...")
                categoriesDAO().insertAll(data.categories)
                Log.d("DB_INIT", "Categorie inserite: ${data.categories.size}")

                Log.d("DB_INIT", "Inserimento POI...")
                pointsOfInterestDAO().insertAll(data.pointsOfInterest)
                Log.d("DB_INIT", "POI inseriti: ${data.pointsOfInterest.size}")

                Log.d("DB_INIT", "Inserimento utenti...")
                usersDAO().insertAll(data.users)
                Log.d("DB_INIT", "Utenti inseriti: ${data.users.size}")

                Log.d("DB_INIT", "Inserimento achievements...")
                achievementsDao().insertAll(data.achievements)
                Log.d("DB_INIT", "Achievements inseriti: ${data.achievements.size}")

                Log.d("DB_INIT", "Inserimento visite...")
                visitsDao().insertAll(data.visits)
                Log.d("DB_INIT", "Visite inserite: ${data.visits.size}")

                Log.d("DB_INIT", "Inserimento amicizie...")
                friendshipsDao().insertAll(data.friendships)
                Log.d("DB_INIT", "Amicizie inserite: ${data.friendships.size}")
            }

            Log.d("DB_INIT", "Database popolato con successo!")
        } catch (e: Exception) {
            Log.e("DB_INIT", "Errore nel popolamento database", e)
            e.printStackTrace()
        }
    }
}