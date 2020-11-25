package uz.mahmudxon.currency.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import uz.mahmudxon.currency.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if (findViewById<DrawerLayout?>(R.id.drawer)?.isDrawerOpen(GravityCompat.START) == true) {
            findViewById<DrawerLayout?>(R.id.drawer)?.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }
}