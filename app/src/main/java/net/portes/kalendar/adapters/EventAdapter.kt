package net.portes.kalendar.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_calendar.view.*
import kotlinx.android.synthetic.main.item_event.view.*
import net.portes.kalendar.R
import net.portes.kalendar.pojo.Event
import net.portes.kalendar.utils.inflate
import java.util.*


/**
 * Created by portes on 25/02/18.
 */
class EventAdapter(val mListEvent: List<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_event))
    }

    override fun getItemCount(): Int = mListEvent.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mListEvent[position])
    }

    class ViewHolder(calendarItemView: View) : RecyclerView.ViewHolder(calendarItemView) {
        val mCalendar = Calendar.getInstance()
        companion object {
            val TAG = "ViewHolder"
        }
        fun bind(mEvent: Event) = with(itemView) {
            Log.i(TAG, "bind: ${mEvent.id}")
            lblTitle.text = "# ${mEvent.id} ${mEvent.title}"
            lblDescription.text = "${mEvent.description}"
            mCalendar.timeInMillis = mEvent.location.toLong() * 1000
            lblLocation.text = "${mCalendar.time}"
        }

    }
}