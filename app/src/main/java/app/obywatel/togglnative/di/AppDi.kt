package app.obywatel.togglnative.di

import android.content.Context
import app.obywatel.toggl.client.TogglClientBuilder
import app.obywatel.togglnative.model.service.user.UserService
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(context: Context) {

    private val appContext: Context = context.applicationContext
    private val togglClientBuilder = TogglClientBuilder()

    @ApplicationScope
    @Provides
    fun provideApplicationContext() = appContext

    @ApplicationScope
    @Provides
    fun provideToggleClientBuilder() = togglClientBuilder

    @ApplicationScope
    @Provides
    fun provideUserSelectionService() = UserService(togglClientBuilder)
}

@ApplicationScope
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun plus(userViewModelModule: UserViewModelModule): UserViewModelComponent

    fun plus(userModule: UserModule): UserComponent

    fun userSelectionService() : UserService
}