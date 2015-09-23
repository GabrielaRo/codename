(function () {
    var $presence = function ($http, $cookieStore, $rootScope) {
        var factory = {};
        $rootScope.newNotifications = 0;
        $rootScope.notifications = [];

        factory.clearNewNotifications = function () {
            $rootScope.newNotifications = 0;
            $rootScope.notifications = [];
        };
        
        


        return factory;
    };

    $presence.$inject = ['$http', '$cookieStore', '$rootScope'];
    angular.module("codename").factory("$presence", $presence);

}());