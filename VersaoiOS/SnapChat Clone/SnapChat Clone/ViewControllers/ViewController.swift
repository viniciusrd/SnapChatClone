//
//  ViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 12/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseAuth

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //verificar se usuario esta autenticado
        let auth = Auth.auth();
        auth.addStateDidChangeListener { (auth, user) in
            
            if let userLogged = user
            {
                self.performSegue(withIdentifier: "loginAutomaticSegue", sender: nil)
            }
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //Toda vez que a tela for aparecer
    override func viewWillAppear(_ animated: Bool) {
        //esconder barra
        self.navigationController?.setToolbarHidden(true, animated: false);
    }


}

