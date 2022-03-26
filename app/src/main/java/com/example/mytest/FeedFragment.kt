package com.example.mytest

import com.example.mytest.sqlite.DataBaseHandler
import com.example.mytest.Model.Roomlist
import com.example.mytest.Adapter.RoomAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mytest.R
import android.content.DialogInterface
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.database.Cursor
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class FeedFragment : Fragment() {
   lateinit var feedbinding: Feedbinding;
    var db: DataBaseHandler? = null
    var cursor: Cursor? = null
    var list: ArrayList<Roomlist>? = null
    var adapter: RoomAdapter? = null
    var valid = false
    var live = ""
    var roomname = "ee"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedbinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed, container, false
        )
        val view = feedbinding.getRoot()
        feedbinding.setFeedcontents(this)
        db = DataBaseHandler(activity)
        list = ArrayList()
        feedbinding.addFab.setOnClickListener { PopupAddRoom() }
        feedbinding.imageSort.setOnClickListener {
            val checkedItem = intArrayOf(-1)
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setTitle("Sort By")
            val listItems = arrayOf("All", "Live")
            alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
                checkedItem[0] = which
                live = listItems[which]
                for (i in list!!.indices) {
                    if (live.equals("Live", ignoreCase = true)) {
                        if (list!![i].islive.equals("true", ignoreCase = true)) {
                        } else {
                            list!!.removeAt(i)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                }
                dialog.dismiss()
            }
            val customAlertDialog = alertDialog.create()
            customAlertDialog.show()
        }
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        feedbinding.FeedRecycle.layoutManager = linearLayoutManager
        feedbinding.FeedRecycle.itemAnimator = DefaultItemAnimator()
        cursor = db!!.select_MDP()
        if (cursor!!.getCount() > 0) {
            cursor!!.moveToFirst()
            do {
                Log.v("vall>>", cursor!!.getString(1))
                roomname += cursor!!.getString(0)
                list!!.add(Roomlist(cursor!!.getString(0), cursor!!.getString(2), cursor!!.getString(1)))
            } while (cursor!!.moveToNext())
        }
        adapter = activity?.let { RoomAdapter(it, list!!) }
        feedbinding.FeedRecycle.adapter = adapter
        return view
    }

    private fun PopupAddRoom() {
        val dialog = Dialog(requireActivity())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.popup_room)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val editText = dialog.findViewById<EditText>(R.id.etroom)
        val checkBox = dialog.findViewById<CheckBox>(R.id.chk1)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val create = dialog.findViewById<Button>(R.id.btn_create)
        cancel.setOnClickListener { dialog.dismiss() }
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            valid = if (buttonView.isChecked) true else false
        }
        create.setOnClickListener {
            if (roomname.contains(editText.text.toString())) {
                Toast.makeText(activity, "Room name already exist!", Toast.LENGTH_SHORT).show()
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss z")
                val currentDateandTime = sdf.format(Date())
                db!!.insert_rooms(editText.text.toString(), valid.toString(), currentDateandTime)
                list!!.add(Roomlist(editText.text.toString(), currentDateandTime, valid.toString()))
                adapter!!.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
    }
}