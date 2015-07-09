(function () {
    var localFhellowsController = function ($scope, $rootScope, $users,  appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.filters = {location: '', proximity: 200, type:"", search: ""};
        $scope.filtersType = {freelance:false, entrepenuers:false, digitalnomads:false};
        $scope.filtersLookingTo = {socialise:false, collaborate:false, mentor:false};

        $scope.serverUrlFull = appConstants.server + appConstants.context;
        
        
        $scope.tags = [ ];
        
         $scope.loadInterests = function($query) {
            return [
                        { text: 'design' },
                        { text: 'development' },
                        { text: 'other' }
                      ];
         }
        
       
        $scope.typeButtonPressed = function(buttonName){
            $scope.filtersType[buttonName] = !$scope.filtersType[buttonName];
        }
        $scope.lookingToButtonPressed = function(buttonName){
            $scope.filtersLookingTo[buttonName] = !$scope.filtersLookingTo[buttonName];
        }
        
        $scope.loadFhellows = function () {


            $users.loadAllLive().success(function (data) {
                //$rootScope.$broadcast("quickNotification", "Clubs loaded!");
                $scope.fhellowsList = data;
                console.log(  $scope.fhellowsList);
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong!" + data);
            });

        };




        $scope.loadFhellows();
        
        
        //FHELLOW FILTERS
        
        $scope.userPosition = function(position){
            console.log(position);
            $scope.filters.location = "Lat " + (Math.round(position.coords.latitude * 100) / 100) + " Long " +  (Math.round(position.coords.longitude * 100) / 100); 
        }
        
        $scope.getLocation = function(){
           
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition($scope.userPosition);
                
            } else { 
                alert( "Geolocation is not supported by this browser.");
            }
        }
        
       
        
       

        $( window ).scroll(function() {
        
            if ($(window).scrollTop() > 235){
                $( ".sticky" ).addClass("isSticky");
            }else {
                $( ".sticky" ).removeClass("isSticky");
            }
        });

    };

    localFhellowsController.$inject = ['$scope', '$rootScope', '$users',  'appConstants'];
    angular.module("codename").controller("localFhellowsController", localFhellowsController);

}());


