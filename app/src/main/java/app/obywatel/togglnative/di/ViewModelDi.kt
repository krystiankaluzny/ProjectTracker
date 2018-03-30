package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.UsersService
import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.user.UsersViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UsersViewModelModule {

    @Provides
    @ViewScope
    fun provideUsersService(jTogglFactory: JTogglFactory) = UsersService(jTogglFactory)

    @Provides
    @ViewScope
    fun providesUsersViewModel(usersService: UsersService): UsersViewModel = UsersViewModel(usersService)
}

@ViewScope
@Subcomponent(modules = [(UsersViewModelModule::class)])
interface UsersViewModelComponent {

    fun inject(userActivity: UserActivity)
}