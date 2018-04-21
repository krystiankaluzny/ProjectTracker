package app.obywatel.toggl.client.internal.retrofit

import app.obywatel.toggl.client.internal.retrofit.dto.Project
import app.obywatel.toggl.client.internal.retrofit.dto.UserResponse
import app.obywatel.toggl.client.internal.retrofit.dto.Workspace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface TogglApi {

    @GET("me")
    fun me(): Call<UserResponse>

    @GET("workspaces")
    fun workspaces(): Call<List<Workspace>>

    @GET("workspaces/{workspaceId}/projects")
    fun workspaceProjects(@Path("workspaceId") workspaceId: Long): Call<List<Project>>
}