(function () {

    var codename = angular.module('codename', ['textAngular', 'ngCookies', 'ngRoute',
        'ngAnimate', 'angular.filter', 'ngFileUpload',
        'satellizer', 'ngTagsInput', 'locator', 'angular-growl', 'ngSanitize', 'emojiApp']);

    codename.constant("appConstants", {
        server: 'http://localhost:8080/',
        address: "localhost",
        port: "8080",
        context: 'codename-server/',
        chatServer: 'https://api.layer.com',
        chatHeaders: {
            Accept: 'application/vnd.layer+json; version=1.0',
            Authorization: '',
            'Content-type': 'application/json'
        },
        chatAppId: 'a094788a-52e8-11e5-ae20-faf631006ce3'

    })
            // configure our routes
            .config(function ($routeProvider, $authProvider) {
                $routeProvider
                        .when('/admin', {
                            templateUrl: 'views/admin.html',
                            controller: 'adminController',
                            access: {
                                requiresLogin: true,
                                requiresAdmin: true
                            }
                        })


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
                        .when('/settings', {
                            templateUrl: 'views/settings.html',
                            controller: 'settingsController',
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
                        .when('/messages/:selectedUser/:firstname/:lastname/:onlineStatus', {
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
                        next.access.requiresAdmin,
                        next.access.permissions,
                        next.access.permissionCheckType);
                if (authorised === 'NotAuthorized') {
                    $location.path('/').search({email: 'Enter your email'}).replace();
                } else if (authorised === 'RequiresProfile') {
                    $location.path('/profile').replace();
                } else if (authorised == 'AdminNotAuthorized') {
                    $location.path('/profile').replace();
                } else if (authorised === 'Home') {
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

