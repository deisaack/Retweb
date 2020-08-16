package co.ke.deisaac.retweb.data.api

import co.ke.deisaac.retweb.data.model.User
import retrofit2.http.GET

interface MockApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}