package com.example.mytest.Adapter

import com.example.mytest.Model.Roomlist
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest.Adapter.RoomAdapter.Viewholder
import com.example.mytest.sqlite.DataBaseHandler
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.mytest.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import java.util.ArrayList

class RoomAdapter(var context: Context, var list: ArrayList<Roomlist>) :
    RecyclerView.Adapter<Viewholder>() {
    var db: DataBaseHandler
    var valid = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_room_list, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, @SuppressLint("RecyclerView") position: Int) {
        holder.textView_name.text = list.get(position).name
        holder.textView_date.text = list.get(position).date
        if (list[position].islive.equals(
                "true",
                ignoreCase = true
            )
        ) holder.textView_live.visibility = View.VISIBLE else holder.textView_live.visibility =
            View.GONE
        holder.linearLayout.setOnClickListener(View.OnClickListener {
            PopupRoom(
                list[position].name,
                list[position].islive,
                position
            )
        })
    }

    private fun PopupRoom(name: String, islive: String, position: Int) {
        val dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.popup_room)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val editText = dialog.findViewById<EditText>(R.id.etroom)
        editText.isEnabled = false
        editText.setText(name)
        val checkBox = dialog.findViewById<CheckBox>(R.id.chk1)
        if (islive.equals("true", ignoreCase = true)) checkBox.isChecked =
            true else checkBox.isChecked = false
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val create = dialog.findViewById<Button>(R.id.btn_create)
        cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()
            }
        })
        checkBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (buttonView.isChecked) valid = true else valid = false
            }
        })
        create.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                db.updateRoom(name, valid.toString())
                list.get(position).islive = valid.toString()
                notifyDataSetChanged()
                dialog.dismiss()
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Viewholder(view: View) : RecyclerView.ViewHolder(view) {
        var textView_live: TextView
        var textView_name: TextView
        var textView_date: TextView
        var linearLayout: LinearLayout

        init {
            textView_live = view.findViewById(R.id.live_txt)
            textView_name = view.findViewById(R.id.txt_name)
            linearLayout = view.findViewById(R.id.ln)
            textView_date = view.findViewById(R.id.txt_date)
        }
    }

    init {
        db = DataBaseHandler(context)
    }
}