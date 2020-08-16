package co.ke.deisaac.retweb.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.ke.deisaac.retweb.ExampleAdapter
import co.ke.deisaac.retweb.ExampleItem
import co.ke.deisaac.retweb.R


class MainActivity : AppCompatActivity() {
    private var mExampleList: ArrayList<ExampleItem>? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var buttonInsert: Button? = null
    private var buttonRemove: Button? = null
    private var editTextInsert: EditText? = null
    private var editTextRemove: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createExampleList()
        buildRecyclerView()
        buttonInsert = findViewById(R.id.button_insert)
        buttonRemove = findViewById(R.id.button_remove)
        editTextInsert = findViewById(R.id.edittext_insert)
        editTextRemove = findViewById(R.id.edittext_remove)
        buttonInsert?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val position = editTextInsert?.getText().toString().toInt()
                insertItem(position)
            }
        })
        buttonRemove?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val position = editTextRemove?.getText().toString().toInt()
                removeItem(position)
            }
        })
    }

    fun insertItem(position: Int) {
        mExampleList!!.add(
            position,
            ExampleItem(R.drawable.ic_baseline_monochrome_photos_24, "New Item At Position$position", "This is Line 2")
        )
        mAdapter!!.notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        mExampleList?.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
    }

    private fun createExampleList() {
        mExampleList = ArrayList()
        mExampleList!!.add(ExampleItem(R.drawable.ic_baseline_monochrome_photos_24, "Line 1", "Line 2"))
        mExampleList!!.add(ExampleItem(R.drawable.ic_baseline_monochrome_photos_24, "Line 3", "Line 4"))
        mExampleList!!.add(ExampleItem(R.drawable.ic_baseline_monochrome_photos_24, "Line 5", "Line 6"))
    }

    private fun buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = ExampleAdapter(mExampleList!!)
        mRecyclerView?.setLayoutManager(mLayoutManager)
        mRecyclerView?.setAdapter(mAdapter)
    }
}