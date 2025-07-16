package com.ultipoll

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val botnav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        botnav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navHome ->{
                    supportFragmentManager.beginTransaction().replace(R.id.FrameLayout,
                        SplashFragment()).commit()
                }
                R.id.navHistory->{
                    supportFragmentManager.beginTransaction().replace(R.id.FrameLayout,
                        HistoryFragment()).commit()
                }
            }
            true
        }
        botnav.visibility = View.GONE
        supportFragmentManager.beginTransaction().replace(R.id.FrameLayout,SplashFragment()).commit()
    }
}