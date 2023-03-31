package com.mcgregor.chimhauapp.di

import android.app.Application
import androidx.room.Room
import com.mcgregor.chimhauapp.database.ProductDatabase
import com.mcgregor.chimhauapp.repository.ProductRepository
import com.mcgregor.chimhauapp.repository.ProductRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductDatabase(app: Application): ProductDatabase {
        return Room.databaseBuilder(
            app,
            ProductDatabase::class.java,
            "product_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductRepository(db: ProductDatabase): ProductRepository {
        return ProductRepositoryImp(db.productDao)
    }
}