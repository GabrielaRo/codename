(function(){
    var signUpController = function ($scope, $rootScope, $users) {
         $scope.registerUser = function (isValid) {
        // console.log("asd " + $scope.newUser.email + " / " + $scope.newUser.pass);

            $scope.submitted = true;

            if (isValid) {
               $users.signup($scope.newUser.email, $scope.newUser.pass).success(function (data) {
                   // $rootScope.$broadcast("quickNotification", "You are  now registered, please login!");
                    
                   $rootScope.$broadcast("goTo", "/");
                    $scope.registerForm.$setPristine();
                    console.log("Welcome to " + $scope.newUser.email + "!");
                    
                   
                }).error(function (data) {
                    $rootScope.$broadcast("quickNotification", "Something failed: "+data.error, 'error');
                    console.log("Error : " + data.error + "!");

                });
            } else {
                console.log("Invalid Form");
            }
        };
    };
    
    signUpController.$inject = ['$scope', '$rootScope', '$users'];
    angular.module( "codename" ).controller("signUpController", signUpController);
    
}());

