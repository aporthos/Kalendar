package net.portes.kalendar.fragment


import android.Manifest.permission.READ_CALENDAR
import android.Manifest.permission.WRITE_CALENDAR
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_calendar.*
import net.portes.kalendar.R
import net.portes.kalendar.adapters.CalendarAdapter
import net.portes.kalendar.utils.inflate
import net.portes.kalendar.pojo.Calendar


/**
 * A simple [Fragment] subclass.
 */
class CalendarFragment : Fragment() {
    companion object {
        val TAG = "CalendarFragment"
        const val REQUEST_GRANTED_PERMISSION_CALENDAR = 1
        private var REQUEST_CODE_PERMISSION_CALENDAR = false

        private val PROJECTION_ID_INDEX = 0
        private val PROJECTION_ACCOUNT_NAME_INDEX = 1
        private val PROJECTION_DISPLAY_NAME_INDEX = 2
        private val PROJECTION_OWNER_ACCOUNT_INDEX = 3
    }

    private val mListCalendar: ArrayList<Calendar> by lazy { ArrayList<Calendar>() }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_calendar)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inicializeView()
    }

    private fun inicializeView() {
        checkPermissionCalendar()
        recyclerCalendars.layoutManager = LinearLayoutManager(context)
        recyclerCalendars.setHasFixedSize(true)
        if (REQUEST_CODE_PERMISSION_CALENDAR) {
            getListCalendars()
        }
    }

    @SuppressLint("MissingPermission")
    fun getListCalendars() {
        val mProjection = arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.OWNER_ACCOUNT)

        val mCursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                mProjection,
                null,
                null,
                null)

        while (mCursor.moveToNext()) {
            val mCalendar = Calendar(
                    mCursor.getLong(PROJECTION_ID_INDEX),
                    mCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX),
                    mCursor.getString(PROJECTION_DISPLAY_NAME_INDEX),
                    mCursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX))
            mListCalendar.add(mCalendar)
        }
        recyclerCalendars.adapter = CalendarAdapter(mListCalendar)
    }


    fun checkPermissionCalendar() {
        val mPermissionCalendarRead = ContextCompat.checkSelfPermission(context, READ_CALENDAR)
        val mPermissionCalendarWrite = ContextCompat.checkSelfPermission(context, WRITE_CALENDAR)
        if (mPermissionCalendarRead != PackageManager.PERMISSION_GRANTED || mPermissionCalendarWrite != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(READ_CALENDAR, WRITE_CALENDAR), REQUEST_GRANTED_PERMISSION_CALENDAR)
        } else {
            REQUEST_CODE_PERMISSION_CALENDAR = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_GRANTED_PERMISSION_CALENDAR -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getListCalendars()
                }
            }
        }
    }


}
