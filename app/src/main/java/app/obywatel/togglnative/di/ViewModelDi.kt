package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.model.service.user.AddingUserService
import app.obywatel.togglnative.model.service.user.UserSelectionService
import app.obywatel.togglnative.view.timer.TimerActivity
import app.obywatel.togglnative.view.user.UserActivity
import app.obywatel.togglnative.viewmodel.timer.TimerViewModel
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
    fun providesUserViewModel(userSelectionService: UserSelectionService, addingUserService: AddingUserService): UserViewModel {
        return UserViewModel(userSelectionService, addingUserService)
    }
}

@ViewScope
@Subcomponent(modules = [(UserViewModelModule::class)])
interface UserViewModelComponent {

    fun inject(userActivity: UserActivity)
}

@Module
class TimerViewModelModule {

    @ViewScope
    @Provides
    fun provideTimerViewModel(timerService: TimerService) = TimerViewModel(timerService)
}

@ViewScope
@Subcomponent(modules = [(TimerViewModelModule::class)])
interface TimerViewModelComponent {

    fun inject(timerActivity: TimerActivity)
}