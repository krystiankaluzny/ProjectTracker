package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.timer.TimerService
import app.obywatel.togglnative.model.service.user.UserService
import app.obywatel.togglnative.view.settings.SettingsActivity
import app.obywatel.togglnative.view.timer.TimerActivity
import app.obywatel.togglnative.viewmodel.timer.DailyTimerViewModel
import app.obywatel.togglnative.viewmodel.user.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserViewModelModule {

    @ViewScope
    @Provides
    fun providesUserViewModel(userService: UserService): UserViewModel = UserViewModel(userService)
}

@ViewScope
@Subcomponent(modules = [(UserViewModelModule::class)])
interface UserViewModelComponent {

    fun inject(settingsActivity: SettingsActivity)
}

@Module
class TimerViewModelModule {

    @ViewScope
    @Provides
    fun provideDailyTimerViewModel(timerService: TimerService) = DailyTimerViewModel(timerService)
}

@ViewScope
@Subcomponent(modules = [(TimerViewModelModule::class)])
interface TimerViewModelComponent {

    fun inject(timerActivity: TimerActivity)
}