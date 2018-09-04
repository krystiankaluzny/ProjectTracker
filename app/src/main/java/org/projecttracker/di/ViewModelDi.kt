package org.projecttracker.di

import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.model.service.user.UserService
import org.projecttracker.view.settings.SettingsActivity
import org.projecttracker.view.timer.TimerActivity
import org.projecttracker.viewmodel.timer.DailyTimerViewModel
import org.projecttracker.viewmodel.user.UserViewModel
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