'use strict';

/**
 * @ngdoc overview
 * @name codebaseFrontendApp
 * @description
 * # codebaseFrontendApp
 *
 * Main module of the application.
 */
angular
    .module('codebaseFrontendApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngHello',
    'ui.router',
    'user.factory',
    'rest.factory',
    'constants.factory',
    'user.controller',
    'about.controller'
  ])
    .config(function ($stateProvider, $urlRouterProvider, helloProvider) {
        helloProvider.init({
            facebook: '163194807355490'
        });

        $urlRouterProvider.otherwise('/');
        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'userController'
            })
            .state('about', {
                url: '/about',
                templateUrl: 'views/about.html',
                controller: 'aboutController'
            });
    }).run(function () {});