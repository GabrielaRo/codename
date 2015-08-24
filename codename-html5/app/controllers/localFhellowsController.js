(function () {
    var localFhellowsController = function ($scope, $rootScope, $users, $interests, $chat, location, appConstants, reverseGeocoder) {
        $scope.imagePath = "static/img/public-images/";
        $scope.filters = {location: '', proximity: 200, type: "", search: ""};
        $scope.filtersType = [];
        $scope.filtersLookingTo = [];
        $scope.tags = [];
        $scope.tagsText = [];
        $scope.serverUrlFull = appConstants.server + appConstants.context;
        $scope.fhellowsList = [];
        $scope.fhellowPerPage = 10;
        $scope.currentPage = 0;

        $scope.currrentOffset = 0;
        $scope.noMoreResults = false;

        $scope.shareLocation = function () {
            location.get(angular.noop, angular.noop);
            location.ready(function () {
                reverseGeocoder.geocode(location.current)
                        .then(function (results) {
                            console.log("Result [0]");
                            console.log(results[0]);
                            $scope.resetPaging();
                            
                            var locData = {
                                latitude: location.current.latitude,
                                longitude: location.current.longitude,
                                name: results[0].address_components[6].short_name,
                                description: results[0].address_components[6].short_name + " , " +results[0].address_components[3].short_name
                            };
                            $scope.selectedLocation = locData;
                            $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                                    $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
                            var el = angular.element(document.querySelectorAll("#myLocationText"));
                            el[0].value = 'Current Location';
                            $scope.lookedUpLocation = locData;
                            console.log("Current location is: ");
                            console.log(locData);

                        });
            });

        };

        $scope.showMore = function () {
            $scope.currentPage = $scope.currentPage + 1;



            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };
        $scope.resetPaging = function () {
            $scope.fhellowsList = [];
            $scope.currentPage = 0;
            $scope.currrentOffset = ($scope.currentPage * $scope.fhellowPerPage);
            $scope.currrentLimit = (($scope.currentPage + 1) * $scope.fhellowPerPage);

        }
        $scope.selectAddress = function () {
            if ($scope.lookedUpLocation) {
                $scope.resetPaging();
                $scope.selectedLocation = $scope.lookedUpLocation;

                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };

        $scope.interestsTagAdded = function ($tag) {

            $scope.tagsText.push($tag.text);

            $scope.resetPaging();
            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };
        $scope.interestsTagRemoved = function ($tag) {

            $scope.tagsText.splice($scope.tagsText.indexOf($tag.text), 1);
            $scope.resetPaging();
            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
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
            $scope.resetPaging();
            if ($scope.lookedUpLocation) {

                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
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
            $scope.resetPaging();
            if ($scope.lookedUpLocation) {

                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude,
                        $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            }
        }

        $scope.loadFhellowsInRange = function (lon, lat, tags, lookingFors, categories) {
            $scope.noMoreResults = false;
            if ($scope.fhellowsList.length > 0) {
                $scope.currentRange = $scope.fhellowsList[$scope.fhellowsList.length - 1].rangeCode;
                $scope.currrentOffset = $scope.fhellowsList[$scope.fhellowsList.length - 1].offset + 1;
            } else {
                $scope.currrentOffset = 0;
                $scope.currentRange = "NA";
            }

            console.log("Current range: " + $scope.currentRange + " From : " + $scope.currrentOffset);
            $users.search(lon, lat, tags, lookingFors, categories, $scope.currentRange, $scope.currrentOffset, $scope.fhellowPerPage).success(function (data) {
                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for all ranges : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                } else {
                    console.log("no data for all ranges");
                    $scope.noMoreResults = true;
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });
        }




        $scope.loadFhellows = function (tags, lookingFors, categories) {
            $scope.noMoreResults = false;
            if ($scope.fhellowsList.length > 0) {
                $scope.currentRange = $scope.fhellowsList[$scope.fhellowsList.length - 1].rangeCode;
                $scope.currrentOffset = $scope.fhellowsList[$scope.fhellowsList.length - 1].offset + 1;
            } else {
                $scope.currrentOffset = 0;
                $scope.currentRange = "NA";
            }

            console.log("Current range: " + $scope.currentRange + " From : " + $scope.currrentOffset);

            $users.search(0.0, 0.0, tags, lookingFors, categories, $scope.currentRange, $scope.currrentOffset, $scope.fhellowPerPage).success(function (data) {

                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for the world : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                } else {
                    console.log("no data for all ranges");
                    $scope.noMoreResults = true;
                }
            }).error(function (data) {
                console.log("Error: ");
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };

        $scope.resetPaging();
        $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

        $(window).scroll(function () {

            if ($(window).scrollTop() > 235) {
                $(".sticky").addClass("isSticky");
            } else {
                $(".sticky").removeClass("isSticky");
            }
        });

    };

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users', '$interests', '$chat', 'location', 'appConstants', 'reverseGeocoder'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


