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
                $scope.loadFhellowsByLocation($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, [], [], []);
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

        $scope.loadFhellowsByLocation = function (lon, lat) {
            console.log("lon = "+ lon);
            console.log("lat = "+ lat);

            $queries.getByLocation(lon, lat).success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Clubs loaded!");
                console.log("Fhellows: ");
                console.log(data);
                $scope.fhellowsList = data;
                console.log($scope.fhellowsList);
            }).error(function (data) {
                console.log("Error: ");
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };


        $scope.loadFhellows = function () {
           

            $queries.getAll().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Clubs loaded!");
                console.log("Fhellows: ");
                console.log(data);
                $scope.fhellowsList = data;
                console.log($scope.fhellowsList);
            }).error(function (data) {
                console.log("Error: ");
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellows();

       





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


