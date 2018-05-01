package app.obywatel.toggl.client

import app.obywatel.toggl.client.internal.TogglClientImpl
import app.obywatel.toggl.client.internal.retrofit.RetrofitFactory
import app.obywatel.toggl.client.internal.retrofit.TogglApi
import app.obywatel.toggl.client.internal.retrofit.TogglReportApi

class TogglClientBuilder {

    private fun togglApi(apiToken: String) = RetrofitFactory.create<TogglApi>(apiToken, "https://www.toggl.com/api/v8/")
    private fun togglReportApi(apiToken: String) = RetrofitFactory.create<TogglReportApi>(apiToken, "https://toggl.com/reports/api/v2/")

    fun build(apiToken: String): TogglClient = TogglClientImpl(togglApi(apiToken), togglReportApi(apiToken))

}
