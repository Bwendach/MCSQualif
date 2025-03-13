package com.example.bdapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.bdapplication.adapter.TabHomeVPAdapter
import com.example.bdapplication.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)

        val sharedPref = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val darkTheme = sharedPref.getBoolean("isDarkTheme", false)

        if(darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked->
            val editor = sharedPref.edit()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkTheme", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkTheme", false)
            }
            editor.apply()
        }

        val username = intent.getStringExtra("usernameKey")
        binding.tvHello.text = "Hello, $username"

        binding.vpTabHome.adapter = TabHomeVPAdapter(this)

        TabLayoutMediator(binding.tlTabHome, binding.vpTabHome) { tab, position ->
            tab.text = when(position) {
                0 -> "Items Tab"
                1 -> "Menu Tab"
                else -> "Items Tab"
            }

            tab.setIcon(
                when(position) {
                    0 -> R.drawable.ic_launcher_background
                    1 -> R.drawable.ic_launcher_foreground
                    else -> R.drawable.ic_launcher_background
                }
            )
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.internal_external_storage_profile -> {
                val intent = Intent(this, internalExternalStorageActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_map -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_profile -> Toast.makeText(this, "Profile menu clicked", Toast.LENGTH_SHORT).show()

            R.id.menu_log_out -> {
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sms -> {
                val intent = Intent(this, SmsActivity::class.java)
                startActivity(intent)
            }


        }
        return super.onOptionsItemSelected(item)
    }


}