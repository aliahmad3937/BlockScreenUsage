package com.cc.blockscreenusage

import android.app.usage.UsageStats
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.blockscreenusage.databinding.UsageStatsItemBinding

class MyAdapter(var context: Context, var list: MutableList<UsageStats>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.usage_stats_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // Log.v("TAG6", "adapter onBind app ")
        with(list[position]) {
            var foregroundUsageTime: Long = this.totalTimeInForeground
            val seconds = (foregroundUsageTime / 1000) % 60
            val minute = (foregroundUsageTime / (1000 * 60)) % 60
            val hour = (foregroundUsageTime / (1000 * 60 * 60))
            val time_val = "$hour h $minute m $seconds s"


            holder.binding.usageTime.text = time_val
            holder.binding.packageName.text =  this.packageName


        }


    }

    override fun getItemCount(): Int {
        return if (list == null || list!!.isEmpty()) 0 else list!!.size
    }

    fun updateAdapter(list: MutableList<UsageStats>) {
    //    Log.v("TAG6", "adapter app :${list.size}")
        this.list = list
  //      Log.v("TAG6", "adapter app :${this.list.size}")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: UsageStatsItemBinding

        init {
            binding = UsageStatsItemBinding.bind(itemView)
        }
    }


}