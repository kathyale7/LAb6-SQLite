package com.example.mysqliteexample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private var items: ArrayList<StudentModel>, private val itemClickListener:onStudentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var itemsList: ArrayList<StudentModel>? = null

    lateinit var mcontext: Context
    interface onStudentClickListener {
        fun onItemClick(student : StudentModel)
    }
    class StudentHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        this.itemsList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
        val personListView = LayoutInflater.from(parent.context).inflate(R.layout.templatestudent, parent, false)
        val sch = StudentHolder(personListView)
        mcontext = parent.context
        return sch
    }



    override fun getItemCount(): Int {
        return itemsList?.size!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemsList = items
                } else {
                    val resultList = ArrayList<StudentModel>()
                    for (row in items) {
                        if (row.Name.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    itemsList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemsList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsList = results?.values as ArrayList<StudentModel>
                notifyDataSetChanged()
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvName)?.text = item?.Name
        holder.itemView.findViewById<TextView>(R.id.tvSurname)?.text = item?.Surname
        holder.itemView.findViewById<TextView>(R.id.tvAge)?.text = item?.Age

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item!!)
            Log.d("Selected:", itemsList?.get(position)?.Name.toString())

        }
}}