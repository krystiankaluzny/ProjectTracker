package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.entity.User
import app.obywatel.togglnative.model.service.JTogglFactory
import app.obywatel.togglnative.model.service.timer.TimerService
import ch.simas.jtoggl.JToggl
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserModule(private val user: User) {

    @UserScope
    @Provides
    fun provideJToggl(jTogglFactory: JTogglFactory) = jTogglFactory.jToggl(user.apiToken)

    @UserScope
    @Provides
    fun provideTimerService(jToggl: JToggl) = TimerService(user, jToggl)
}

@UserScope
@Subcomponent(modules = [(UserModule::class)])
interface UserComponent {

    fun plus(timerViewModelModule: TimerViewModelModule): TimerViewModelComponent
}