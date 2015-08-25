(function () {
    var adminController = function ($scope, $rootScope, $users, $contact, $auth, appConstants) {


        $scope.registerUser = function (isValid) {

            if (isValid) {
                $users.signup($scope.newUser.email, $scope.newUser.pass).success(function (data) {


                    $rootScope.$broadcast("goTo", "/admin");
                    $scope.registerForm.$setPristine();



                }).error(function (data) {
                    $rootScope.$broadcast("quickNotification", " <i class='fa fa-exclamation-triangle'></i>Something failed: " + data.error, 'error');
                    console.log("Error : " + data.error + "!");

                });
            } else {
                console.log("Error : Invalid Form");
            }
        };

        $scope.getNotRepliedContactMessages = function () {


            $contact.getNotRepliedContactMessages().success(function (data) {

                console.log(data);

            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", " <i class='fa fa-exclamation-triangle'></i>Something failed: " + data.error, 'error');
                console.log("Error : " + data.error + "!");

            });

        };


    };

    adminController.$inject = ['$scope', '$rootScope', '$users', '$contact', '$auth', 'appConstants'];
    angular.module("codename").controller("adminController", adminController);

}());


