(function () {
    var settingsController = function ($scope, $rootScope, $users, $auth, $trace, appConstants) {
        $scope.pageClass = "settings";
        $(window).scrollTop(0);
        $scope.sharedLocations = [];
        $scope.formStatus;
        $scope.currentSection = "password";
        
        
        $scope.updatePassword = function (oldPassword, newPassword, newPasswordConfirm) {
            if($scope.passwordForm.$valid){
              
                if (newPassword == newPasswordConfirm) {
                    $users.updatePassword(oldPassword, newPassword).success(function (data) {

                        $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Password Updated", 'success');
                        $scope.formStatus = "";
                  
                    }).error(function (data) {
                        $scope.formStatus = "Incorrect Password"
                        //$rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                        console.log("Error : " + data + "!");

                    });
                } else {
                    $scope.formStatus = "Passwords doesn't match."
                   // $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Passwords doesn't match! ", 'error');

                }
            }else {
                console.log($scope.passwordForm.$valid);
                $scope.formStatus = "Fill all fields";
            }

        }
        
        $scope.setSection = function(section){
            $scope.currentSection = section;
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


