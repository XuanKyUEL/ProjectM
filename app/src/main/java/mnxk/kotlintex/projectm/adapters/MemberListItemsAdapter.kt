package mnxk.kotlintex.projectm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.models.User

open class MemberListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<User>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_member,
                parent,
                false,
            ),
        )
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
            if (model.image.isEmpty()) {
                holder.itemView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.iv_member_image)
                    .setImageDrawable(
                        context.resources.getDrawable(R.drawable.ic_user_place_holder),
                    )
            } else {
                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.itemView.findViewById(R.id.iv_member_image))

                holder.itemView.findViewById<TextView>(R.id.tv_member_name).text = model.name
                holder.itemView.findViewById<TextView>(R.id.tv_member_email).text = model.email
            }
        }
    }

    inner class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view)
}
