(function () {
    var inviteController = function ($scope, $rootScope, $users, $auth, appConstants) {
        $scope.pageClass = "invite full";
       
        
    };

    inviteController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("inviteController", inviteController);

}());


