package app.obywatel.togglnative.di

import app.obywatel.togglnative.model.service.JTogglFactory
import ch.simas.jtoggl.domain.User
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class UserModule(private val user: User) {

    @UserScope
    @Provides
    fun provideJToggl(jTogglFactory: JTogglFactory) = jTogglFactory.jToggl(user.apiToken)
}

@UserScope
@Subcomponent(modules = [(UserModule::class)])
interface UserComponent {

}