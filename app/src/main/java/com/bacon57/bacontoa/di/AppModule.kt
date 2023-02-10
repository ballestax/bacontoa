package com.bacon57.bacontoa.di

import com.bacon57.bacontoa.data.BaconApi
import com.bacon57.bacontoa.repository.ProductRepository
import com.bacon57.bacontoa.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideProductRepository(
        api: BaconApi
    ) = ProductRepository(api)

    @Singleton
    @Provides
    fun provideBaconApi(): BaconApi {
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(BaconApi::class.java)
    }
}