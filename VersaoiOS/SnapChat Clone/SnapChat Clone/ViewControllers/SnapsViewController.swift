//
//  SnapsViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 14/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseAuth

class SnapsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    @IBAction func loggof(_ sender: Any) {
        
        let auth = Auth.auth();
        do
        {
            try auth.signOut();
            dismiss(animated: true, completion: nil);
        }
        catch let exception
        {
            print(exception.localizedDescription)
        }
        
    }
}
