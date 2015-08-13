(function () {
    var contactController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "contact";
 
    };

    contactController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("contactController", contactController);

}());


