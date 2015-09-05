(function () {
    var settingsController = function ($scope, $rootScope, $users, $auth, $trace, appConstants) {
        $scope.pageClass = "contact";
        $(window).scrollTop(0);
        $scope.sharedLocations = [];
        $scope.updatePassword = function (oldPassword, newPassword, newPasswordConfirm) {
            if (newPassword == newPasswordConfirm) {
                $users.updatePassword(oldPassword, newPassword).success(function (data) {

                    $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Password Updated", 'success');
                }).error(function (data) {
                    $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                    console.log("Error : " + data + "!");

                });
            } else {
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Passwords doesn't match! ", 'error');

            }

        }
        $scope.loadSharedLocations = function () {
            $trace.getSharedLocations().success(function (data) {

                $scope.sharedLocations = data;
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });
        };
        
        $scope.loadSharedLocations();

    };

    settingsController.$inject = ['$scope', '$rootScope', '$users', '$auth', '$trace', 'appConstants'];
    angular.module("codename").controller("settingsController", settingsController);

}());


