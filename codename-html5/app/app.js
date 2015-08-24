(function () {

    var codename = angular.module('codename', ['ngCookies', 'ngRoute',
        'ngAnimate', 'angular.filter', 'ngFileUpload',
        'satellizer', 'ngTagsInput', 'locator', 'angular-growl']);

    codename.constant("appConstants", {
        server: 'http://localhost:8080/',
        address: "localhost",
        port: "8080",
        context: 'codename-server/'

    })
            // configure our routes
            .config(function ($routeProvider, $authProvider) {
                $routeProvider
                        .when('/removeadmin', {
                            templateUrl: 'views/home.html',
                            controller: 'homeController',
//                            access: {
//                                requiresAdmin: true
//                            }
                        })
                
//                        .when('/invitelogin', {
//                            templateUrl: 'app/views/login.html',
//                            controller: 'loginController'
//                        })

                        .when('/localfhellows', {
                            templateUrl: 'views/localfhellows.html',
                            controller: 'localFhellowsController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                        .when('/password', {
                            templateUrl: 'views/password.html',
                            controller: 'passwordController'

                        })
                        .when('/profile', {
                            templateUrl: 'views/profile.html',
                            controller: 'profileController',
                            access: {
                                requiresLogin: true
                            }
                        })
                        .when('/p/:nickname', {
                            templateUrl: 'views/publicprofile.html',
                            controller: 'publicProfileController',
                            access: {
                                requiresLogin: true
                            }
                        })

                        .when('/messages', {
                            templateUrl: 'views/messages.html',
                            controller: 'messagesController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                        .when('/messages/:selectedConversation', {
                            templateUrl: 'views/messages.html',
                            controller: 'messagesController',
                            access: {
                                requiresLogin: true
                            }
                        })
                
                
                        .when('/contact', {
                            templateUrl: 'views/contact.html',
                            controller: 'contactController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                
                        .when('/feedback', {
                            templateUrl: 'views/feedback.html',
                            controller: 'feedbackController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                
                        .when('/terms_and_conditions', {
                            templateUrl: 'views/terms_and_conditions.html',
                            controller: 'termsController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                
                        .when('/privacy', {
                            templateUrl: 'views/privacy.html',
                            controller: 'privacyController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })
                
                        .when('/report', {
                            templateUrl: 'views/report.html',
                            controller: 'reportController',
                            access: {
                                requiresLogin: true,
                                requiresProfile: true
                            }
                        })

                        .when('/', {
                            templateUrl: 'views/invite.html',
                            controller: 'inviteController'
                        })
                        .otherwise({
                            redirectTo: '/'
                        });
//                $authProvider.google({
//                    url: '/' + "codename-server/" + '/rest/auth/google',
//                    redirectUri: window.location.protocol + '//' + window.location.host + '/' + "codename-server/",
//                    clientId: '475121985833-g2ludjvano3pbgbt98nvt04h7ojmvpjv.apps.googleusercontent.com'
//                });
            });


    //HISTORY 
    codename.run(function ($rootScope, $location, $routeParams, $auth) {

        var history = [];
        $rootScope.navStatus = "show";
        $rootScope.$on('$routeChangeStart', function (event, next) {
            var authorised;
            
            if (next.access !== undefined) {
                authorised = $auth.authorize(next.access.requiresLogin, 
                        next.access.requiresProfile,
                        next.access.permissions,
                        next.access.permissionCheckType);
                if (authorised === 'NotAuthorized') {
                    $location.path('/').search({email:'Enter your email'}).replace();
                } else if (authorised === 'RequiresProfile') {
                    $location.path('/profile').replace();
                } else if(authorised === 'Home') {
                    $location.path('/').replace();
                }
            }
        });

        $rootScope.$on('$routeChangeSuccess', function () {
            if ($location.path() == "/invite" || $location.path() == "/") {
                $rootScope.footerStatus = "hiddenFooter";
            } else {
                $rootScope.footerStatus = "show";
            }
            history.push($location.$$path);
        });
//        $rootScope.back = function () {
//            var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
//
//            $location.path(prevUrl);
//        };
    });

    //END HISTORY

    codename.config(['growlProvider', function (growlProvider) {
            growlProvider.globalPosition('top-center');
            growlProvider.globalDisableIcons(true);
            growlProvider.globalTimeToLive(3000);
            growlProvider.globalDisableCountDown(true);
        }]);

}());

