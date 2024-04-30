//
//  ViewController.swift
//  1 VS 1 Demo
//
//  Created by CP on 2024/2/23.
//

import UIKit

class ViewController: UIViewController {

    private var sceneType: SceneType = .Chat
    private var role: AudioScenarioType = .Chat_Caller
    @IBOutlet weak var tf: UITextField!
    @IBOutlet weak var seg: UISegmentedControl!
    private var roleName: String = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        roleName = "榜一大哥"
    }
    @IBAction func VClick(_ sender: UIButton) {
        sceneType = .Chat
        seg.setTitle("榜一大哥", forSegmentAt: 0)
        seg.setTitle("连麦小姐姐", forSegmentAt: 1)
    }
    
    
    @IBAction func SClick(_ sender: Any) {
        sceneType = .Show
        seg.setTitle("秀场主播", forSegmentAt: 0)
        seg.setTitle("连麦观众", forSegmentAt: 1)
    }
    
    @IBAction func segChanged(_ sender: UISegmentedControl) {
        if sceneType == .Chat {
            roleName = sender.selectedSegmentIndex == 0 ? "榜一大哥" : "连麦小姐姐"
            role = sender.selectedSegmentIndex == 0 ? .Chat_Caller : .Chat_Callee
        } else {
            roleName = sender.selectedSegmentIndex == 0 ? "秀场主播" : "连麦观众"
            role = sender.selectedSegmentIndex == 0 ? .Show_Host : .Show_InteractiveAudience
        }
    }
    
    
    @IBAction func joinChannel(_ sender: Any) {
        let VC = ScenarioViewController()
           VC.channelName = tf.text
           VC.sceneType = sceneType
           VC.role = role
           VC.titleName = roleName
           
         //  VC.modalPresentationStyle = .fullScreen
           self.present(VC, animated: true, completion: nil)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        self.tf.endEditing(true)
    }
}

