package app.obywatel.toggl.client

import app.obywatel.toggl.client.internal.TogglClientImpl

class TogglClientBuilder {

    fun build(apiToken: String): TogglClient = TogglClientImpl(apiToken)

}
