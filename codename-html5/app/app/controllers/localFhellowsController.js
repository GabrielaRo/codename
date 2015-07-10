(function () {
    var localFhellowsController = function ($scope, $rootScope, $queries, location, appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.filters = {location: '', proximity: 200, type: "", search: ""};
        $scope.filtersType = [];
        $scope.filtersLookingTo = [];

        $scope.serverUrlFull = appConstants.server + appConstants.context;

        location.get(angular.noop, angular.noop);
        

        $scope.selectAddress = function () {
            console.log($scope.lookedUpLocation);
            if($scope.lookedUpLocation){
                $scope.selectedLocation = $scope.lookedUpLocation;
               // $scope.loadFhellows($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, interests, lookingFor, categories);
            }
        };

        $scope.$watch('lookedUpLocation', $scope.selectAddress);

        $scope.tags = [];

        $scope.loadInterests = function ($query) {
            return [
                {text: 'design'},
                {text: 'development'},
                {text: 'other'}
            ];
        }


        $scope.typeButtonPressed = function (buttonName) {
            
            if($scope.filtersType.indexOf(buttonName) == -1){
                $scope.filtersType.push(buttonName);
            }else {
                $scope.filtersType.splice ($scope.filtersType.indexOf(buttonName), 1);   
            }
           
        }
        $scope.lookingToButtonPressed = function (buttonName) {
            if($scope.filtersLookingTo.indexOf(buttonName) == -1){
                $scope.filtersLookingTo.push(buttonName);
            }else {
                $scope.filtersLookingTo.splice ($scope.filtersLookingTo.indexOf(buttonName), 1);   
            }
        }

        $scope.loadFhellows = function (lon, lat, interests, lookingFor, categories) {


            $queries.getByLocation(lon, lat, interests, lookingFor, categories).success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Clubs loaded!");
                $scope.fhellowsList = data;
                console.log($scope.fhellowsList);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };




       // $scope.loadFhellows();


        //FHELLOW FILTERS

        $scope.userPosition = function (position) {
            console.log(position);
            $scope.filters.location = "Lat " + (Math.round(position.coords.latitude * 100) / 100) + " Long " + (Math.round(position.coords.longitude * 100) / 100);
        }

        $scope.getLocation = function () {

            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition($scope.userPosition);

            } else {
                alert("Geolocation is not supported by this browser.");
            }
        }





        $(window).scroll(function () {

            if ($(window).scrollTop() > 235) {
                $(".sticky").addClass("isSticky");
            } else {
                $(".sticky").removeClass("isSticky");
            }
        });

    };

    localFhellowsController.$inject = ['$scope', '$rootScope', '$queries', 'location', 'appConstants'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


