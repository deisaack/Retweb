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
            val post = Post(23, "20", "Hi Title", text = "Hello world")
            val fields: MutableMap<String, String> = HashMap()
            fields["userId"] = "25"
            fields["title"] = "New Title"
            val call: Call<Post> = mainRepository.createPost(post)
            call.enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post?>) {
                    if (!response.isSuccessful()) {
                        resultTextView.append("\n\nPost Code: " + response.code())
                        return
                    }
                    val resp = response.body()
                    var content = ""
                    content +=  "\n\n\nhhhhh" + resp.toString()
                    resultTextView.append(content)
                }

                override fun onFailure(call: Call<Post?>?, t: Throwable) {
                    resultTextView.append(t.message)
                }
            })
        }
    }
}

