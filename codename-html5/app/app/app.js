(function() {
    
    var codename = angular.module('codename', [ 'ngCookies', 'ngTagsInput', 'ngRoute', 'ngAnimate', 'angular.filter', 'angularFileUpload', 'ui-rangeSlider','masonry','angular-growl']);
    
   codename.constant("appConstants", {
        server: "http://localhost:8080/",
        context: "codename-server/"
    })
    // configure our routes
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'app/views/home.html',
                controller: 'homeController'
            })

            .when('/localfhellows', {
                templateUrl: 'app/views/localfhellows.html',
                controller: 'localFhellowsController'
            })
            .when('/password', {
                templateUrl: 'app/views/password.html',
                controller: 'passwordController'

            })

           .when('/firstlogin', {
                templateUrl: 'app/views/profile.html',
                controller: 'profileController'
            })

            .when('/profile', {
                templateUrl: 'app/views/profile.html',
                controller: 'profileController'
            })
           
            .otherwise({
                redirectto: '/'
            });

    });
    
    
    
    //HISTORY 
    codename.run(function ($rootScope, $location) {

        var history = [];
        $rootScope.navStatus = "show";

        $rootScope.$on('$routeChangeSuccess', function () {
            console.log("LA RUTA ACTUAL ES = " +$location.path())
            if($location.path() == "/"){
                $rootScope.navStatus = "hidden";
            }else{
                $rootScope.navStatus = "show";
            }
            history.push($location.$$path);
        });

        $rootScope.back = function () {
            var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";

            $location.path(prevUrl);
        };
    });

    //END HISTORY

    
}());













    