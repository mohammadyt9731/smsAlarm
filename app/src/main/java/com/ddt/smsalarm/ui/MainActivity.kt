package com.ddt.smsalarm.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ddt.smsalarm.R
import com.ddt.smsalarm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 111
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        getSmsPermission()
        checkScreenOverlaysPermission()

        lifecycleScope.launchWhenCreated {
            viewModel.setting.collect {
                Log.i("Mohammad", "onCreate: $it")
            }
        }
    }

    private fun getSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                REQUEST_CODE
            )
        }
    }

    private fun checkScreenOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            if (!Settings.canDrawOverlays(this)) {
                showGetPermissionDialog()
            }
    }

    private fun showGetPermissionDialog() {

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.get_permission))
            .setMessage(getString(R.string.getOverlaysPermission))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.givePermission)) { _, _ ->
                val intent =
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${packageName}")
                    )
                startActivityForResult(intent, REQUEST_CODE)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "مجوز داده شد", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "مجوز داده نشد", Toast.LENGTH_LONG).show()
            }
        }
    }
}