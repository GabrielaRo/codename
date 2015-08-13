(function () {
    var loginController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "login full";
 
    };

    loginController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("loginController", loginController);

}());


