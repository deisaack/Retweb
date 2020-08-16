package co.ke.deisaac.retweb.data.api;

import co.ke.deisaac.retweb.data.model.Comment
import co.ke.deisaac.retweb.data.model.Post
import retrofit2.Call
import retrofit2.http.*


public interface JsonPlaceholderApi {
    @GET("posts")
    fun getPosts(): List<Post?>

    @GET("posts")
    fun getPosts(@QueryMap parameters: Map<String?, String?>?): Call<List<Post?>?>?

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(@FieldMap fields: Map<String?, String?>?): Call<Post?>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(
        @Field("userId") userId: Int,
        @Field("title") title: String?,
        @Field("body") text: String?
    ): Call<Post?>

    @GET("posts/{id}")
    fun getPost(@Path("id") postId: Int): Call<Post?>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int): Call<List<Comment?>?>?

    @GET
    fun getComments(@Url url: String?): Call<List<Comment?>?>?

    @PUT("posts/{id}")
    fun putPost(@Path("id") id: Int, @Body post: Post?): Call<Post?>

    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body post: Post?): Call<Post?>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Void?>

}
