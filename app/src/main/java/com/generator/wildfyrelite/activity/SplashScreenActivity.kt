package com.generator.wildfyrelite.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.generator.wildfyrelite.R
import com.generator.wildfyrelite.defaultSettings
import com.generator.wildfyrelite.model.DefaultSettings
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        createDefaultSettings()
        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 2000)

    }

    fun createDefaultSettings() {
        var url = arrayOf(
            "annvassadress.com",
            "axlpowerhouse.com",
            "brandxph.com",
            "brownrepublic.net",
            "buddybadette.com",
            "cleanspoonchronicle.com",
            "connectmanila.com",
            "crazylittlethingsilove.com",
            "dadslife.net",
            "dbedalyn.com",
            "digi-ph.com",
            "firebranddigital.net",
            "flingerosphilippines.com",
            "gadaboutprincess.com",
            "gforanything.com",
            "happeningph.com",
            "hearthandhomebuddies.com",
            "heyraul.net",
            "homeiskool.com",
            "iamleirs.com",
            "iamsarahjanemanalo.com",
            "jaysmin.com",
            "jaysonbiadog.net",
            "kawaiibeautyandlifestyle.com",
            "kellybiasong.net",
            "larawanatkape.net",
            "manilasociety.com",
            "manilenyo.net",
            "mimiworld.net",
            "mommshie.com",
            "mommybelleph.com",
            "mommyerikajane.com",
            "mommyjoyceshares.com",
            "momof4sc.com",
            "monzmelecio.com",
            "nexteventph.com",
            "palawanderer.com",
            "pinoyopolis.com",
            "shescapade.com",
            "shisamom.net",
            "sipandblog.com",
            "star-powerhouse.com",
            "stefthemomma.com",
            "techienoy.com",
            "thebakerwanderer.com",
            "theeventstribune.com",
            "themanilapost.net",
            "wanderwithsisters.com"
        )
        var time = arrayOf(
            DefaultSettings.Time("1", "0", "3", "0", "20"),
            DefaultSettings.Time("3", "1", "6", "0", "20"),
            DefaultSettings.Time("6", "1", "10", "0", "30"),
            DefaultSettings.Time("10", "1", "14", "0", "20"),
            DefaultSettings.Time("14", "1", "23", "59", "30")
        )

        var default = DefaultSettings.Setting(
            "90",
            "30",
            "30",
            url,
            time,
            "5"
        )

        defaultSettings = default
    }
}