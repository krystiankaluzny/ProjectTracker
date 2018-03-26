package app.obywatel.togglnative.di

import android.content.Context
import app.obywatel.togglnative.di.scope.ApplicationScope
import dagger.Component
import dagger.Module
import dagger.Provides


@Module
class ApplicationModule(context: Context) {

    private val appContext: Context = context.applicationContext

    @ApplicationScope
    @Provides
    fun provideApplicationContext() = appContext
}

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun appContext(): Context

    fun plus(userViewModelModule: UserViewModelModule): UserViewModelComponent
}