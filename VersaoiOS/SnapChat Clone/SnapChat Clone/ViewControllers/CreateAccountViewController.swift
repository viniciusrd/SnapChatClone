//
//  CreateAccountViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 12/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseDatabase

class CreateAccountViewController: UIViewController {

    
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var passwordConfirm: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        //esconder barra
        self.navigationController?.setToolbarHidden(false, animated: false);
    }
    
    // clicar fora do teclado e fechar o teclado
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        view.endEditing(true);
        
    }
    
    func showMessage(title: String, message: String){
        
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert);
        let cancelAction = UIAlertAction(title: "Cancelar", style: .cancel, handler: nil);
        
        alert.addAction(cancelAction);
        present(alert, animated: true, completion: nil);
        
        
    }
    
    
    @IBAction func createAccount(_ sender: Any) {
        
        //Recuperar os dados digitados
        if let getEmail = self.email.text
        {
            if let getName = name.text
            {
                if let getPassword = self.password.text
                {
                    if let getPasswordConfirm = self.passwordConfirm.text
                    {
                        //validar senha
                        if getPassword == getPasswordConfirm
                        {
                            if getName != ""{
                                
                                let auth = Auth.auth();
                                auth.createUser(withEmail: getEmail, password: getPassword) { (user, error) in
                                    
                                    if error == nil
                                    {
                                        if user == nil
                                        {
                                            self.showMessage(title: "Erro ao Autenticar", message: "Ocorreu um erro ao realizar a autenticação do seu usuário, tente novamente!")
                                        }
                                        else
                                        {
                                            let database = Database.database().reference();
                                            let users = database.child("usuarios");
                                            let getUser = user?.user;
                                            let dataUser = ["name":getName,"email":getEmail];
                                            users.child( getUser!.uid ).setValue(dataUser);
                                            
                                            //tudo ok redirecionar para a tela principal
                                            self.performSegue(withIdentifier: "createAccountSegue", sender: nil);
                                        }
                                        
                                    }
                                    else
                                    {
                                        let getError = error! as NSError;
                                        var messageError = ""
                                        if let codError = getError.userInfo["error_name"]{
                                            let textError = codError as! String;
                                            
                                            switch textError {
                                            case "EROR_INVALID_EMAIL":
                                                messageError = "E=mail inválido, digite novamente!";
                                                break;
                                            case "ERROR_WEAK_PASSWORD":
                                                messageError = "Senha deve ter no minimo 6 caracteres com letras e números, digite novamente!";
                                                break;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                messageError = "E-mail já utilizado por outro usuário, digite um novo e-mail!";
                                                break;
                                            default:
                                                messageError = "Dados informados estão incorretos, confira seus dados e tente novamente!";
                                            }
                                            
                                            self.showMessage(title: "Criação de Conta", message: messageError);
                                            
                                        }
                                        
                                    }
                                    
                                }
                            }
                            else
                            {
                                self.showMessage(title: "Dados Incorretos", message: "As senhas estão diferentes, digite novamente!")
                            }//fim valida senha
                        }
                        
                    }
                }
            }// fim nome
            else
            {
                self.showMessage(title: "Dados Incorretos", message: "Informe seu nome!")
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
