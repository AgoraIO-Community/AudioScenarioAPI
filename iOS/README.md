# Audio Scenario 场景化 API 示例 demo

> 本文档主要介绍如何快速跑通 Audio Scenario 场景化 API 示例工程，本 demo 支持 1v1 语聊 和 秀场直播连麦两种场景的音频最佳实践
---

## 1. 环境准备

- <mark>最低兼容 iOS 13.0.0 </mark> 
- Xcode 13.0.0 及以上版本
- iPhone 6 及以上的手机设备(系统需要 iOS 13.0.0 及以上)

---

## 2. 运行示例

- 2.1 进入声网控制台获取 APP ID 和 APP 证书 [控制台入口](https://console.shengwang.cn/overview)

  - 点击创建项目

    ![图片](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/ent-full/sdhy_1.jpg)

  - 选择项目基础配置, 鉴权机制需要选择**安全模式**

    ![图片](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/ent-full/sdhy_2.jpg)

  - 拿到项目 APP ID 与 APP 证书

    ![图片](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/ent-full/sdhy_3.jpg)

- 2.2 在项目的 Example/1 VS 1 Demo 目录下会有一个 KeyCenter.swift 文件，需要在 KeyCenter.swift 里填写需要的声网 App ID 和 App 证书

  ![xxx](https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/github_readme/ktv/img_ktv_keys_ios.png)

  ```texag-0-1gpap96h0ag-1-1gpap96h0ag-0-1gpap96h0ag-1-1gpap96h0ag-0-1gpap96h0ag-1-1gpap96h0ag-0-1gpap96h0ag-1-1gpap96h0ag-0-1gpap96h0ag-1-1gpap96h0
  AppId：声网 appid
  Certificate：声网 Certificate
  ```
- 项目的第三方库使用 pod 集成, 需要在 Example/1 VS 1 Demo 目录下执行 pod install, 然后再开始体验项目
- 在 Example/1 VS 1 Demo 目录下，找到 1 VS 1 Demo.xcworkspace 文件
- 用 Xcode 运行 1 VS 1 Demo.xcworkspace 文件即可开始您的体验

---

## 3. 如何集成 Audio Scenario 场景化 API 实现 1v1 场景下的音频最佳实践
- 3.1 集成声网 RTC SDK，推荐版本为 4.1.1.29
  ```swift
  // 项目 podFile 文件内
  pod 'AgoraRtcEngine_Special_iOS', '4.1.1.29'
  ```
  将[**audioscenarioapi**](Classes)集成进入您的工程

- 3.2 在初始化 RtcEngine 实例后创建 Audio Scenario API 实例
  ```kotlin
  // 3.2 初始化 RtcEngine 实例
  let mRtcEngine = AgoraRtcEngineKit.sharedEngine(withAppId: KeyCenter.AppId, delegate: strongSelf)
  let audioScenarioApi = AudioScenarioApi(rtcEngine: rtc)
  ```

- 3.3 加入频道前，根据业务场景和身份，设置对应的音频最佳实践，目前支持以下4种配置组合
  ⚠️在加频道前配置一次即可，音频设置不随加入/退出频道发生改变
  ```kotlin
  // 1v1 场景，身份为主叫（发起呼叫方）
  audioScenarioApi.setAudioScenario(sceneType: .Chat, audioScenarioType: .Chat_Caller)
  // 1v1 场景，身份为被叫（接受呼叫方）
  audioScenarioApi.setAudioScenario(sceneType: .Chat, audioScenarioType: .Chat_Callee)
  // 秀场直播场景，身份为主播
  audioScenarioApi.setAudioScenario(sceneType: .Show, audioScenarioType: .Show_Host)
  // 秀场直播场景，身份为连麦观众
  audioScenarioApi.setAudioScenario(sceneType: .Show, audioScenarioType: .Show_InteractiveAudience)
  ```

## 4. FAQ
- 集成遇到困难，该如何联系声网获取协助
  - 方案1：可以从智能客服获取帮助或联系技术支持人员 [声网支持](https://ticket.shengwang.cn/form?type_id=&sdk_product=&sdk_platform=&sdk_version=&current=0&project_id=&call_id=&channel_name=)
  - 方案2：加入微信群提问
  
    ![](https://download.agora.io/demo/release/SDHY_QA.jpg)
---
