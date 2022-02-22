package com.iosix.eldblesample.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.iosix.eldblesample.R
import com.iosix.eldblesample.roomDatabase.daos.UserDao
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs
import com.iosix.eldblesample.shared_prefs.UserData
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*

@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {
    /**
     * @return Toolbar
     */
    var toolbar: Toolbar? = null
        private set
    private var bundle: Bundle? = null
    private var userData: UserData? = null
    var lastStopSharedPrefs: LastStopSharedPrefs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        userData = UserData(applicationContext)
        lastStopSharedPrefs = LastStopSharedPrefs(applicationContext)
        if(userData!!.mode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        appContext = applicationContext
        setContentView(layoutId)
        bundle = savedInstanceState
        setupToolbar()
        initView()
        statusBar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * Change status bar according to Android SDK version
     */
    @SuppressLint("InlinedApi")
    open fun statusBar() {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                true
            )
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(
            this,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            false
        )
//        window.statusBarColor = Color.TRANSPARENT
//            window.navigationBarColor = getColors(
////                R.color.colorBackground,
//                appContext
//            )
    }

    /**
     * @return Saved state that is created in onCreate method of com.iosix.eldblesample.base.BaseActivity
     */
    fun savedState(): Bundle? {
        return bundle
    }

    /**
     * Its common use a toolbar within activity, if it exists in the
     * layout this will be configured
     */
    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
    }

    /**
     * Use this method to initialize view components. This method is called after [ ][BaseActivity.onCreate]
     */
    open fun initView() {}

    fun setToolbarTitle(title_activity: Int) {
        val title = findViewById<TextView>(R.id.toolbarTitle)
        title.setText(title_activity)
    }

    /**
     * @return Content layout
     */
    protected abstract val layoutId: Int

    /**
     * @param intent Start new activity
     */
    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Finish current activity
     */
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    /**
     * @param newBase Calligraphy attach
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    /**
     * @param activity Page
     * @param bits     Flag id
     * @param on       On || Off
     */
    open fun setWindowFlag(
        activity: Activity,
        bits: Int,
        on: Boolean
    ) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        ) // fixme bu screenshot uchun.

    }

    companion object {
        lateinit var appContext: Context
    }

    override fun onStop() {
        super.onStop()
        val timeDate = "" + Calendar.getInstance().time
        val today = timeDate.split(" ").toTypedArray()[1] + " " + timeDate.split(" ").toTypedArray()[2]
        val hour = Calendar.getInstance().time.hours
        val minute = Calendar.getInstance().time.minutes
        val second = Calendar.getInstance().time.seconds
        val time = hour * 3600 + minute * 60 + second
        lastStopSharedPrefs?.saveLastStopTime(time)
        lastStopSharedPrefs?.saveLastStopDate(today)
        lastStopSharedPrefs?.lastStopDate?.let { Log.d("hhhh", it) }
        }
}

