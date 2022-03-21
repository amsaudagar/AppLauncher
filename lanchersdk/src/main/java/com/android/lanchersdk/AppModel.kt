package com.android.lanchersdk

import android.graphics.drawable.Drawable

data class AppModel(
    var appName : String,
    var packageName : String,
    var versionCode : Int,
    var versionName : String,
    var launcherIcon : Drawable?
)