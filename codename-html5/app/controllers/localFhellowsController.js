(function () {
    var localFhellowsController = function ($scope, $rootScope, $users, $interests,
            location, appConstants, reverseGeocoder, $location, $presence, $trace, $error) {
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
        $scope.gettingLocation = false;

        $scope.shareLocation = function () {
            $scope.gettingLocation = true;
            location.get(angular.noop, angular.noop);

            location.ready(function () {

                reverseGeocoder.geocode(location.current)
                        .then(function (results) {
                            $scope.gettingLocation = false;
                            $scope.resetPaging();

                            var locData = {
                                latitude: location.current.latitude,
                                longitude: location.current.longitude,
                                name: results[0].address_components[6].short_name,
                                description: results[0].address_components[6].short_name + " , " + results[0].address_components[3].short_name
                            };
                            $scope.selectedLocation = locData;

                            if ($location.path().contains('localfhellows')) {
                                var el = angular.element(document.querySelectorAll("#myLocationText"));
                                el[0].value = 'Current Location';
                                $scope.lookedUpLocation = locData;
                            }
                            $trace.shareUserLocation($scope.lookedUpLocation.latitude,
                                    $scope.lookedUpLocation.longitude, $scope.selectedLocation.description).success(function (data) {

                            }).error(function (data, status) {
                                $error.handleError(data, status);
                            });




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

        $scope.newConversation = function (selectedUser, firstname, lastname) {
            $rootScope.$broadcast('goTo', "/messages/" + selectedUser + "/" + firstname + "/" + lastname);


        };



        $scope.loadInterests = function ($query) {
            return $scope.interests.filter(function (interest) {
                return interest.text.toLowerCase().indexOf($query.toLowerCase()) != -1;
            });

        }

        $scope.loadInterestOnInit = function () {
            $interests.getAll().success(function (data) {
                $scope.interests = data;

            }).error(function (data, status) {
                $error.handleError(data, status);
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


            $users.search(lon, lat, tags, lookingFors, categories,
                    $scope.currentRange, $scope.currrentOffset, $scope.fhellowPerPage).success(function (fhellows) {
                if (typeof fhellows !== 'undefined' && fhellows.length > 0) {

                    var nicknames = [];
                    for (var i = 0; i < fhellows.length; i++) {
                        nicknames.push(fhellows[i].nickname);
                    }
                    $scope.fhellowsList = $scope.fhellowsList.concat(fhellows);
//                    $presence.getUsersState(nicknames).success(function (states) {
//                        for (var i = 0; i < fhellows.length; i++) {
//                            fhellows[i].onlineStatus = states[i];
//                        }
//                        $scope.fhellowsList = $scope.fhellowsList.concat(fhellows);
//                    }).error(function (data, status) {
//                        $error.handleError(data, status);
//                    });
                } else {

                    $scope.noMoreResults = true;
                }
                if ($scope.fhellowsList.length < ($scope.fhellowPerPage * $scope.currentPage)) {

                    $scope.noMoreResults = true;

                }
            }).error(function (data, status) {
                $error.handleError(data, status);
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

            $users.search(0.0, 0.0, tags, lookingFors, categories, $scope.currentRange,
                    $scope.currrentOffset, $scope.fhellowPerPage)
                    .success(function (fhellows) {

                        if (typeof fhellows !== 'undefined' && fhellows.length > 0) {
                            var nicknames = [];
                            for (var i = 0; i < fhellows.length; i++) {
                                nicknames.push(fhellows[i].nickname);
                            }

                             $scope.fhellowsList = $scope.fhellowsList.concat(fhellows);
//                            $presence.getUsersState(nicknames).success(function (states) {
//
//                                for (var i = 0; i < fhellows.length; i++) {
//                                    fhellows[i].onlineStatus = states[i];
//                                }
//                                $scope.fhellowsList = $scope.fhellowsList.concat(fhellows);
//                            }).error(function (data, status) {
//                                $error.handleError(data, status);
//                            });
                        } else {

                            $scope.noMoreResults = true;
                        }
                        if ($scope.fhellowsList.length < ($scope.fhellowPerPage * $scope.currentPage)) {

                            $scope.noMoreResults = true;

                        }
                    })
                    .error(function (data, status) {
                        $error.handleError(data, status);
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

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users', '$interests',
        'location', 'appConstants', 'reverseGeocoder', '$location', '$presence', '$trace', '$error'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


