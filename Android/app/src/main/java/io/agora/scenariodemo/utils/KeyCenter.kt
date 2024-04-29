package io.agora.scenariodemo.utils

import io.agora.audioscenarioapi.AudioScenarioType
import io.agora.audioscenarioapi.SceneType

object KeyCenter {
    /*
     * 加入的频道名
     */
    var channelId: String = ""

    /*
     * 自己的 uid
     */
    var localUid: Int = 2024

    /*
     * 选择的歌曲类型
     */
    var role: AudioScenarioType = AudioScenarioType.Chat_Caller

    /*
     * 当前场景
     */
    var scene: SceneType = SceneType.Chat
}