package app.obywatel.togglnative.di

import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.user.UsersViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UsersViewModelModule {

    @Provides
    @ViewScope
    fun providesUsersViewModel(): UsersViewModel = UsersViewModel()
}

@ViewScope
@Subcomponent(modules = [(UsersViewModelModule::class)])
interface UsersViewModelComponent {

    fun inject(userActivity: UserActivity)
}