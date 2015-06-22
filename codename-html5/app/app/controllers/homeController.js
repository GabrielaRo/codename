(function () {
    var homeController = function ($scope, $rootScope, $users, appConstants) {
        $scope.imagePath = "static/img/public-images/";

    

      

       

       

        if ($scope.auth_token && $scope.auth_token !== "") {

            console.log("loading private clubs because: " + $scope.auth_token);
           
            //
        } else {
            console.log("loading public clubs because: " + $scope.auth_token);
            

        }
        


        
    };

    homeController.$inject = ['$scope', '$rootScope', '$users', 'appConstants'];
    angular.module("codename").controller("homeController", homeController);

}());


