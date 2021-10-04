package com.example.flo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var backPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        binding.mainPlayerLayout.setOnClickListener{
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.popBackStack("homeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                            .addToBackStack("homeFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.popBackStack("lookFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                            .addToBackStack("lookFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.popBackStack("searchFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                            .addToBackStack("searchFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.popBackStack("lockerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                            .addToBackStack("lockerFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0){
            if(System.currentTimeMillis() > backPressedTime + 2000){
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this,"'뒤로'버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            } else {
                finish()
            }
        }
        Log.d("fragmentStack", supportFragmentManager.backStackEntryCount.toString())
        super.onBackPressed()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

}

