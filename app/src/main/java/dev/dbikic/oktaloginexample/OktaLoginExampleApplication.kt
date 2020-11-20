package dev.dbikic.oktaloginexample

import android.app.Application
import dev.dbikic.oktaloginexample.managers.OktaManger

class OktaLoginExampleApplication : Application() {

    lateinit var oktaManger: OktaManger

    override fun onCreate() {
        super.onCreate()
        oktaManger = OktaManger(this)
    }
}