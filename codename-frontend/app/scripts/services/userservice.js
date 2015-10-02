'use strict';

/**
 * @ngdoc service
 * @name codebaseFrontendApp.userService
 * @description
 * # userService
 * Service in the codebaseFrontendApp.
 */
angular.module('codebaseFrontendApp')
   .factory('userService', ['$http', '$rootScope', '$cookies', 'rest', 'hello', function($http, $rootScope, $cookies, rest, hello){
                            
    var userFactory = {};
          
 
    /*
    *@Pram email
    *@Pram password
    */
    userFactory.signup = function (authObj) {
        rest.post('rest/auth2/register', authObj).then(function(result){
        
        });                 
    };
                            
    userFactory.logout = function () {
        rest.authPost('rest/auth2/logout', {}).then(function(result){

     });                 
    };
       
       
    userFactory.search = function () {
        
        var searchString ='?lon=0&lat=0&interests=[]&lookingFors=[]&categories=[]&range=NA&offset=0&limit=10&excludes=["grogdj"]';
                        
        rest.get('rest/users/search'+searchString, {}).then(function(result){
            console.log('GOT SEARCH' + JSON.stringify(result));


     });                 
    };
    
       
    userFactory.login = function (authObj) {


        rest.post('rest/auth2/login', authObj).then(function(result){
              $cookies.put('user_email', result.data.email);
             $cookies.put('user_token', result.data.auth_token);
            alert('result: ' + JSON.stringify(result));
            userFactory.search()
     });                 
    };
       
    return userFactory;
                            
}]);
