package com.example.mysqliteexample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(private var items: ArrayList<CourseModel>, private val itemClickListener:onCourseClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var itemsList: ArrayList<CourseModel>? = null

    lateinit var mcontext: Context
    interface onCourseClickListener {
        fun onItemClick(course : CourseModel)
    }
    class CourseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        this.itemsList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        val personListView = LayoutInflater.from(parent.context).inflate(R.layout.templatecourse, parent, false)
        val sch = CourseHolder(personListView)
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
                    val resultList = ArrayList<CourseModel>()
                    for (row in items) {
                        if (row.Descripcion.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemsList = results?.values as ArrayList<CourseModel>
                notifyDataSetChanged()
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvName)?.text = item?.Descripcion
        holder.itemView.findViewById<TextView>(R.id.tvSurname)?.text = item?.Creditos


        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item!!)
            Log.d("Selected:", itemsList?.get(position)?.Descripcion.toString())

        }
}}