package io.agora.scenariodemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import io.agora.scenariodemo.rtc.RtcEngineController
import io.agora.scenariodemo.utils.KeyCenter
import io.agora.scenariodemo.utils.TokenGenerator
import io.agora.audioscenarioapi.AudioScenarioType
import io.agora.audioscenarioapi.SceneType
import io.agora.scenarioapi.R
import io.agora.scenarioapi.databinding.FragmentMainBinding
import kotlin.random.Random

/*
 * 体验前配置页面
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            resetSceneView()
            setSceneView()

            // 频道名输入框，开始体验前需要输入一个频道名
            etChannelId.doAfterTextChanged {
                KeyCenter.channelId = it?.trim().toString()
            }

            // 选择体验场景
            btnChat.setOnClickListener {
                resetSceneView()
                KeyCenter.scene = SceneType.Chat
                setSceneView()
            }

            // 选择体验场景
            btnShow.setOnClickListener {
                resetSceneView()
                KeyCenter.scene = SceneType.Show
                setSceneView()
            }

            // 选择加载歌曲的类型， MCC 声网歌曲中心或者本地歌曲
            groupChatRoleType.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbtPatron -> {
                        KeyCenter.role = AudioScenarioType.Chat_Caller
                    }
                    R.id.rbtHostess -> {
                        KeyCenter.role = AudioScenarioType.Chat_Callee
                    }
                }
            }

            // 选择体验 KTVApi 的类型， 普通合唱或者大合唱
            groupShowRoleType.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbtShowHost -> {
                        KeyCenter.role = AudioScenarioType.Show_Host
                    }
                    R.id.rbtAudience -> {
                        KeyCenter.role = AudioScenarioType.Show_InteractiveAudience
                    }
                }
            }

            // 开始体验按钮
            btnStartChorus.setOnClickListener {
                if (KeyCenter.channelId.isEmpty()){
                    toast(getString(R.string.app_input_channel_name))
                    return@setOnClickListener
                }

                // 这里一共获取了四个 Token
                // 1、加入主频道使用的 Rtc Token
                // 2、如果要使用 MCC 模块获取歌单、下载歌曲，需要 RTM Token 进行鉴权，如果您有自己的歌单就不需要获取该 token
                // 3、合唱需要用到的合唱子频道 token，如果您只需要独唱就不需要获取该 token
                TokenGenerator.generateToken(
                    "",
                    KeyCenter.localUid.toString(),
                    TokenGenerator.TokenGeneratorType.token007,
                    TokenGenerator.AgoraTokenType.rtc,
                    success = { token ->
                        RtcEngineController.token = token
                        findNavController().navigate(R.id.action_mainFragment_to_livingFragment)
                    },
                    failure = {
                        toast("获取 token 异常")
                    }
                )
            }
        }

        KeyCenter.localUid = Random(System.currentTimeMillis()).nextInt(100000) + 1000000
    }

    private fun resetSceneView() {
        binding?.apply {
            btnChat.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.lighter_gray, null))
            btnShow.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.lighter_gray, null))
        }
    }

    private fun setSceneView() {
        binding?.apply {
            if (KeyCenter.scene == SceneType.Chat) {
                btnChat.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darker_gray, null))
                groupChatRoleType.isVisible = true
                groupShowRoleType.isVisible = false
            } else {
                btnShow.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darker_gray, null))
                groupChatRoleType.isVisible = false
                groupShowRoleType.isVisible = true
            }
        }
    }
}