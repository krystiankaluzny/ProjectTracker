package org.projecttracker.di

import org.ktoggl.TogglClient
import org.ktoggl.TogglClientBuilder
import org.projecttracker.model.entity.User
import org.projecttracker.model.service.timer.TimerService
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserModule(private val user: User) {

    @UserScope
    @Provides
    fun provideTogglClient(togglClientBuilder: TogglClientBuilder) = togglClientBuilder.build(user.apiToken)

    @UserScope
    @Provides
    fun provideTimerService(togglClient: TogglClient) = TimerService(user, togglClient)
}

@UserScope
@Subcomponent(modules = [(UserModule::class)])
interface UserComponent {

    fun plus(timerViewModelModule: TimerViewModelModule): TimerViewModelComponent
}