package com.syncup.data.remote

import com.syncup.data.remote.dto.ActivityEventDto
import com.syncup.data.remote.dto.ApiResponse
import com.syncup.data.remote.dto.AuthTokenResponse
import com.syncup.data.remote.dto.CreateProjectRequest
import com.syncup.data.remote.dto.CreateTaskRequest
import com.syncup.data.remote.dto.FlagBlockerRequest
import com.syncup.data.remote.dto.LoginRequest
import com.syncup.data.remote.dto.LoginResponse
import com.syncup.data.remote.dto.ProjectDto
import com.syncup.data.remote.dto.PresenceDto
import com.syncup.data.remote.dto.SharedFileDto
import com.syncup.data.remote.dto.SignUpRequest
import com.syncup.data.remote.dto.SignUpResponse
import com.syncup.data.remote.dto.TaskDto
import com.syncup.data.remote.dto.UpdateTaskStatusRequest
import com.syncup.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SyncUpApi {
    // Authentication endpoints
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/refresh")
    suspend fun refreshToken(): AuthTokenResponse

    // User endpoints
    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): UserDto

    @GET("users/search")
    suspend fun searchUsers(@Query("email") email: String): List<UserDto>

    @GET("universities/{universityId}/members")
    suspend fun getUniversityMembers(@Path("universityId") universityId: String): List<UserDto>

    // Project endpoints
    @GET("projects")
    suspend fun getProjects(): List<ProjectDto>

    @GET("projects/{projectId}")
    suspend fun getProjectById(@Path("projectId") projectId: String): ProjectDto

    @POST("projects")
    suspend fun createProject(@Body request: CreateProjectRequest): ProjectDto

    @PUT("projects/{projectId}")
    suspend fun updateProject(
        @Path("projectId") projectId: String,
        @Body request: CreateProjectRequest
    ): ProjectDto

    @POST("projects/{projectId}/members/{userId}")
    suspend fun addMember(
        @Path("projectId") projectId: String,
        @Path("userId") userId: String
    ): ApiResponse<String>

    // Task endpoints
    @GET("projects/{projectId}/tasks")
    suspend fun getProjectTasks(@Path("projectId") projectId: String): List<TaskDto>

    @GET("tasks/{taskId}")
    suspend fun getTaskById(@Path("taskId") taskId: String): TaskDto

    @POST("projects/{projectId}/tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): TaskDto

    @PUT("tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: String,
        @Body request: TaskDto
    ): TaskDto

    @PUT("tasks/{taskId}/status")
    suspend fun updateTaskStatus(@Path("taskId") taskId: String, @Body request: UpdateTaskStatusRequest): TaskDto

    @POST("tasks/{taskId}/blocker")
    suspend fun flagBlocker(@Path("taskId") taskId: String, @Body request: FlagBlockerRequest): ApiResponse<String>

    @PUT("tasks/{taskId}/blocker/resolve")
    suspend fun resolveBlocker(@Path("taskId") taskId: String): ApiResponse<String>

    // Activity endpoints
    @GET("projects/{projectId}/activity")
    suspend fun getProjectActivity(@Path("projectId") projectId: String): List<ActivityEventDto>

    @GET("activity/global")
    suspend fun getGlobalActivity(): List<ActivityEventDto>

    @POST("projects/{projectId}/nudge/{userId}")
    suspend fun sendFriendlyNudge(
        @Path("projectId") projectId: String,
        @Path("userId") userId: String,
        @Body message: Map<String, String>
    ): ApiResponse<String>

    // File endpoints
    @GET("projects/{projectId}/files")
    suspend fun getProjectFiles(@Path("projectId") projectId: String): List<SharedFileDto>

    @POST("projects/{projectId}/files/upload")
    suspend fun uploadFile(@Path("projectId") projectId: String, @Body file: SharedFileDto): SharedFileDto

    // Presence endpoints
    @GET("presence")
    suspend fun getActiveUsers(): List<PresenceDto>

    @GET("projects/{projectId}/presence")
    suspend fun getProjectPresence(@Path("projectId") projectId: String): List<PresenceDto>

    @POST("presence/{projectId}/update")
    suspend fun updatePresence(@Path("projectId") projectId: String): ApiResponse<String>
}
