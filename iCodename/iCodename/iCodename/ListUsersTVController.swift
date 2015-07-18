//
//  ListUsersTVController.swift
//  iCodename
//
//  Created by Claudio Ibañez on 27/6/15.
//  Copyright (c) 2015 utilmobile. All rights reserved.
//

import Foundation
import UIKIt

class ListUsersTVController: UITableViewController, CodeNameSearchAPIProtocol {
    var api:CodeNameSearchAPI = CodeNameSearchAPI()
    var tableData: NSArray = NSArray()
    var imageCache = NSMutableDictionary()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Do any additional setup after loading the view, typically from a nib.
        
        api.delegate = self
        api.searchCodenameFor("pink floyd") //habria que cambiarlo!!!
        
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        //Dispose of any resource that can be recreated.
    }
    
    func didReceiveResponse(results: NSArray) {
        //store the results in our table data array
        println(results)
        
        if results.count > 0 {
            self.tableData = results
            self.tableView.reloadData()
        }
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let kCellIdentifier: String = "MyCell"

        //the tablecell is optional to see if we can reuse cell
        var cell: UITableViewCell?
        cell = tableView.dequeueReusableCellWithIdentifier(kCellIdentifier) as? UITableViewCell
        
        //If we did not get a reuseable cell, then create a new one
        if cell == nil {
            cell = UITableViewCell(style: UITableViewCellStyle.Subtitle, reuseIdentifier: kCellIdentifier)
        }
        
        //Get our data row
        var rowData: NSDictionary = self.tableData[indexPath.row] as NSDictionary
        let cellText :String? = rowData["nickname"] as? String
        cell?.textLabel?.text = cellText
        // Get the track censored name
        var trackCensorName: NSString = rowData["location"] as NSString
        cell?.detailTextLabel?.text = trackCensorName
        
        
        return cell!
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //get the selected tracks information
        var rowData: NSDictionary = self.tableData[indexPath.row] as NSDictionary
        var name: String = rowData["nickname"] as String
        var releaseDate: String = rowData["bio"] as String
        //Show the alert view with the tracks information
        var alert : UIAlertView = UIAlertView()
        
        alert.title = name
        alert.message = releaseDate
        
        alert.addButtonWithTitle("Ok")
        alert.show()
    }
}