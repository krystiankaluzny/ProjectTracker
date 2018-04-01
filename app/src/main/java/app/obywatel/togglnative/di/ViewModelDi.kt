package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.AddingUserService
import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.UserSelectionService
import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserViewModelModule {

    @ViewScope
    @Provides
    fun provideAddingUsersService(jTogglFactory: JTogglFactory) = AddingUserService(jTogglFactory)

    @ViewScope
    @Provides
    fun providesUsersViewModel(userSelectionService: UserSelectionService, addingUserService: AddingUserService): UserViewModel {
        return UserViewModel(userSelectionService, addingUserService)
    }
}

@ViewScope
@Subcomponent(modules = [(UserViewModelModule::class)])
interface UserViewModelComponent {

    fun inject(userActivity: UserActivity)
}