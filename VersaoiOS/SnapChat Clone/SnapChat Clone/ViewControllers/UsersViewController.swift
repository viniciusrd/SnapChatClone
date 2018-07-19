//
//  UsersViewController.swift
//  SnapChat Clone
//
//  Created by Vinícius Rodrigues Duarte on 17/07/18.
//  Copyright © 2018 Vinícius Rodrigues Duarte. All rights reserved.
//

import UIKit
import FirebaseDatabase
import FirebaseAuth

class UsersViewController: UITableViewController {
    
    var users: [User] = []
    var urlImage = "";
    var descImage = "";
    var idImage = "";
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // recuperar todos usuarios cadastratos no nó do firebase
        let database = Database.database().reference();
        let users = database.child("usuarios");
        // adiciona evento novo usuario adicionado
        users.observe(DataEventType.childAdded) { (snapshot) in
        
            let datas = snapshot.value as? NSDictionary;
            let emailUser = datas!["email"] as! String;
            let nameUser = datas!["name:"] as! String;
            let idUser = snapshot.key;
            
            let user = User(email: emailUser, name: nameUser, uid: idUser);
            
            //adiciona usuario no array
            self.users.append(user);
            self.tableView.reloadData();
            print(self.users)
            
            
        }

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return self.users.count;
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "userReuseCell", for: indexPath)

        // Configure the cell...
        let user = self.users[ indexPath.row ];
        cell.textLabel?.text = user.name;
        cell.detailTextLabel?.text = user.email;
        

        return cell
    }
    
    //selecionar a celula e recuperar o usuario

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let userSelect = self.users[ indexPath.row ];
        let idUserSelect = userSelect.uid;
        
        let database = Database.database().reference();
        let usersFirebase = database.child("usuarios");
        let snaps = usersFirebase.child(idUserSelect).child("snaps");
        
        //consultar dados do usuario logado
        let auth = Auth.auth();
        if let idlUserLogged = auth.currentUser?.uid
        {
            
            let userLogged = usersFirebase.child(idlUserLogged);
            userLogged.observeSingleEvent(of: DataEventType.value)
            { (snapshot) in
                
                let dataSnaphot = snapshot.value as? NSDictionary;
                let snapConfig = [
                    "from":dataSnaphot!["email"] as! String,
                    "name":dataSnaphot!["name:"] as! String,
                    "desc":self.descImage,
                    "urlImage":self.urlImage,
                    "idImage":self.idImage
                ];
                snaps.childByAutoId().setValue(snapConfig);
            }
            
        }
        
        
        
    }
   

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
