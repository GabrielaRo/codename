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
        $scope.fhellowPerPage = 20;
        $scope.currentPage = 0;
        $scope.currrentOffset = ($scope.currentPage * $scope.fhellowPerPage);
        $scope.currrentLimit = (($scope.currentPage + 1) * $scope.fhellowPerPage);

        location.get(angular.noop, angular.noop);

        $scope.showMore = function () {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.currrentOffset = ($scope.currentPage * $scope.fhellowPerPage);
            $scope.currrentLimit = (($scope.currentPage + 1) * $scope.fhellowPerPage);

            console.log("currentPage " + $scope.currentPage);
            console.log("currrentOffset " + $scope.currrentOffset);
            console.log("currrentLimit " + $scope.currrentLimit);
            if ($scope.lookedUpLocation) {
                $scope.loadFhellowsInRange($scope.selectedLocation.longitude, $scope.selectedLocation.latitude, $scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);
            } else {
                $scope.loadFhellows($scope.tagsText, $scope.filtersLookingTo, $scope.filtersType);

            }
        };
        $scope.resetPaging = function () {
            $scope.fhellowsList = [];
            $scope.currentPage = 0;
            $scope.currrentOffset = ($scope.currentPage * $scope.fhellowPerPage) + 1;
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
            $users.search(lon, lat, tags, lookingFors, categories, $scope.currrentOffset, $scope.currrentLimit).success(function (data) {
                if (typeof data !== 'undefined' && data.length > 0) {
                    console.log("data for all ranges : " + data.length);
                    console.log(data);
                    $scope.fhellowsList = $scope.fhellowsList.concat(data);
                } else {
                    console.log("no data for all ranges");
                }
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });
        }




        $scope.loadFhellows = function (tags, lookingFors, categories) {
            console.log("From : " + $scope.currrentOffset + "to " + $scope.currrentLimit);

            $users.search(0.0, 0.0, tags, lookingFors, categories, $scope.currrentOffset, $scope.currrentLimit).success(function (data) {
                console.log("data for the world : " + data.length);
                console.log(data);
                $scope.fhellowsList = $scope.fhellowsList.concat(data);
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

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users', '$interests', '$chat', 'location', 'appConstants'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


