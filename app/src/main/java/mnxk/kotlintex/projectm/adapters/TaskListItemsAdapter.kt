package mnxk.kotlintex.projectm.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import mnxk.kotlintex.projectm.activities.TaskListActivity
import mnxk.kotlintex.projectm.databinding.ItemTaskBinding
import mnxk.kotlintex.projectm.models.Task

open class TaskListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams =
            LinearLayout.LayoutParams(
                (
                    parent.width * 0.7
                ).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
        layoutParams.setMargins(
            (
                15.toDp().toPx()
            ),
            0,
            (
                15.toDp().toPx()
            ),
            0,
        )
        binding.root.layoutParams = layoutParams
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Log.d("TAG_ibCloseListName_enabled", holder.binding.ibCloseListName.isEnabled.toString())
            Log.d("TAG_ibDoneListName_enabled", holder.binding.ibDoneListName.isEnabled.toString())
            if (position == list.size - 1) {
                holder.binding.tvAddTaskList.visibility = View.VISIBLE
                holder.binding.llTaskItem.visibility = View.GONE
            } else {
                holder.binding.tvAddTaskList.visibility = View.GONE
                holder.binding.llTaskItem.visibility = View.VISIBLE
            }
            holder.binding.tvTaskListTitle.text = model.title
            holder.binding.tvAddTaskList.setOnClickListener {
                holder.binding.tvAddTaskList.visibility = View.GONE
                holder.binding.cvAddTaskListName.visibility = View.VISIBLE
                Log.e("TAG_tvAddTaskList", "Clicked")
            }
            holder.binding.ibCloseListName.setOnClickListener {
                Log.d("TAG_ibCloseListName_enabled", holder.binding.ibCloseListName.isEnabled.toString())
                Log.i("TAG_ibCloseListName", "Clicked")
                holder.binding.tvAddTaskList.visibility = View.VISIBLE
                holder.binding.cvAddTaskListName.visibility = View.GONE
            }
            holder.binding.ibDoneListName.setOnClickListener {
                Log.d("TAG_ibDoneListName_enabled", holder.binding.ibDoneListName.isEnabled.toString())
                Log.d("TAG_ibDoneListName", "Clicked")
                val listName = holder.binding.etTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.createTaskList(listName)
                    }
                } else {
                    Toast.makeText(context, "Please enter list name.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // make viewholder only occupy 70% of the screen
    private fun Int.toDp(): Int =
        (
            this * context.resources.displayMetrics.density
        ).toInt()

    private fun Int.toPx(): Int =
        (
            this / context.resources.displayMetrics.density
        ).toInt()

    // viewholder class
    inner class MyViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)
}
