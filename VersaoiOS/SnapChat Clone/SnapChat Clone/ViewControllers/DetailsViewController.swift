//
//  DetailsViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 21/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
//bliblioteca instalada do cocoa pods
import SDWebImage
import FirebaseAuth
import FirebaseDatabase
import FirebaseStorage

class DetailsViewController: UIViewController {

    @IBOutlet weak var imageSnap: UIImageView!
    @IBOutlet weak var detailsSnap: UILabel!
    @IBOutlet weak var countTime: UILabel!
    
    var snapReceived = Snap();
    var time = 11;
    
    override func viewDidLoad() {
        super.viewDidLoad()

        detailsSnap.text = "Carregando...";
        let url = URL(string: snapReceived.urlImage)
        imageSnap.sd_setImage(with: url) { (image, error, cache, url) in
            
            if error == nil{

                self.detailsSnap.text = self.snapReceived.desc;
                //inicializando o timer
                Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { (timer) in
                    
                    //decrementar o time
                    self.time = self.time - 1;
                    
                    //exibir o timer
                    self.countTime.text = String (self.time);
                    
                    if self.time == 0 {
                        timer.invalidate();
                        self.dismiss(animated: true, completion: nil);
                    }
                })

            }else{
                print("imagem nao exibida: \(String(describing: error?.localizedDescription))");
            }
        }
        print(snapReceived.urlImage);
        // Do any additional setup after loading the view.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        
        let auth = Auth.auth();
        if let idUserLogged = auth.currentUser?.uid{
            
            //remover nos do database
            let database = Database.database().reference();
            let users = database.child("usuarios");
            let snaps = users.child(idUserLogged).child("snaps");

            snaps.child(snapReceived.id).removeValue();

            //remover imagem do snap no storage
            let store = Storage.storage().reference();
            let imagens = store.child("imagens")

            imagens.child("\(snapReceived.idImage).jpg").delete { (error) in

                if error == nil{
                    print("sucesso ao excluir");
                }else{
                    print("erro ao excluir");
                }
            }
        }
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

}
