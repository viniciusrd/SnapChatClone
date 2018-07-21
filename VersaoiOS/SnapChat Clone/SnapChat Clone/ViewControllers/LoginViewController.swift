//
//  LoginViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 12/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseAuth

class LoginViewController: UIViewController {

    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        print("passou no viewDidLoad Login")
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //Toda vez que a tela for aparecer
    override func viewWillAppear(_ animated: Bool) {
        //esconder barra
        self.navigationController?.setToolbarHidden(false, animated: false);
    }
    
    func showMessage(title: String, message: String){
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert);
        let cancelAction = UIAlertAction(title: "Cancelar", style: .cancel, handler: nil);
        
        alert.addAction(cancelAction);
        present(alert, animated: true, completion: nil);
        
        
    }
    
    
    @IBAction func logar(_ sender: Any) {
        
        //Recuperar os dados digitados
        if let getEmail = self.email.text
        {
            if let getPassword = self.password.text
            {
                //Autenticar Usuario
                let auth = Auth.auth();
                auth.signIn(withEmail: getEmail, password: getPassword) { (user, error) in
                    
                    if error == nil
                    {
                        if user == nil
                        {
                            self.showMessage(title: "Erro ao Autenticar", message: "Ocorreu um erro ao realizar a autenticação do seu usuário, tente novamente!")
                        }
                        else
                        {
                            //tudo ok redirecionar para a tela principal
                            self.performSegue(withIdentifier: "loginSegue", sender: nil);
                        }
                    }
                    else
                    {
                        self.showMessage(title: "Login", message: "Dados incorretos, verifique seus dados e tente novamente!")
                    }
                    
                    
                }
            
            }
        }
        
    }
    
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
