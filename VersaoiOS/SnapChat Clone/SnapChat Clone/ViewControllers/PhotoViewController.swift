//
//  PhotoViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 14/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseStorage

class PhotoViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var image: UIImageView!
    @IBOutlet weak var textDesc: UITextField!
    @IBOutlet weak var buttonNext: UIButton!
    
    var idImage = NSUUID().uuidString;
    var imagePicker = UIImagePickerController();

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        imagePicker.delegate = self;
        enableButton(enable: false);
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func selectPhoto(_ sender: Any) {
        
        let alertController = UIAlertController(
            title: "Fotos para Snap",
            message: "Como deseja selecionar sua foto?",
            preferredStyle: .alert
        );
        
        // terceito parametro eh uma closure.
        let configCamera = UIAlertAction(
            title: "Abrir Galeria",
            style: .default) { (alertConfig) in
                self.openCameraOrGallery(option: 0);
        };
        
        let configGallery = UIAlertAction(
            title: "Abrir Camera",
            style: .default) { (alertConfig) in
                self.openCameraOrGallery(option: 1);
        }
        
        alertController.addAction(configCamera);
        alertController.addAction(configGallery)
        present(alertController, animated: true, completion: nil);
        
        
        
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        let getImage = info[UIImagePickerControllerOriginalImage] as! UIImage;
        self.image.image = getImage;
        imagePicker.dismiss(animated: true, completion: nil);
        
        enableButton(enable: true);
        
    }
    
    func openCameraOrGallery(option: Int) {
        
        
        imagePicker.sourceType = option == 0 ? .savedPhotosAlbum : .camera;
        present(imagePicker, animated: true, completion: nil);
       
    }
    
    func configButtonUpload(enable:Bool)  {
        self.buttonNext.isEnabled = enable;
        let text = enable ? "Proximo" : "Carregando";
        self.buttonNext.setTitle(text, for: .normal);
    }
    
    func enableButton(enable:Bool) {
        self.buttonNext.isEnabled = enable;
        let color = enable ? UIColor(red: 0.553, green: 0.369, blue: 0.749, alpha: 1) : UIColor.gray;
        self.buttonNext.backgroundColor = color;
    }
    
    
    //quando clickado  no botao proximo
    @IBAction func next(_ sender: Any) {
        
        //configura o botao
        self.configButtonUpload(enable: false);
        
        //recupera a instancia de armazenamento do firabase
        let store = Storage.storage().reference();
        let images = store.child("imagens");
        
        //recuperar imagem
        if let imageSelected = self.image.image{
            
            //converte a imagem
            if let imageData = UIImageJPEGRepresentation(imageSelected, 0.5)
            {
                images.child("\(self.idImage).jpg").putData(imageData, metadata: nil)
                {
                    (metaData, errorPutData) in
                    
                    if errorPutData == nil
                    {
                        let imageRef = Storage.storage().reference(withPath: "imagens").child("\(self.idImage).jpg");
                        imageRef.downloadURL(completion: { (url, errorGetUrl) in
                            if errorGetUrl == nil
                            {
                                
                                if let urlFile = url?.absoluteString
                                {
                                    self.performSegue(withIdentifier: "selectUserSegue", sender: urlFile);
                                    self.configButtonUpload(enable: true);
                                    
                                    //teste
                                    print("URL: \(String(describing: urlFile))");
                                    
                                }
                                
                            }
                            else
                            {
                                let alert = Alert(title: "Falha", message: "Falha ao obter a URL do arquivo salvo, tente novamente!");
                                self.present(alert.getAlert(), animated: true, completion: nil)
                                print("Erro: \(String(describing: errorGetUrl?.localizedDescription))");
                                
                            }
                        })
//                        images.downloadURL(completion: { (url, error) in
//                            if error == nil
//                            {
//
//
//                                let urlFile = url?.absoluteString;
//                                self.performSegue(withIdentifier: "selectUserSegue", sender: urlFile);
//                                self.configButtonUpload(enable: true);
//
//                                //teste
//                                print("URL: \(String(describing: urlFile))");
//                            }
//                            else
//                            {
//                                let alert = Alert(title: "Falha", message: "Falha ao obter a URL do arquivo salvo, tente novamente!");
//                                self.present(alert.getAlert(), animated: true, completion: nil)
//                                print("Erro: \(error?.localizedDescription)");
//
//                            }
//                        })
                    }
                    else
                    {
                        let alert = Alert(title: "Upload Falhou", message: "Erro ao salvar o arquivo, tente novamente!");
                        self.present(alert.getAlert(), animated: true, completion: nil);
                        self.configButtonUpload(enable: true);
                    }
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "selectUserSegue"{
            
            let userViewController = segue.destination as! UsersViewController;
            userViewController.descImage = self.textDesc.text!;
            userViewController.urlImage = sender as! String;
            userViewController.idImage = self.idImage;
            
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
    
    
//    service firebase.storage {
//    match /b/{bucket}/o {
//    match /{allPaths=**} {
//    allow read, write: if request.auth != null;
//    }
//    }
//    }

}
