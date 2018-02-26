package net.portes.kalendar


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import net.portes.kalendar.fragment.CalendarFragment
import net.portes.kalendar.fragment.EventFragment


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(EventFragment())
                toolbar.title = "Eventos"
            }
            R.id.navigation_dashboard -> {
                loadFragment(CalendarFragment())
                toolbar.title = "Calendario"
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                toolbar.title = "Recordatorios"
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadFragment(EventFragment())
        toolbar.title = "Eventos"
    }

    private fun loadFragment(mFragment: Fragment) {
        val mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.replace(R.id.frame_container, mFragment)
        mFragmentTransaction.addToBackStack(null)
        mFragmentTransaction.commit()
    }


}
