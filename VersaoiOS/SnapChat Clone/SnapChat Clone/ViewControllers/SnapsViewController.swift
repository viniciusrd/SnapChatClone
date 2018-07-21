//
//  SnapsViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 14/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseDatabase

class SnapsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    var snaps: [Snap] = [];

    override func viewDidLoad() {
        super.viewDidLoad()

        //nao necessario essas linhas abaixo devido ja ter sido definido na interface mainStoryBoard
//        self.tableView.delegate = self;
//        self.tableView.dataSource = self;

        let auth = Auth.auth();
        
        if let idUserLogged = auth.currentUser?.uid{
            
            let database = Database.database().reference();
            let users = database.child("usuarios");
            
            let snaps = users.child(idUserLogged).child("snaps");
            
            //criar ouvinte para receber snaps
            snaps.observe(DataEventType.childAdded) { (snapshot) in
                
                let dataSnap = snapshot.value as! NSDictionary
                
                let snapForUserLogged = Snap();
                
                snapForUserLogged.id = snapshot.key;
                snapForUserLogged.name = dataSnap["name"] as! String;
                snapForUserLogged.desc = dataSnap["desc"] as! String;
                snapForUserLogged.urlImage = dataSnap["urlImage"] as! String;
                snapForUserLogged.idImage = dataSnap["idImage"] as! String;
                
                self.snaps.append(snapForUserLogged);
                self.tableView.reloadData();
            }
            
            //Adicionar evento para intem removido
            snaps.observe(DataEventType.childRemoved) { (snapshot) in
                
                print(snapshot);
                var index = 0;
                for snap in self.snaps{
                    print("atual index: "+String(index))
                    if snap.id == snapshot.key {
                        self.snaps.remove(at: index);
                        print("Removed: "+snap.id)
                    }
                    index += 1;
                }
                
                self.tableView.reloadData();
            }
            
            
        }
        
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
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let totalSnap = snaps.count;
        
        if totalSnap == 0 {
            return 1
        }else{
            return totalSnap
        }

        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "cellSnap", for: indexPath)
        
        let totalSnaps = snaps.count;
        if totalSnaps == 0 {
            cell.textLabel?.text = "Você ainda não tem nenhum snap..."
        }else{
            
            let snapForViewCell = self.snaps[ indexPath.row ];
            cell.textLabel?.text = snapForViewCell.name;
            
        }
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let total = snaps.count;
        
        if total > 0 {
            
            let snap = self.snaps[indexPath.row];
            self.performSegue(withIdentifier: "detailsSnapSegue", sender: snap);
        }
    }
    
    // envia o objeto para uma viewcontroller
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if segue.identifier == "detailsSnapSegue"{
            
            let detailsSnapViewController = segue.destination as! DetailsViewController;
            
            detailsSnapViewController.snapReceived = sender as! Snap;
            
        }
        
    }
    
}
