package co.ke.deisaac.retweb.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import co.ke.deisaac.retweb.R
import co.ke.deisaac.retweb.data.model.Post
import co.ke.deisaac.retweb.data.repository.MainRepository
import co.ke.deisaac.retweb.data.utils.Status
import co.ke.deisaac.retweb.ui.base.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.MutableMap as MutableMap


internal class MainActivity : AppCompatActivity() {
    private val mainRepository = MainRepository()
    lateinit var resultTextView: TextView;
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTextView = findViewById(R.id.tv_result)
        setUpViewModel()
        setupObservers()
        getPost()
    }

    fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory())
            .get(MainViewModel::class.java)
    }

    private fun getPost() {
        lifecycleScope.launch {
            val call = mainRepository.getPost(1)
            call.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    if (!response.isSuccessful()) {
                        resultTextView.append( "\n\nGetPost\nCode: " + response.code())
                        return
                    }
                    val post: Post? = response.body()
                    val content = "Post id: " + post?.id + "\nUserId: " +
                            post?.userId + "\nTitle: " + post?.title + "\nText: " + post?.text
                    resultTextView.append(content)
                    createPost()
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    resultTextView.text = t.message
                }
            })
        }
    }

    private fun getPosts() {
        lifecycleScope.launch {
            val params: MutableMap<String?, String?> = HashMap()
            params["_sort"] = "userId"
            val call = mainRepository.getPosts(params)
            call?.enqueue(object : Callback<List<Post?>?> {
                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<List<Post?>?>, response: Response<List<Post?>?>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        resultTextView.append("List returned status : " + response.code().toString())
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val users = resource.data
                        resultTextView.append("Users " + users?.count().toString())
                        Log.d(TAG, resource.toString())
                    }
                    Status.ERROR -> {
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Log.d(TAG, "Loading")
                    }
                }
            }
        })
    }

    private fun createPost() {
        lifecycleScope.launch {
//            Option 1: Create a Post instance and submit it
//            val post = Post(23, "20", "Hi Title", text = "Hello world")
//            val call: Call<Post> = mainRepository.createPost(post)

//            Option 2: create a field map of the available fields and submit
//            val fields: MutableMap<String, String> = HashMap()
//            fields["userId"] = "29"
//            fields["title"] = "Title is this"
//            fields["body"] = "Happy new year"
//            val f = fields as Map<String?, String?>
//            val call: Call<Post?> = mainRepository.createPost(f)

//           Option 3: pass the required fields as parameters in the function
            val call: Call<Post?> = mainRepository.createPost(
                userId = 12, title = "Hey there title", text = "Fantastic text")
            call.enqueue(object : Callback<Post?> {
                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    if (!response.isSuccessful()) {
                        resultTextView.append("\n\nPost Code: " + response.code())
                        return
                    }
                    val resp = response.body()
                    var content = ""
                    content +=  "\n\n\nhhhhh" + resp.toString()
                    resultTextView.append(content)
                    getPosts()
                }

                override fun onFailure(call: Call<Post?>?, t: Throwable) {
                    resultTextView.append(t.message)
                }
            })
        }
    }
}

