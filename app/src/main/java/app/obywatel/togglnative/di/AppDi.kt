package app.obywatel.togglnative.di

import android.content.Context
import app.obywatel.togglnative.model.service.JTogglFactory
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(context: Context) {

    private val appContext: Context = context.applicationContext
    private val jTogglFactory = JTogglFactory()

    @ApplicationScope
    @Provides
    fun provideApplicationContext() = appContext

    @ApplicationScope
    @Provides
    fun provideJTogglFactory() = jTogglFactory
}

@ApplicationScope
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun appContext(): Context

    fun plus(usersViewModelModule: UsersViewModelModule): UsersViewModelComponent
}