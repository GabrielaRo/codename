(function () {
    var $chat = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //send message
        factory.sendMessage = function (to, message) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/messages',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {userId: $cookieStore.get('user_id') , to: to, message: message}
            });
        };
        
        //get message
        factory.getMessages = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/messages',
                headers: {'Content-Type': 'application/x-www-form-urlencoded',service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        

       
        return factory;
    };

    $chat.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$chat", $chat);

}());