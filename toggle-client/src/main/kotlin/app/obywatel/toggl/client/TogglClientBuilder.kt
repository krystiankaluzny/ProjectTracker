package app.obywatel.toggl.client

import app.obywatel.toggl.client.impl.TogglClientImpl
import ch.simas.jtoggl.JToggl
import javax.ws.rs.client.Client

class TogglClientBuilder(val androidSupport: Boolean) {

    fun build(apiToken: String): TogglClient {

        val jToggl: JToggl = when {
            androidSupport -> AndroidJToggle(apiToken)
            else -> JToggl(apiToken)
        }

        jToggl.throttlePeriod = 1000L
        jToggl.switchLoggingOn()

        return TogglClientImpl(jToggl)
    }

    internal class AndroidJToggle(apiToken: String) : JToggl(apiToken) {

        override fun prepareApiClient(): Client {
            val apiClient: Client = super.prepareApiClient()
            apiClient.register(JToggl.AndroidFriendlyFeature())

            return apiClient
        }
    }
}
