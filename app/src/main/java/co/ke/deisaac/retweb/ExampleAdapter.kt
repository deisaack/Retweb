package co.ke.deisaac.retweb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class ExampleAdapter(private val mExampleList: ArrayList<ExampleItem>) :
    RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {

    class ExampleViewHolder(itemView: View) : ViewHolder(itemView) {
        var mImageView: ImageView
        var mTextView1: TextView
        var mTextView2: TextView

        init {
            mImageView = itemView.findViewById(R.id.imageView)
            mTextView1 = itemView.findViewById(R.id.textView)
            mTextView2 = itemView.findViewById(R.id.textView2)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExampleViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return ExampleViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: ExampleViewHolder,
        position: Int
    ) {
        val currentItem = mExampleList[position]
        holder.mImageView.setImageResource(currentItem.imageResource)
        holder.mTextView1.text = currentItem.text1
        holder.mTextView2.text = currentItem.text2
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

}