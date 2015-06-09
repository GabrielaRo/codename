(function (){
    var userInterestsController = function ($rootScope, $scope, $cookieStore, $users, $interests) {
        //GRID
        $scope.imagePath = "static/img/public-images/"
        $scope.interests = [];

        $scope.newProfile = function () {
            //console.log("creating profile for user " + user_id + " with email: " + email + " and auth_token: " + auth_token);
            $users.createProfile().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Profile created!");
                $cookieStore.put('firstLogin', false);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };
       
        
        $scope.loadUserInterests = function () {
            //console.log("creating profile for user " + user_id + " with email: " + email + " and auth_token: " + auth_token);
            $users.loadInterests().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Users Tags loaded!");
                $scope.selectedInterests = data;
                console.log("User interests: "+$scope.selectedInterests);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };
        
        $scope.loadAllInterests = function () {
            console.log("loading all interests");

            $interests.loadAll().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "User Interest loaded!");
                $scope.interests = data;
                $scope.initImages();
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.updateInterests = function () {
            //console.log("updating interests for user " + user_id + " with email: " + email + " and auth_token: " + auth_token);
            console.log("selected interests: "+$scope.selectedInterests);
            $users.updateInterests($scope.selectedInterests).success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Interest updated !");
                $rootScope.$broadcast("goTo", "/settings");
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };
        var firstLogin = $cookieStore.get('firstLogin');
        if(firstLogin){
            $scope.newProfile();
        }
        $scope.loadUserInterests();
        $scope.loadAllInterests();


        $scope.initImages = function(){
            $scope.numberOfItem = Math.floor($(window).width() / 300);
            $scope.pageClass = "firstLoginScreen";
            $scope.elementWidth = (($(window).width()-4) / $scope.numberOfItem)-4;
            $scope.elementHeight = (($(window).height() - 4) / Math.ceil(($scope.interests.length / $scope.numberOfItem)))-4;

        }


        $(window).resize(function () {
            $scope.numberOfItem = Math.floor($(window).width() / 300);
            $scope.elementWidth = (($(window).width()-4) / $scope.numberOfItem)-4;
            $scope.elementHeight = (($(window).height() - 4) / Math.ceil(($scope.interests.length / $scope.numberOfItem)))-4;
            $scope.$digest();

        });

        //CLICK CHECK
        $scope.selectedInterests = [];
        $scope.toggleCheck = function (value) {

            if ($scope.selectedInterests.map(function(e) { return e.name; }).indexOf(value.name) > -1) {
                $scope.selectedInterests.splice($scope.selectedInterests.map(function(e) { return e.name; }).indexOf(value.name), 1);
            } else {
                $scope.selectedInterests.push(value);
            }

            console.log($scope.selectedInterests);
        }

        $scope.isChecked = function (value) {
                return $scope.selectedInterests.map(function(e) { return e.name; }).indexOf(value.name) > -1;
        };
    };
    
    userInterestsController.$inject = ['$rootScope','$scope','$cookieStore', '$users', '$interests'];
    angular.module( "codename").controller("userInterestsController", userInterestsController);

}());

