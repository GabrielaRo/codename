(function () {
    var localFhellowsController = function ($scope, $rootScope, $users, $interests, appConstants) {
        $scope.imagePath = "static/img/public-images/";

        $scope.interests = [];
        $scope.allInterests = [];
        $scope.userInterests = [];
        
        $scope.loadUserInterests = function () {
            //console.log("loading interests for user " + user_id + " with email: " + email + " and auth_token: " + auth_token);

            $users.loadInterests().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "User Interest loaded!");
                $scope.interests = data;
                for (x in $scope.interests) {
                    $scope.userInterests.push($scope.interests[x].name);
                }
                console.log($scope.userInterests);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadAllInterests = function () {
            console.log("loading all interests");

            $interests.loadAll().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "User Interest loaded!");
                $scope.allInterests = data;
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellows = function () {


            $users.loadAll().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Clubs loaded!");
                $scope.fhellowsList = data;
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };
       

       

//        if ($scope.auth_token && $scope.auth_token !== "") {
//
//            console.log("loading private clubs because: " + $scope.auth_token);
//           
//            //
//        } else {
//            console.log("loading public clubs because: " + $scope.auth_token);
//            
//
//        }
        $scope.loadFhellows();
        $scope.loadAllInterests();


        //CUSTOM FILTER FOR USER INTERESTS
        $scope.checkUserInterest = function (interest) {

            var temp = false;

            if ($scope.showAllInterests) {
                temp = true;
            } else {

                if ($scope.userInterests.length > 0) {
                    for (i = 0; i < $scope.userInterests.length; i++) {
                        if (interest.interest === $scope.userInterests[i]) {

                            temp = true;
                            break;
                        }
                    }
                } else {
                    temp = true;
                }
            }
            return temp;
        };

    };

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users', '$interests'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


