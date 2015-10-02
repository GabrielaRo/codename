'use strict';

/**
 * @ngdoc function
 * @name codebaseFrontendApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the codebaseFrontendApp
 */
angular.module('codebaseFrontendApp')
  .controller('MainCtrl', ['$scope', '$rootScope', 'register', 'userService', function($scope, $rootScope, register, user) {
   // register.login('facebook');
      
 
      user.login({email:'grogdj@gmail.com', password:'asdasd'});
}]);