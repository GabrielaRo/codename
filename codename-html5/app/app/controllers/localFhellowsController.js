(function () {
    var localFhellowsController = function ($scope, $rootScope, $users,  appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.filters = {location: '', proximity: 200, type:"", search: ""};

        $scope.serverUrlFull = appConstants.server + appConstants.context;

       

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
            console.log("SCROLL " + $(window).scrollTop() );
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


