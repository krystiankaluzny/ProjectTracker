package org.projecttracker.di

import android.content.Context
import org.projecttracker.model.service.user.UserService
import dagger.Component
import dagger.Module
import dagger.Provides
import org.ktoggl.TogglClientBuilder
import org.ktoggl.android.AndroidTogglClientBuilder

@Module
class ApplicationModule(context: Context) {

    private val appContext: Context = context.applicationContext
    private val togglClientBuilder: TogglClientBuilder = AndroidTogglClientBuilder()

    @ApplicationScope
    @Provides
    fun provideApplicationContext() = appContext

    @ApplicationScope
    @Provides
    fun provideToggleClientBuilder() = togglClientBuilder

    @ApplicationScope
    @Provides
    fun provideUserService() = UserService(togglClientBuilder)
}

@ApplicationScope
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun plus(userViewModelModule: UserViewModelModule): UserViewModelComponent

    fun plus(userModule: UserModule): UserComponent

    fun userService(): UserService
}