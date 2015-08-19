(function () {
    var localFhellowsController = function ($scope, $rootScope, $users, $interests, $chat, location, appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.filters = {location: '', proximity: 200, type: "", search: ""};
        $scope.filtersType = [];
        $scope.filtersLookingTo = [];
        $scope.tags = [];
        $scope.tagsText = [];
        $scope.serverUrlFull = appConstants.server + appConstants.context;
        $scope.fhellowsList = [];
        location.get(angular.noop, angular.noop);


        $scope.selectAddress = function () {
            if ($scope.lookedUpLocation) {
                $scope.selectedLocation = $scope.lookedUpLocation;
                $scope.fhellowsList = [];
                $scope.loadFhellowsWithin1KM($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
                $scope.loadFhellowsWithin3KM($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
                $scope.loadFhellowsWithin10KM($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
                $scope.loadFhellowsWithin50KM($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            }
        };
        $scope.eliminateDuplicates = function (arr) {
            var i,
                    len = arr.length,
                    out = [],
                    obj = {};

            for (i = 0; i < len; i++) {
                obj[arr[i]] = 0;
            }
            for (i in obj) {
                out.push(i);
            }
            return out;
        }
        $scope.interestsTagAdded = function ($tag) {

            $scope.tagsText.push($tag.text);


            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsByLocation($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };
        $scope.interestsTagRemoved = function ($tag) {

            $scope.tagsText.splice($scope.tagsText.indexOf($tag.text), 1);

            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsByLocation($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };

        $scope.$watch('lookedUpLocation', $scope.selectAddress);

        $scope.newConversation = function (selectedUser) {

            $chat.newConversation(selectedUser).success(function (data) {
                $rootScope.$broadcast('goTo', "/messages/" + data);

            }).error(function (data) {
                console.log("Error: ");
                $rootScope.$broadcast("quickNotification", "Something went wrong creating a new conversations!" + data);
            });

        }



        $scope.loadInterests = function ($query) {
            return $scope.interests.filter(function (interest) {
                return interest.text.toLowerCase().indexOf($query.toLowerCase()) != -1;
            });

        }

        $scope.loadInterestOnInit = function () {
            $interests.getAll().success(function (data) {
                $scope.interests = data;

            }).error(function (data) {
                console.log("Error: ");
                $rootScope.$broadcast("quickNotification", "Something went wrong loading the interests!" + data);
            });

        }
        $scope.loadInterestOnInit();
        $scope.typeButtonPressed = function (buttonName) {

            if ($scope.filtersType.indexOf(buttonName) == -1) {
                $scope.filtersType.push(buttonName);
            } else {
                $scope.filtersType.splice($scope.filtersType.indexOf(buttonName), 1);
            }
            if ($scope.lookedUpLocation) {

                $scope.loadFhellowsByLocation($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        }
        $scope.lookingToButtonPressed = function (buttonName) {

            if ($scope.filtersLookingTo.indexOf(buttonName) == -1) {
                $scope.filtersLookingTo.push(buttonName);
            } else {
                $scope.filtersLookingTo.splice($scope.filtersLookingTo.indexOf(buttonName), 1);
            }
            if ($scope.lookedUpLocation) {

                $scope.loadFhellowsByLocation($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            }
        }

        $scope.loadFhellowsWithin1KM = function (lon, lat, tags, lookingFors, categories) {
//            console.log("lon = " + lon);
//            console.log("lat = " + lat);
//            console.log("lookingFors = ");
//            console.log(lookingFors);
//            console.log("categories = ");
//            console.log(categories);
            $users.search(lon, lat, tags, lookingFors, categories, '_1KM', 0, 20).success(function (data) {
                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for 1km : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                } else {
                    console.log("no data for 1km");
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellowsWithin3KM = function (lon, lat, tags, lookingFors, categories) {
//            console.log("lon = " + lon);
//            console.log("lat = " + lat);
//            console.log("lookingFors = ");
//            console.log(lookingFors);
//            console.log("categories = ");
//            console.log(categories);
            $users.search(lon, lat, tags, lookingFors, categories, '_3KM', 0, 20).success(function (data) {
                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for 3km : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                    $scope.eliminateDuplicates($scope.fhellowsList);
                } else {
                    console.log("no data for 3km");
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellowsWithin10KM = function (lon, lat, tags, lookingFors, categories) {
//            console.log("lon = " + lon);
//            console.log("lat = " + lat);
//            console.log("lookingFors = ");
//            console.log(lookingFors);
//            console.log("categories = ");
//            console.log(categories);
            $users.search(lon, lat, tags, lookingFors, categories, '_10KM', 0, 20).success(function (data) {
                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for 10km : " + data.length);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                    $scope.eliminateDuplicates($scope.fhellowsList);
                    console.log(data);
                } else {
                    console.log("no data for 10km");
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellowsWithin50KM = function (lon, lat, tags, lookingFors, categories) {
//            console.log("lon = " + lon);
//            console.log("lat = " + lat);
//            console.log("lookingFors = ");
//            console.log(lookingFors);
//            console.log("categories = ");
//            console.log(categories);
            $users.search(lon, lat, tags, lookingFors, categories, '_50KM', 0, 20).success(function (data) {

                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for 50km : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                    $scope.eliminateDuplicates($scope.fhellowsList);
                } else {
                    console.log("no data for 50km");
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };


        $scope.loadFhellows = function (tags, lookingFors, categories) {


            $users.search(0.0, 0.0, tags, lookingFors, categories, '_WORLD', 0, 20).success(function (data) {
                console.log("data for the world : " + data.length);
                $scope.fhellowsList = data;
            }).error(function (data) {
                console.log("Error: ");
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

        $(window).scroll(function () {

            if ($(window).scrollTop() > 235) {
                $(".sticky").addClass("isSticky");
            } else {
                $(".sticky").removeClass("isSticky");
            }
        });

    };

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users', '$interests', '$chat', 'location', 'appConstants'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


