package mnxk.kotlintex.projectm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ItemBoardBinding
import mnxk.kotlintex.projectm.models.Board

open class BoardItemsAdapter(
    private val context: Context,
    private val list: ArrayList<Board>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private class MyViewHolder(val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemBoardBinding
                .inflate(
                    LayoutInflater
                        .from(context),
                    parent,
                    false,
                )
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
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.binding.civBoardImage)
            holder.binding.tvBoardName.text = model.name
            holder.binding.tvCreatedBy.text =
                context.getString(R.string.created_by, model.createdBy)

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(
            position: Int,
            model: Board,
        )
    }
}
