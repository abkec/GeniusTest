package com.example.test

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.core.view.GravityCompat

class TutorMenu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutor_menu)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        drawer = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.tutor_navView)
        bottomNavigationView = findViewById(R.id.tutor_nav_view)
        navController = Navigation.findNavController(this,R.id.tutor_nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.tutor_navigation_course,R.id.tutor_navigation_forum, R.id.tutor_navigation_quiz)
                .setDrawerLayout(drawer)
                .build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.itemIconTintList = null

        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.aboutUsFragment -> startActivity(Intent(this, StudentMenu::class.java))
        }
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val builder = AlertDialog.Builder(this,R.style.AlertDialogCustom)

            builder.setMessage("Are you sure you want to Exit ?")
            builder.setCancelable(true)

            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.cancel()
            })

            builder.setPositiveButton("Yes" , DialogInterface.OnClickListener { dialog, i ->
                finish()
            })

            val dialog = builder.create()
            dialog.setTitle("Exit")
            dialog.show()
        }
    }
}
