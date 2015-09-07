(function () {
    var loginController = function ($scope) {
       $scope.pageClass = "login full";
 
    };

    loginController.$inject = ['$scope'];
    angular.module("codename").controller("loginController", loginController);

}());


