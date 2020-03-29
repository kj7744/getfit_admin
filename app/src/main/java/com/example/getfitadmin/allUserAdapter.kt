package com.example.getfitadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.showuser.view.*

class allUserAdapter(var context:Context,var list:ArrayList<mem>):RecyclerView.Adapter<displayuserHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): displayuserHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.showuser,parent,false)
        return displayuserHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: displayuserHolder, position: Int) {
        if(list[position].is_selected==0){
            holder.itemview.s_send.visibility=View.GONE
        }
        else{
            holder.itemview.s_send.visibility=View.VISIBLE
        }
        holder.itemview.showrel.setOnLongClickListener {
            if(holder.itemview.s_send.visibility==View.VISIBLE){
                holder.itemview.s_send.visibility=View.GONE
                list[position].is_selected=0
            }
            else{
                list[position].is_selected=1
                holder.itemview.s_send.visibility=View.VISIBLE
            }
            true
        }
        holder.itemview.mem_name.text = list[position].name
        holder.itemview.mem_email.text = list[position].email
    }
    fun update(list:ArrayList<mem>){
        this.list=list
        notifyDataSetChanged()
    }
}
class displayuserHolder(view:View):RecyclerView.ViewHolder(view){
    var itemview=view

}