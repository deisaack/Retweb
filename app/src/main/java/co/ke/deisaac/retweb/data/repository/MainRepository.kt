package co.ke.deisaac.retweb.data.repository
import co.ke.deisaac.retweb.data.api.RetrofitBuilder
import co.ke.deisaac.retweb.data.model.Post


class MainRepository() {
    private val jsonPlaceholderApi = RetrofitBuilder.jsonPlaceholderApi
    private val mockApi = RetrofitBuilder.mockApi

    suspend fun getUsers() = mockApi.getUsers()
    suspend fun getPost(postId: Int) = jsonPlaceholderApi.getPost(postId)
    suspend fun createPost(fields: Map<String?, String?>) = jsonPlaceholderApi.createPost(fields)
    suspend fun createPost(post: Post) = jsonPlaceholderApi.createPost(post)
    suspend fun createPost(userId: Int, title: String, text: String) = jsonPlaceholderApi.createPost(userId, title, text)
    suspend fun getPosts() = jsonPlaceholderApi.getPosts()
    suspend fun getPosts(parameters: Map<String?, String?>) = jsonPlaceholderApi.getPosts(parameters)
}