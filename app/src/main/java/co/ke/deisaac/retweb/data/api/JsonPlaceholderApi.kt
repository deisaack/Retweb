package co.ke.deisaac.retweb.data.api;

import co.ke.deisaac.retweb.data.model.Comment
import co.ke.deisaac.retweb.data.model.Post
import retrofit2.Call
import retrofit2.http.*


public interface JsonPlaceholderApi {
    @GET("posts")
    fun getPosts(): List<Post?>

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    @GET("posts/{id}")
    fun getPost(@Path("id") postId: Int): Call<Post?>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int): Call<List<Comment?>?>?

    @GET
    fun getComments(@Url url: String?): Call<List<Comment?>?>?

}
