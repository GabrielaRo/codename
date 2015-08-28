(function () {
    var $presence = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};
        factory.newNotifications = 0;
        factory.notifications = [];

        factory.clearNewNotifications = function () {
            factory.newNotifications = 0;
            factory.notifications = [];
        };
        
        //Search Users
        factory.getUsersState = function (nicknames) {
            
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/presence?users='+JSON.stringify(nicknames),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                
            });
        };


        return factory;
    };

    $presence.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$presence", $presence);

}());