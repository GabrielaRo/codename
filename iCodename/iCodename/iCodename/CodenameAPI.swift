//
//  CodenameAPI.swift
//  iCodename
//
//  Created by Claudio Ibañez on 27/6/15.
//  Copyright (c) 2015 utilmobile. All rights reserved.
//

import Foundation
import UIKit

protocol CodeNameSearchAPIProtocol {
    func didReceiveResponse(results: NSArray)
}

class CodeNameSearchAPI: NSObject {
    var data: NSMutableData = NSMutableData()
    var delegate: CodeNameSearchAPIProtocol?
    
    //Search codename
    func searchCodenameFor(searchTerm: String) {
        
        //Clean up the search term by replacing spaces with +
        
        var codenameSearchTerm = searchTerm.stringByReplacingOccurrencesOfString(" ", withString: "+", options: NSStringCompareOptions.CaseInsensitiveSearch, range: nil)

        var escapedSearchTerm = codenameSearchTerm.stringByAddingPercentEscapesUsingEncoding(NSUTF8StringEncoding)
        
        //var urlPath = "https://itunes.apple.com/search?term=\(escapedSearchTerm!)&media=music"

        //http://localhost:8080/codename-server/rest/public/users/all
        var urlPath = "http://localhost:8080/codename-server/rest/public/users/all"
        var url: NSURL = NSURL(string: urlPath)!
        
        var request: NSURLRequest = NSURLRequest(URL: url)
        
        var connection: NSURLConnection = NSURLConnection(request: request,
            delegate: self, startImmediately: false)!
        
        println("Search codename API at URL \(url)")
        
        connection.start()
    }
    
    //NSURLConnection delegate method
    
    func connection(connection: NSURLConnection!, didFailWithError error:NSError!) {
        println("Failed with error \(error.localizedDescription)")
    }
    
    //NSURLConnection delegate method
    
    func connection(connection: NSURLConnection!, didReceiveData data:NSData) {
        //append incoming data
        self.data.appendData(data)
    }
    
    //NSURLConnection delegate method
    
    func connectionDidFinishLoading(connection:NSURLConnection!) {
        //Finished receiving data and convert it to JSON object
        
        var err: NSError?
        var jsonResult: NSArray = NSJSONSerialization.JSONObjectWithData(data, options: nil, error: &err) as NSArray
        
        if err? != nil {
            NSLog("error de parseo: \(err?.description)")
        }

        if jsonResult.count > 0 {
            NSLog("Resultado: \(jsonResult)")
        }
        
        delegate?.didReceiveResponse(jsonResult)
        
    }

}