package mnxk.kotlintex.projectm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
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
            if (position == list.size - 1) {
                holder.binding.tvAddTaskList.visibility = View.VISIBLE
                holder.binding.llTaskItem.visibility = View.GONE
            } else {
                holder.binding.tvAddTaskList.visibility = View.GONE
                holder.binding.llTaskItem.visibility = View.VISIBLE
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
    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
}
