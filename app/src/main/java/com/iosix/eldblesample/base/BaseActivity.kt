package com.iosix.eldblesample.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.iosix.eldblesample.MyApplication
import com.iosix.eldblesample.MyApplication.executorService
import com.iosix.eldblesample.R
import com.iosix.eldblesample.shared_prefs.UserData
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {
    /**
     * @return Toolbar
     */
    var toolbar: Toolbar? = null
        private set
    private var bundle: Bundle? = null
    private var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        userData = UserData(applicationContext)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        appContext = applicationContext
        setContentView(layoutId)
        bundle = savedInstanceState
        setupToolbar()
        initView()
        statusBar()

        val runnable = Runnable {
            if (userData!!.autoSwitch) {
                if (userData!!.mode){
                    if (Calendar.getInstance().time
                            .hours >= 22 || Calendar.getInstance().time
                            .hours <= 6
                    ) {
                        runOnUiThread { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }else if (userData!!.mode) {
                runOnUiThread { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
            } else {
                runOnUiThread { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
            }
        }

        executorService.scheduleAtFixedRate(runnable,0,1,TimeUnit.SECONDS)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.clear()
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
}

