package net.portes.kalendar.fragment


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings.System.DATE_FORMAT
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import kotlinx.android.synthetic.main.dialog_fullscreen.*
import kotlinx.android.synthetic.main.fragment_event.*

import net.portes.kalendar.R
import net.portes.kalendar.adapters.EventAdapter
import net.portes.kalendar.pojo.Event
import net.portes.kalendar.utils.inflate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {
    companion object {
        val TAG = "EventFragment"
        val mCalendar = Calendar.getInstance()
        val DATE_FOR_USER = 1
        val DATE_FOR_SYSTEM = 2
        private val DATE_FORMAT_FOR_USER = SimpleDateFormat("E, dd 'de' MMM 'de' yyyy", Locale.getDefault())
        private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())


    }

    private val mListEvent: ArrayList<Event> by lazy { ArrayList<Event>() }


    private val fullscreenDialog: Dialog by lazy {
        Dialog(context, R.style.DialogFullscreen)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_event)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inicializeView()
    }

    private fun inicializeView() {
        recyclerEvents.layoutManager = LinearLayoutManager(context)
        recyclerEvents.setHasFixedSize(true)
        loadEvents()
        fabNewEvent.setOnClickListener({ openFullScreenDialog() })
    }

    @SuppressLint("MissingPermission")
    private fun loadEvents() {
        val mProjection = arrayOf(
                "_id",
                CalendarContract.Events.ACCOUNT_NAME,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        )
        val mUri = CalendarContract.Events.CONTENT_URI
        val mSelection = CalendarContract.Events.ACCOUNT_NAME + " = ? "
        val mSelectionArgs = arrayOf("a.portes.dw@gmail.com")
        val mCursor = context.contentResolver.query(mUri, mProjection, mSelection, mSelectionArgs, "${CalendarContract.Events._ID} DESC")
        while (mCursor.moveToNext()) {
            val mEvent = Event(
                    mCursor.getLong(0),
                    mCursor.getString(1),
                    mCursor.getString(2),
                    mCursor.getString(3),
                    mCursor.getString(4))
            mListEvent.add(mEvent)
        }
        recyclerEvents.adapter = EventAdapter(mListEvent)
    }

    private fun openFullScreenDialog() {
        fullscreenDialog.setContentView(R.layout.dialog_fullscreen)
        fullscreenDialog.img_dialog_fullscreen_close.setOnClickListener({
            fullscreenDialog.dismiss()
        })

        fullscreenDialog.lblDay.setOnClickListener({ openCalendar() })
        fullscreenDialog.lblHour.setOnClickListener({ openTimePicker() })
        fullscreenDialog.btnSaveEvent.setOnClickListener({
            addEvent()
        })
        fullscreenDialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun addEvent() {
        val mCustomer = fullscreenDialog.lblCustomer.text.toString()
        val mLocation = fullscreenDialog.lblLocation.text.toString()
        val mDay = fullscreenDialog.lblDay.text.toString()
        val mHour = fullscreenDialog.lblHour.text.toString()
        val mContentResolver = context.contentResolver
        val mContentValues = ContentValues()
        mContentValues.put(CalendarContract.Events.DTSTART, mCalendar.time.time)
        mContentValues.put(CalendarContract.Events.DTEND, mCalendar.time.time)
        mContentValues.put(CalendarContract.Events.TITLE, "Visita a : $mCustomer")
        mContentValues.put(CalendarContract.Events.DESCRIPTION, "Direccion: $mLocation")
        mContentValues.put(CalendarContract.Events.CALENDAR_ID, 3)
        mContentValues.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Mexico_City")
        mContentValues.put(CalendarContract.Events.EVENT_LOCATION, mLocation)

        val uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, mContentValues)

        val values = ContentValues()
        values.put(CalendarContract.Reminders.MINUTES, 2)
        values.put(CalendarContract.Reminders.EVENT_ID, uri.lastPathSegment)
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_EMAIL)
        val uriReminder = mContentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
        fullscreenDialog.dismiss()
    }


    private fun openCalendar() {
        val mDatePickerListener = DatePickerDialog.OnDateSetListener({ datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            mCalendar.set(Calendar.YEAR, year)
            mCalendar.set(Calendar.MONTH, monthOfYear)
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val mStringForUser = convertDate(mCalendar.time, DATE_FOR_USER)
            fullscreenDialog.lblDay.setText(mStringForUser)
        })
        val mPickerDialog = DatePickerDialog(
                context,
                mDatePickerListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        )
        mPickerDialog.show()
    }

    private fun openTimePicker() {
        val mTimePickerListener = TimePickerDialog.OnTimeSetListener({ timePicker, hourOfDay, minute ->
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            mCalendar.set(Calendar.MINUTE, minute)
            val mDateString = getTime(mCalendar.time)
            fullscreenDialog.lblHour.setText(mDateString)
        })
        TimePickerDialog(context,
                mTimePickerListener,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                false).show()
    }

    private fun getTime(mDateTime: Date): String {
        return TIME_FORMAT.format(mDateTime)
    }

    private fun convertDate(mDateTime: Date, mType: Int): String {
        var mDateResult = ""
        when (mType) {
            DATE_FOR_USER -> mDateResult = DATE_FORMAT_FOR_USER.format(mDateTime).replace(".", "").toUpperCase()
            DATE_FOR_SYSTEM -> mDateResult = DATE_FORMAT.format(mDateTime)
        }
        return mDateResult
    }

}
