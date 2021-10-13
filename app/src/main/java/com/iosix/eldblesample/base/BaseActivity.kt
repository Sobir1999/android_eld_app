package com.iosix.eldblesample.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.iosix.eldblesample.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {
    /**
     * @return Toolbar
     */
    var toolbar: Toolbar? = null
        private set
    private var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext
        setContentView(layoutId)
        bundle = savedInstanceState
        setupToolbar()
        initView()
        statusBar()
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
        window.statusBarColor = Color.TRANSPARENT
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        ) // fixme bu screenshot uchun.

    }

    companion object {
        lateinit var appContext: Context
    }

}