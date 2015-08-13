(function () {
    var $interests = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        
        //GET ALL
        factory.getAll = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/interest/query/all',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                
            });
        };

        return factory;
    };

    $interests.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$interests", $interests);

}());