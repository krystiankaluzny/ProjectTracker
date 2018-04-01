package app.obywatel.togglnative.di

import android.content.Context
import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.user.UserSelectionService
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

    @ApplicationScope
    @Provides
    fun provideUserSelectionService() = UserSelectionService()
}

@ApplicationScope
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun plus(userViewModelModule: UserViewModelModule): UserViewModelComponent

    fun plus(userModule: UserModule): UserComponent
}