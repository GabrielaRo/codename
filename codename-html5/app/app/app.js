(function () {

    var codename = angular.module('codename', ['ngCookies', 'ngTagsInput', 'ngRoute',
        'ngAnimate', 'angular.filter', 'angularFileUpload', 'ui-rangeSlider',
        'masonry', 'angular-growl', 'satellizer']);

    codename.constant("appConstants", {
        server: "http://localhost:8080/",
        context: "codename-server/"
    })
            // configure our routes
            .config(function ($routeProvider, $authProvider) {
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
                        .when('/profile/:id', {
                            templateUrl: 'app/views/profile.html',
                            controller: 'profileController'
                        })

                        .otherwise({
                            redirectto: '/'
                        });
                $authProvider.google({
                    url: '/codename-server/rest/auth/google',
                    redirectUri: window.location.protocol + '//' + window.location.host + '/codename-server/',
                    clientId: '475121985833-g2ludjvano3pbgbt98nvt04h7ojmvpjv.apps.googleusercontent.com'
                });
            });


    //HISTORY 
    codename.run(function ($rootScope, $location) {

        var history = [];
        $rootScope.navStatus = "show";

        $rootScope.$on('$routeChangeSuccess', function () {
            console.log("LA RUTA ACTUAL ES = " + $location.path())
            if ($location.path() == "/") {
                $rootScope.navStatus = "hidden";
            } else {
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














