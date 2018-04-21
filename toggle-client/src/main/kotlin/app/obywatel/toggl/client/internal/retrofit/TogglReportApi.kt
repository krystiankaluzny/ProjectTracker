package app.obywatel.toggl.client.internal.retrofit

import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface TogglReportApi {

    @GET("weekly")
    fun weekly(@QueryMap queryParams: Map<String, String>)

    @GET("details")
    fun detailed(@QueryMap queryParams: Map<String, String>)

    @GET("summary")
    fun summary(@QueryMap queryParams: Map<String, String>)
}