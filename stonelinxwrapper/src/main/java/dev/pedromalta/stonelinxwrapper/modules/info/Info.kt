package dev.pedromalta.stonelinxwrapper.modules.info

import android.content.Context
import android.os.Build
import com.linx.dtefmobile.BuildConfig
import com.linx.dtefmobile.CDTEFMobile
import com.linx.dtefmobile.config.CConfig
import com.linx.dtefmobile.model.Info
import com.linx.dtefmobile.model.SimpleInfo
import com.linx.dtefmobile.sdk.MobileSDK
import com.linx.dtefmobile.smartpos.SmartUtil

class Info {

    fun getInfo(context: Context?): Info {
        return MobileSDK.getInfo(context)
    }
}