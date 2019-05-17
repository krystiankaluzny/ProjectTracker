package org.projecttracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import org.projecttracker.model.service.timer.TimerService
import org.projecttracker.model.service.user.UserService
import org.projecttracker.view.settings.SettingsActivity
import org.projecttracker.view.timer.TimerActivity
import org.projecttracker.viewmodel.NetworkStateMonitor
import org.projecttracker.viewmodel.timer.DailyTimerViewModel
import org.projecttracker.viewmodel.user.UserViewModel

@Module
class UserViewModelModule(private val context: Context) {

    @ViewScope
    @Provides
    fun providesNetworkStateMonitor(): NetworkStateMonitor = NetworkStateMonitor(context)

    @ViewScope
    @Provides
    fun providesUserViewModel(userService: UserService, networkStateMonitor: NetworkStateMonitor): UserViewModel = UserViewModel(userService, networkStateMonitor)
}

@ViewScope
@Subcomponent(modules = [(UserViewModelModule::class)])
interface UserViewModelComponent {

    fun inject(settingsActivity: SettingsActivity)
}

@Module
class TimerViewModelModule(private val context: Context) {

    @ViewScope
    @Provides
    fun providesNetworkStateMonitor(): NetworkStateMonitor = NetworkStateMonitor(context)

    @ViewScope
    @Provides
    fun provideDailyTimerViewModel(timerService: TimerService, networkStateMonitor: NetworkStateMonitor) = DailyTimerViewModel(timerService, networkStateMonitor)
}

@ViewScope
@Subcomponent(modules = [(TimerViewModelModule::class)])
interface TimerViewModelComponent {

    fun inject(timerActivity: TimerActivity)
}