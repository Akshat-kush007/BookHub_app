package com.example.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.R
import com.example.bookhub.fragment.Settings_Fragment
import com.example.bookhub.fragment.About_App_Fragment
import com.example.bookhub.fragment.DashboardFragment
import com.example.bookhub.fragment.Favourite_Fragment
import com.example.bookhub.fragment.Profile_Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        navigationView = findViewById(R.id.navigatorView)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.framelayout)


        setUpToolbar()

        var actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        setUpDashboard()

        var previousMenuItem:MenuItem?=null
        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when (it.itemId) {
                R.id.dashboard -> {
//                    Toast.makeText(this@MainActivity, "Dashboard is clicked", Toast.LENGTH_SHORT)
//                        .show()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        DashboardFragment()
                    )
//                        .addToBackStack("Dashboard")
                        .commit()
                    supportActionBar?.title="DashBoard"
                    drawerLayout.closeDrawers()

                }
                R.id.favourite -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        Favourite_Fragment()
                    )
//                        .addToBackStack("favourite")
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        Profile_Fragment()
                    )
//                        .addToBackStack("profile")
                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,
                        Settings_Fragment()
                    )
//                        .addToBackStack("settings")
                        .commit()
                    supportActionBar?.title="AboutApp"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        About_App_Fragment()
                    )
//                        .addToBackStack("aboutApp")
                        .commit()
                    supportActionBar?.title="Settings"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpDashboard(){
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, DashboardFragment())
            .commit()
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.dashboard)
        supportActionBar?.title="DashBoard"
    }

    override fun onBackPressed() {
        val frag =supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is DashboardFragment ->setUpDashboard()

            else->super.onBackPressed()
        }
    }
}