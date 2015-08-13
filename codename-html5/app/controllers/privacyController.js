(function () {
    var privacyController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "privacy";
 
    };

    privacyController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("privacyController", privacyController);

}());


