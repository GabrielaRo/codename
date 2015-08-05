(function () {
    var inviteController = function ($scope, $rootScope, $users, $auth, appConstants) {
       $scope.pageClass = "invite full";
       $scope.inviteStatus = $rootScope.inviteStatus;
        
       $rootScope.$watch('inviteStatus', function() {
           $scope.inviteStatus = $rootScope.inviteStatus;
       });
        
    };

    inviteController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants'];
    angular.module("codename").controller("inviteController", inviteController);

}());


