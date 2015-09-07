(function () {
    var adminController = function ($scope, $rootScope, $users, $contact,  $error) {


        $scope.registerUser = function (isValid) {

            if (isValid) {
                $users.signup($scope.newUser.email, $scope.newUser.pass).success(function (data) {

                    $rootScope.$broadcast("goTo", "/admin");
                    $scope.registerForm.$setPristine();



                }).error(function (data, status) {
                    $error.handleError(data, status);
                });
            } else {
                console.log("Error : Invalid Form");
            }
        };

        $scope.getNotRepliedContactMessages = function () {


            $contact.getNotRepliedContactMessages().success(function (data) {

                console.log(data);

            }).error(function (data, status) {
                $error.handleError(data, status);
            });

        };


    };

    adminController.$inject = ['$scope', '$rootScope', '$users', '$contact', '$error'];
    angular.module("codename").controller("adminController", adminController);

}());


