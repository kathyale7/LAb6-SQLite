package com.example.mysqliteexample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class EnrollAdapter(private var items: ArrayList<Enroll>, private val itemClickListener:onEnrollClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var itemsList: ArrayList<Enroll>? = null

    lateinit var mcontext: Context
    interface onEnrollClickListener {
        fun onItemClick(course : Enroll)
    }
    class EnrollHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        this.itemsList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollHolder {
        val personListView = LayoutInflater.from(parent.context).inflate(R.layout.templateenrollements, parent, false)
        val sch = EnrollHolder(personListView)
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
                    val resultList = ArrayList<Enroll>()
                    for (row in items) {
                        if (row.STUDENT_ID.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemsList = results?.values as ArrayList<Enroll>
                notifyDataSetChanged()
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvStudent)?.text = item?.STUDENT_ID
        holder.itemView.findViewById<TextView>(R.id.tvCourse)?.text = item?.COURSE_ID


        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item!!)
            Log.d("Selected:", itemsList?.get(position)?.STUDENT_ID.toString())

        }
}}