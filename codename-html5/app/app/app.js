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
            .when('/signup', {
                templateUrl: 'app/views/signup.html',
                controller: 'signUpController'
            })
            .when('/localfhellows', {
                templateUrl: 'app/views/localfhellows.html',
                controller: 'localFhellowsController'
            })
            .when('/settings', {
                templateUrl: 'app/views/settings.html',
                controller: 'settingsController'
            })
            .when('/password', {
                templateUrl: 'app/views/password.html',
                controller: 'passwordController'

            })
            .when('/login', {
                templateUrl: 'app/views/login.html'
            })

            .when('/firstlogin', {
                templateUrl: 'app/views/userinterests.html',
                controller: 'userInterestsController'
            })

           
            .otherwise({
                redirectto: '/'
            });

    });
    
    
    
    //HISTORY 
    codename.run(function ($rootScope, $location) {

        var history = [];

        $rootScope.$on('$routeChangeSuccess', function () {
            history.push($location.$$path);
            console.log("HISTORY " + history)
        });

        $rootScope.back = function () {
            var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";

            $location.path(prevUrl);
        };
    });

    //END HISTORY

    
}());













    