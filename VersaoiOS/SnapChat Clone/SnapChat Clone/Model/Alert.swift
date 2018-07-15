//
//  Alert.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 14/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit

class Alert {
    var title: String;
    var message: String;
    
    init(title:String, message: String)
    {
        self.title = title;
        self.message = message;
    }
    
    func getAlert() -> UIAlertController
    {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert);
        let cancelAction = UIAlertAction(title: "Cancelar", style: .cancel, handler: nil);
        
        alert.addAction(cancelAction);
//        present(alert, animated: true, completion: nil);
        
        return alert;
    }
    
}
