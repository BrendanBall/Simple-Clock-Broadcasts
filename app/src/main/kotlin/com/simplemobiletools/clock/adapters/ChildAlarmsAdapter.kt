package com.simplemobiletools.clock.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.simplemobiletools.clock.R
import com.simplemobiletools.clock.extensions.config
import com.simplemobiletools.clock.extensions.getFormattedTime
import com.simplemobiletools.clock.models.Alarm
import kotlinx.android.synthetic.main.item_child_alarm.view.*
import com.simplemobiletools.commons.extensions.toast
import kotlin.math.abs

class ChildAlarmsAdapter (var items: List<Alarm>, var shownItems: List<Int>, val parentAlarm: Alarm, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return shownItems.size
    }

    fun updateItems(newItems: List<Alarm>, newShownItems: List<Int>) {
        items = newItems
        shownItems = newShownItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder (LayoutInflater.from(context).inflate(R.layout.item_child_alarm, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var childAlarm : Alarm = items.get(shownItems.get(position))
        var time = context.getFormattedTime(childAlarm.timeInMinutes *60, false, true)
        holder.itemView.tv_child_alarm_info.setTextColor(context!!.config.textColor)
        holder.itemView.tv_child_alarm_info.setText(time)
        holder.itemView.seekbar_alarm_offset.max = 180
        val seekBarZero = holder.itemView.seekbar_alarm_offset.max/2
        if(parentAlarm.timeInMinutes - childAlarm.timeInMinutes >= 0)
            holder.itemView.seekbar_alarm_offset.progress = seekBarZero - abs(parentAlarm.timeInMinutes - childAlarm.timeInMinutes)
        else
            holder.itemView.seekbar_alarm_offset.progress = seekBarZero + abs(parentAlarm.timeInMinutes - childAlarm.timeInMinutes)
        holder.itemView.seekbar_alarm_offset.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener {

                    override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                        var mappedProgress = progress - seekBarZero
                        val newTime = mappedProgress + childAlarm.timeInMinutes
                        time = context.getFormattedTime(newTime * 60, false, false)
                        holder?.itemView.tv_child_alarm_info.setText(time)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                        var mappedProgress = p0?.progress!! - seekBarZero
                        val newTime = mappedProgress + childAlarm.timeInMinutes
                        childAlarm.timeInMinutes = newTime
                        context.toast(context.getFormattedTime(newTime * 60, false, false).toString())
                    }
                })

        holder.itemView.ib_delete_child_alarm.setOnClickListener {
            items.get(shownItems.get(position)).isEnabled = false
            updateItems(items, shownItems.minus(position))
            notifyDataSetChanged()
        }
    }

    fun addItem(alarm: Alarm) {
        updateItems(items.plus(alarm), shownItems.plus(shownItems.lastIndex+1))
        //updateItems(items.plus(alarm), items.plus(alarm).indices.toList())
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view)