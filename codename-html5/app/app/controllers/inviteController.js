(function () {
    var inviteController = function ($scope, $rootScope, $users, $auth, appConstants, $invites ) {
       $scope.pageClass = "invite full";
       $scope.inviteStatus = false;
        
    
       $scope.requestInvite = function (email) {
            console.log("requesting invite for: " + email);
            $invites.request(email).success(function (data) {
                $scope.inviteStatus = true;
                $rootScope.$broadcast("quickNotification", "Invitation Sended");


            }).error(function (data) {
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });

        }
        
    };

    inviteController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants', '$invites'];
    angular.module("codename").controller("inviteController", inviteController);

}());


