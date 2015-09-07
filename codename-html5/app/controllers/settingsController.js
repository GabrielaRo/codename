(function () {
    var settingsController = function ($scope, $rootScope, $users, $trace, $error) {
        $scope.pageClass = "settings";
        $(window).scrollTop(0);
        $scope.sharedLocations = [];
        $scope.formStatus;
        $scope.currentSection = "password";


        $scope.updatePassword = function (oldPassword, newPassword, newPasswordConfirm) {
            if ($scope.passwordForm.$valid) {

                if (newPassword == newPasswordConfirm) {
                    $users.updatePassword(oldPassword, newPassword).success(function (data) {

                        $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Password Updated", 'success');
                        $scope.formStatus = "";

                    }).error(function (data, status) {
                        $scope.formStatus = "Incorrect Password"
                        $error.handleError(data, status);
                    });


                } else {
                    $scope.formStatus = "Passwords doesn't match."
                    // $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Passwords doesn't match! ", 'error');

                }
            } else {
                console.log($scope.passwordForm.$valid);
                $scope.formStatus = "Fill all fields";
            }

        }

        $scope.setSection = function (section) {
            $scope.currentSection = section;
        }

        $scope.loadSharedLocations = function () {
            $trace.getSharedLocations().success(function (data) {

                $scope.sharedLocations = data;
            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };

        $scope.removeSharedLocation = function (loc) {
            $trace.removeSharedLocation(loc.id).success(function (data) {
                var idx = $scope.sharedLocations.indexOf(loc);
                $scope.sharedLocations.splice(idx, 1);

            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };


        $scope.clearSharedLocations = function () {
            $trace.clearSharedLocations().success(function (data) {
                $scope.sharedLocations = [];
            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };

        $scope.loadSharedLocations();

    };

    settingsController.$inject = ['$scope', '$rootScope', '$users', '$trace', '$error'];
    angular.module("codename").controller("settingsController", settingsController);

}());


