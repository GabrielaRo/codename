'use strict';

/**
 * @ngdoc service
 * @name codebaseFrontendApp.rest
 * @description
 * # rest
 * Factory in the codebaseFrontendApp.
 */
angular.module('codebaseFrontendApp')
  .factory('rest', ['$http', '$cookies', function($http, $cookies ) {

    var urlBase = '/api/';
    var headers = {'Content-Type': 'application/x-www-form-urlencoded'};
      
          var regHeaders = { headers :{ 
                                       'Access-Control-Allow-Headers': '*'}};
      

    var authHeaders = { headers :{
                      service_key: 'webkey:' + $cookies.get('user_email'), auth_token: $cookies.get('user_token')}};
      
      
   
    var server = 'http://localhost:8080/';
    var address = "localhost";
    var port = "8080";
    var context = 'codename-server/';
      var totalPath = server + context;
    var restFactory = {};

    restFactory.get = function (ext) {
        
        console.log(JSON.stringify(authHeaders));
        return $http.get(totalPath + ext, authHeaders);
    };

      //post(url, data, [config]
    restFactory.post = function (ext, data) {
       
        console.log('total path: ' + JSON.stringify(totalPath) + 'ext: '+ JSON.stringify(ext) + "data: " + JSON.stringify(data));
        
        return $http.post(totalPath + ext, data);
    };
    
      //post(url, data, [config]
    restFactory.authPost = function (ext, data) {
        return $http.post(urlBase + ext, data, authHeaders);
    };
      
    restFactory.testall = function () {
        return 'test path';
    };

    return restFactory;
}]);