package dev.pedromalta.stonelinxwrapper.modules.info

import android.content.Context
import android.os.Build
import com.linx.dtefmobile.BuildConfig
import com.linx.dtefmobile.CDTEFMobile
import com.linx.dtefmobile.config.CConfig
import com.linx.dtefmobile.model.Info
import com.linx.dtefmobile.model.SimpleInfo
import com.linx.dtefmobile.smartpos.SmartUtil

class Info {

    fun getInfo(context: Context?): Info {
        val info = SimpleInfo()
        if (context != null) {
            val cConfig = CConfig(context)
            info.identifier = cConfig.getString("CNPJESTAB", "")
            info.companyCode = cConfig.getString("NUMESTAB", "")
            info.storeCode = cConfig.getString("NUMLOJA", "")
            info.pointOfSale = cConfig.getString("NUMPDV", "")
        }

        info.sdkVersion = CDTEFMobile.getVersion()
        info.sdkExternalVersion = String.format("%s-%s-%s", "2.6.6", "2308141153", if (BuildConfig.DEBUG) "Debug" else "release")
        info.hardwareManufacturer = Build.MANUFACTURER
        info.hardwareModel = Build.MODEL
        info.isSmartPOS = SmartUtil.isSmartPOS()
        info.isPrintSupported = SmartUtil.isPrintSupported()
        info.buildType = if (BuildConfig.DEBUG) "debug" else "release"
        info.profile = "linx"
        return info
    }
}