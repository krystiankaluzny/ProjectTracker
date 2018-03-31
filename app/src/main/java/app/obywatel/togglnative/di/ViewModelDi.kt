package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.UserService
import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserViewModelModule {

    @Provides
    @ViewScope
    fun provideUsersService(jTogglFactory: JTogglFactory) = UserService(jTogglFactory)

    @Provides
    @ViewScope
    fun providesUsersViewModel(userService: UserService): UserViewModel = UserViewModel(userService)
}

@ViewScope
@Subcomponent(modules = [(UserViewModelModule::class)])
interface UserViewModelComponent {

    fun inject(userActivity: UserActivity)
}