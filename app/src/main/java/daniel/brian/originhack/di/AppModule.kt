package daniel.brian.originhack.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.brian.originhack.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = Firebase.firestore

    @Provides
    fun provideIntroductionSP(
        application: Application
    ): SharedPreferences = application.getSharedPreferences(
        Constants.INTRODUCTION_SP,
        Context.MODE_PRIVATE
    )


        @Provides
        @Singleton
        fun provideGenerativeModel(@ApplicationContext context: Context): GenerativeModel {
            return GenerativeModel(
                modelName = "gemini-1.5-pro",
                apiKey = "AIzaSyACmDhMhpztMuKkojKjwqNcH1JKlj2-h4o"
            )
        }

}