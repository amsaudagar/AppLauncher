package com.android.applauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.android.lanchersdk.AppModel
import com.android.lanchersdk.LauncherViewModel

/**
 * Represents the main activity
 */
class MainActivity : AppCompatActivity() {
    private lateinit var appListAdapter : AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvAppList)
        val launcherViewModel = LauncherViewModel()
        val appList = launcherViewModel.retrieveInstalledApps(this)

        appListAdapter = AppListAdapter(appList, object : AppListAdapter.IAppLaunchListener {
            override fun onAppSelected(appModel: AppModel) {
                launcherViewModel.getLauncherIntent(this@MainActivity, appModel.packageName)?.let {
                    startActivity(it)
                }
            }
        })

        recyclerView.adapter = appListAdapter

        setAppNameTextChangeListener()
    }

    private fun setAppNameTextChangeListener() {
        findViewById<EditText>(R.id.edtAppName).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s : CharSequence, start: Int, before: Int, count: Int) {
                appListAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}