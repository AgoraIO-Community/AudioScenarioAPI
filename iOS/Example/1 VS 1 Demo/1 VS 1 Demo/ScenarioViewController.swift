//
//  ScenarioViewController.swift
//  1 VS 1 Demo
//
//  Created by CP on 2024/2/23.
//

import UIKit
import AgoraRtcKit
class ScenarioViewController: UIViewController {
    var rtcKit: AgoraRtcEngineKit!
    var api: AudioScenarioApi?
    var channelName: String?
    var sceneType: SceneType = .Chat
    var role: AudioScenarioType = .Chat_Caller
    var tv: UITableView = UITableView()
    var dumpLabel: UILabel = UILabel()
    var dumpSeg: UISegmentedControl!
    var scSeg: UISegmentedControl!
    var scLabel: UILabel = UILabel()
    let randomNum = 100 + Int(arc4random_uniform(UInt32(100000 - 100)))
    var logs: [String] = [] {
        didSet {
            tv.reloadData()
        }
    }
    var titleLabel: UILabel = UILabel()
    var titleName: String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        layoutUI()
        loadRtc()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        rtcKit.leaveChannel()
        rtcKit.delegate = nil
    }
    
    private func layoutUI() {
        
        titleLabel = UILabel(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 44))
        titleLabel.textColor = .black
        titleLabel.text = titleName
        titleLabel.textAlignment = .center
        self.view.addSubview(titleLabel)
        
        tv = UITableView(frame: CGRect(x: 0, y: 44, width: UIScreen.main.bounds.width, height: 300))
        tv.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        tv.backgroundColor = .gray
        tv.delegate = self
        tv.dataSource = self
        self.view.addSubview(tv)
        
        dumpLabel.frame = CGRect(x: 0, y: 354, width: 100, height: 30)
        dumpLabel.text = "dump enable"
        self.view.addSubview(dumpLabel)
        
        dumpSeg = UISegmentedControl(frame: CGRect(x: 120, y: 354, width: 150, height: 30))
        dumpSeg.insertSegment(withTitle: "enable", at: 0, animated: false)
        dumpSeg.insertSegment(withTitle: "disable", at: 1, animated: false)
        dumpSeg.selectedSegmentIndex = 1
        dumpSeg.addTarget(self, action: #selector(dumpChange), for: .valueChanged)
        self.view.addSubview(dumpSeg)
    }

    private func loadRtc() {
        fetchToken(with: "\(randomNum)") { [weak self] rtcToken in
            guard let strongSelf = self else {
                return
            }
            strongSelf.rtcKit = AgoraRtcEngineKit.sharedEngine(withAppId: KeyCenter.AppId, delegate: strongSelf)
            
            guard let rtc = strongSelf.rtcKit else {
                return
            }
            strongSelf.api = AudioScenarioApi(rtcEngine: rtc)
            strongSelf.api?.setAudioScenario(sceneType: strongSelf.sceneType, audioScenarioType: strongSelf.role)
            
            strongSelf.rtcKit.setEnableSpeakerphone(true)
            strongSelf.rtcKit.setClientRole(.broadcaster)
            strongSelf.rtcKit.joinChannel(byToken: rtcToken, channelId: strongSelf.channelName ?? "", info: nil, uid: UInt(strongSelf.randomNum), joinSuccess: nil)
        }
    }
    
    @objc private func dumpChange(seg: UISegmentedControl) {
        print(seg.selectedSegmentIndex)
        if seg.selectedSegmentIndex == 0 {
            rtcKit.setParameters("{\"rtc.debug.enable\":true}")
            rtcKit.setParameters("{\"che.audio.frame_dump\":{\"location\":\"all\",\"action\":\"start\",\"max_size_bytes\":\"120000000\",\"uuid\":\"123456789\",\"duration\":\"1200000\"}}");
            logs.append("{\"rtc.debug.enable\":true}")
            logs.append("{\"che.audio.frame_dump\":{\"location\":\"all\",\"action\":\"start\",\"max_size_bytes\":\"120000000\",\"uuid\":\"123456789\",\"duration\":\"1200000\"}}")
        } else {
            rtcKit.setParameters("{\"rtc.debug.enable\":false}")
            logs.append("{\"rtc.debug.enable\":false}")
        }
    }
    
    private func fetchToken(with userId: String, completion:@escaping ((String)->Void)) {
                
        NetworkManager.shared.generateTokens(channelName: channelName ?? "",
                                             uid: userId,
                                             tokenGeneratorType: .token006,
                                             tokenTypes: [.rtc]) { tokenMap in
            if let rtcToken = tokenMap[NetworkManager.AgoraTokenType.rtc.rawValue] {
                completion(rtcToken)
            }
        }
    }
}

extension ScenarioViewController: AgoraRtcEngineDelegate {
    func rtcEngine(_ engine: AgoraRtcEngineKit, didJoinChannel channel: String, withUid uid: UInt, elapsed: Int) {
        print("join channel success")
        logs.append("join channel success")
    }
    
    func rtcEngine(_ engine: AgoraRtcEngineKit, didOccurError errorCode: AgoraErrorCode) {
        print("join channel fail")
        logs.append("join channel fail")
    }
}

extension ScenarioViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return logs.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 44
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        cell.textLabel?.text = logs[indexPath.row]
        cell.backgroundColor = .gray
        cell.textLabel?.numberOfLines = 0
        return cell
    }
}
