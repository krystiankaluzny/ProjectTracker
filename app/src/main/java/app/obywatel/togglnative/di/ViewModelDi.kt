package app.obywatel.togglnative.di

import app.obywatel.togglnative.di.scope.ActivityScope
import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserViewModelModule {

    @Provides
    @ActivityScope
    fun providesUsersViewModel(): UserViewModel = UserViewModel()
}

@ActivityScope
@Subcomponent(modules = arrayOf(UserViewModelModule::class))
interface UserViewModelComponent {

    fun inject(userActivity: UserActivity)
}