package mnxk.kotlintex.projectm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mnxk.kotlintex.projectm.databinding.ItemCardBinding
import mnxk.kotlintex.projectm.models.Card

open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mPositionDraggedFrom = -1
    private var mPositionDraggedTo = -1
    private var onClickListener: OnClickListener? = null

    inner class MyViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val model = list[position]
        if (holder is MyViewHolder) {
            holder.binding.tvCardName.text = model.name
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }
        }
    }
}
