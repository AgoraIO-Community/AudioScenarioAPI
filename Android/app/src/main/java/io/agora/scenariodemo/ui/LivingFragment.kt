package io.agora.scenariodemo.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.navigation.fragment.findNavController
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.audioscenarioapi.*
import io.agora.scenarioapi.databinding.FragmentLivingBinding
import io.agora.scenariodemo.rtc.RtcEngineController
import io.agora.scenariodemo.utils.KeyCenter


class LivingFragment : BaseFragment<FragmentLivingBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLivingBinding {
        return FragmentLivingBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initScenarioApi()
        joinChannel()
    }

    override fun onDestroy() {
        RtcEngineController.rtcEngine.leaveChannel()
        super.onDestroy()
    }

    private fun initView() {
        binding?.apply {
            tvRole.text = KeyCenter.role.toString()

            // 退出场景
            btnClose.setOnClickListener {
                RtcEngineController.rtcEngine.leaveChannel()
                findNavController().popBackStack()
            }

            // Audio Dump
            dumpAudio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    RtcEngineController.rtcEngine.setParameters("{\"rtc.debug.enable\": true}")
                    RtcEngineController.rtcEngine.setParameters("{\"che.audio.frame_dump\":{\"location\":\"all\",\"action\":\"start\",\"max_size_bytes\":\"120000000\",\"uuid\":\"123456789\",\"duration\":\"1200000\"}}")
                } else {
                    RtcEngineController.rtcEngine.setParameters("{\"rtc.debug.enable\": false}")
                }
            }
        }
    }

    private fun initScenarioApi() {
        RtcEngineController.getAudioScenarioApi().setAudioScenario(KeyCenter.scene, KeyCenter.role)
    }

    private fun joinChannel() {
        RtcEngineController.rtcEngine.addHandler(
            object : IRtcEngineEventHandler() {
                override fun onError(err: Int) {
                    super.onError(err)
                    runOnUiThread {
                        printLog("onError: $err")
                    }
                }

                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    super.onJoinChannelSuccess(channel, uid, elapsed)
                    runOnUiThread {
                        printLog("onJoinChannelSuccess channel:$channel, uid:$uid")
                    }
                }

                override fun onUserJoined(uid: Int, elapsed: Int) {
                    super.onUserJoined(uid, elapsed)
                    runOnUiThread {
                        printLog("onUserJoined uid:$uid")
                    }
                }
            }
        )
        RtcEngineController.rtcEngine.joinChannel(
            RtcEngineController.token,
            KeyCenter.channelId,
            KeyCenter.localUid,
            ChannelMediaOptions().apply {
                autoSubscribeAudio = true
                autoSubscribeVideo = KeyCenter.scene == SceneType.Show
                publishMicrophoneTrack = true
                publishCameraTrack = KeyCenter.scene == SceneType.Show
                clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            }
        )
    }

    /**
     * 打印日志到TextView，并自动换行。
     * @param log 要打印的日志内容
     */
    fun printLog(log: String) {
        binding?.apply {
            tvLog.append("$log\n")

            // 保持滚动到最底部，以显示最新的日志条目
            val scrollView = tvLog.parent as ScrollView
            scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private fun runOnUiThread(runnable: Runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            runnable.run()
        } else {
            mHandler.post(runnable)
        }
    }
}