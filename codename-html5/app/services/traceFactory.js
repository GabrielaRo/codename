(function () {
    var $trace = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //share location
        factory.shareUserLocation = function ( lat, lon, desc) {
            console.log("lat: "+ lat);
            console.log("lon: "+ lon);
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/trace',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {userId: $cookieStore.get('user_id'), latitude: lat, longitude: lon, description: desc}
            });
        };

        //get shared locations
        factory.getSharedLocations = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/trace?userId=' + $cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };

       


        return factory;
    };

    $trace.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$trace", $trace);

}());