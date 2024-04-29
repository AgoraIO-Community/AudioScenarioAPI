package io.agora.scenariodemo.rtc

import android.util.Log
import io.agora.audioscenarioapi.AudioScenarioApi
import io.agora.scenariodemo.MyApplication
import io.agora.rtc2.*
import io.agora.scenarioapi.BuildConfig

object RtcEngineController {

    private var innerRtcEngine: RtcEngineEx? = null
    private var innerAudioScenarioApi: AudioScenarioApi? = null

    val rtcEngine: RtcEngineEx
        get() {
            if (innerRtcEngine == null) {
                val config = RtcEngineConfig()
                config.mContext = MyApplication.app()
                config.mAppId = BuildConfig.AGORA_APP_ID
                config.mEventHandler = object : IRtcEngineEventHandler() {
                    override fun onError(err: Int) {
                        super.onError(err)
                        Log.d(
                            "RtcEngineController",
                            "Rtc Error code:$err, msg:" + RtcEngine.getErrorDescription(err)
                        )
                    }
                }
                config.addExtension("agora_ai_echo_cancellation_extension")
                config.addExtension("agora_ai_noise_suppression_extension")
                innerRtcEngine = (RtcEngine.create(config) as RtcEngineEx).apply {
                    setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
                }
            }
            return innerRtcEngine!!
        }

    fun getAudioScenarioApi(): AudioScenarioApi {
        if (innerAudioScenarioApi == null) {
            innerAudioScenarioApi = AudioScenarioApi(rtcEngine)
        }
        return innerAudioScenarioApi!!
    }

    var token = ""

    fun destroy() {
        innerAudioScenarioApi = null
        innerRtcEngine = null
    }
}