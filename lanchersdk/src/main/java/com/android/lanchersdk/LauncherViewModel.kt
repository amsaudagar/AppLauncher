package com.android.lanchersdk

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList

class LauncherViewModel {

    /**
     * Retrieve the installed app details
     *
     * @param context - App context
     * @return - list of AppModel
     */
    fun retrieveInstalledApps(context : Context) : List<AppModel> {
        val packageManager = context.packageManager

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val appList = packageManager.queryIntentActivities(mainIntent, 0)
        Collections.sort(appList, ResolveInfo.DisplayNameComparator(packageManager))

        val installedApps = ArrayList<AppModel>()
        for (resolveInfo in appList) {
            val packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.packageName,0)
            val appModel = AppModel(packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo).toString(),
                resolveInfo.activityInfo.packageName,
                packageInfo.versionCode,
                packageInfo.versionName,
                getAppIconByPackageName(context, resolveInfo.activityInfo.packageName, packageManager))

            installedApps.add(appModel)
        }

        return installedApps.sortedBy { it.appName }
    }

    /**
     * Return the launcher activity intent for the given package
     * @param context - context
     * @param packageName - package name of the app
     *
     * @return launcher intent
     */
    fun getLauncherIntent(context : Context, packageName: String) : Intent? {
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }

    private fun getAppIconByPackageName(context : Context, packageName: String, packageManager : PackageManager): Drawable? {
        return try {
            packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            ContextCompat.getDrawable(context, R.drawable.ic_launcher)
        }
    }
}