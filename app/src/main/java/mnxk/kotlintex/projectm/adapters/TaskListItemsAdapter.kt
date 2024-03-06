package mnxk.kotlintex.projectm.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import mnxk.kotlintex.projectm.R
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
                val listName = holder.binding.etTaskListName.text.toString()
                if (listName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.createTaskList(listName)
                        Log.d("TAG_ibDoneListName", "Clicked")
                    }
                } else {
                    Toast.makeText(context, "Please enter list name.", Toast.LENGTH_SHORT).show()
                    Log.e("TAG_ibDoneListName", "List name is empty.")
                }
            }
            holder.binding.ibEditListName.setOnClickListener {
                holder.binding.etEditTaskListName.setText(model.title)
                holder.binding.llTitleView.visibility = View.GONE
                holder.binding.cvEditTaskListName.visibility = View.VISIBLE
            }
            holder.binding.ibCloseEditableView.setOnClickListener {
                holder.binding.llTitleView.visibility = View.VISIBLE
                holder.binding.cvEditTaskListName.visibility = View.GONE
            }
            holder.binding.ibDoneEditListName.setOnClickListener {
                val listName = holder.binding.etEditTaskListName.text.toString()
                if (listName.isNotEmpty() && listName != model.title) {
                    if (context is TaskListActivity) {
                        context.updateTaskList(position, listName, model)
                    }
                } else if (listName == model.title) {
                    holder.binding.llTitleView.visibility = View.VISIBLE
                    holder.binding.cvEditTaskListName.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Please enter list name.", Toast.LENGTH_SHORT).show()
                }
            }
            holder.binding.ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(position)
            }
            holder.binding.tvAddCard.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.GONE
                holder.binding.cvAddCard.visibility = View.VISIBLE
            }
            holder.binding.ibCloseCardName.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.VISIBLE
                holder.binding.cvAddCard.visibility = View.GONE
            }
            holder.binding.ibDoneCardName.setOnClickListener {
                val cardName = holder.binding.etCardName.text.toString()
                if (cardName.isNotEmpty()) {
                    if (context is TaskListActivity) {
                        context.addCardToTaskList(position, cardName)
                    }
                } else {
                    Toast.makeText(context, "Please enter card name.", Toast.LENGTH_SHORT).show()
                }
            }
            holder.binding.rvCardList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            holder.binding.rvCardList.setHasFixedSize(true)
            val adapter = CardListItemsAdapter(context, model.cards)
            holder.binding.rvCardList.adapter = adapter
        }
    }

    // Alert the user if they are about to delete a task list
    private fun alertDialogForDeleteList(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete this task list?")
        builder.setIcon(R.drawable.delete_icon_fordialog)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()
            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
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
