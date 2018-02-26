package net.portes.kalendar.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_calendar.view.*
import net.portes.kalendar.R
import net.portes.kalendar.utils.inflate
import net.portes.kalendar.pojo.Calendar

/**
 * Created by portes on 25/02/18.
 */
class CalendarAdapter(val mListCalendar: List<Calendar>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_calendar))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mListCalendar[position])
    }

    override fun getItemCount(): Int = mListCalendar.size

    class ViewHolder(calendarItemView: View) : RecyclerView.ViewHolder(calendarItemView) {
        fun bind(mCalendar: Calendar) = with(itemView) {
            lblCalendarId.text = "ID# ${mCalendar.id}"
            lblAccountName.text = " AccoutName: ${mCalendar.accountName}"
            lblDisplayName.text = " DisplayName:  ${mCalendar.displayName}"
            lblOwner.text = " Owner ${mCalendar.ownerAccount}"
        }
    }
}