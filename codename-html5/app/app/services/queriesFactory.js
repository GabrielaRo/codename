(function () {
    var $queries = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //GET ALL
        factory.getByLocation = function (lon,lat, interests, lookingFor, categories) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/query/all',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {lon: lon, lat: lat, interests: JSON.stringify(interests), 
                    lookingFor: JSON.stringify(lookingFor), categories: JSON.stringify(categories)}
            });
        };



        return factory;
    };

    $queries.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$queries", $queries);

}());