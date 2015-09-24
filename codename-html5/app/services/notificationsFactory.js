(function () {
    var $presence = function ($http, $cookieStore, $rootScope, $transformRequestToForm, appConstants) {
        var factory = {};
        $rootScope.newNotifications = 0;
        $rootScope.notifications = [];

        factory.clearNewNotifications = function () {
            $rootScope.newNotifications = 0;
            $rootScope.notifications = [];
        };

        factory.getUsersState = function (users) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/presence?users='+JSON.stringify(users),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };

        factory.registerInterestInUsers = function (usersNicknames) {
            return $http({
                method: 'PUT',
                url: appConstants.server + appConstants.context + 'rest/presence',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {nickname: $cookieStore.get('user_nick'), usersNicknames: JSON.stringify(usersNicknames)}
            });
        };

        return factory;
    };

    $presence.$inject = ['$http', '$cookieStore', '$rootScope', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$presence", $presence);

}());