package com.example.jrp_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.option_entry.view.*
import kotlinx.android.synthetic.main.receipt_record_row.view.*
import kotlinx.android.synthetic.main.row_users.view.*


class UsersAdapter(options: FirestoreRecyclerOptions<UsersModel>) :
    FirestoreRecyclerAdapter<UsersModel, UsersAdapter.UsersAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapterVH {
//        return UsersAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.row_users,parent,false))
        return UsersAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.receipt_record_row,parent,false))
    }

    override fun onBindViewHolder(holder: UsersAdapterVH, position: Int, model: UsersModel) {
//        holder.userName.text = model.userName
//        holder.user_email.text = model.userEmail
//        holder.user_mobile.text = model.userMobile

        holder.from.text = model.FROM
        holder.to.text = model.TO
        holder.rn.text = model.Reciept_Number.toString()
        holder.amt.text = model.AMOUNT.toString()
    }

    class UsersAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    var userName = itemView.tvUsername
//    var user_email = itemView.tvEmail
//    var user_mobile = itemView.tvMobile

    var from = itemView.RtvFromFS
    var rn = itemView.RtvNFS
    var to = itemView.RtvToFS
    var amt = itemView.RtvRsFS



    }


}