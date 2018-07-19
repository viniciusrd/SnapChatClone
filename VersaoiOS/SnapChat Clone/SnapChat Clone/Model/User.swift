//
//  User.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 18/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import Foundation

class User {
    
    var email: String
    var name: String
    var uid: String
    
    init(email: String, name: String, uid: String) {
        self.email = email;
        self.name = name;
        self.uid = uid;
    }
    
}
