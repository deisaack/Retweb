package co.ke.deisaac.retweb.data.repository
import co.ke.deisaac.retweb.data.api.RetrofitBuilder
import co.ke.deisaac.retweb.data.model.Post


class MainRepository() {
    private val jsonPlaceholderApi = RetrofitBuilder.jsonPlaceholderApi
    private val mockApi = RetrofitBuilder.mockApi

    suspend fun getUsers() = mockApi.getUsers()
    suspend fun getPost(postId: Int) = jsonPlaceholderApi.getPost(postId)
    suspend fun createPost(post: Post) = jsonPlaceholderApi.createPost(post)
    suspend fun getPosts() = jsonPlaceholderApi.getPosts()
}