(function () {
    var termsController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "terms";
 
    };

    termsController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("termsController", termsController);

}());


