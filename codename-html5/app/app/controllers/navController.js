(function (){
    
    var NavigationController = function($scope, $location, $rootScope){
        
        $scope.newMessages = 3;
        
        $scope.isActive = function (route) {
            return route === $location.path();
        };

        $rootScope.$on('goTo', function (event, data) {
            $location.path(data);
        });
    };
    
    NavigationController.$inject = ['$scope','$location', '$rootScope'];
    angular.module( "codename" ).controller("NavigationController", NavigationController);
    
}());
