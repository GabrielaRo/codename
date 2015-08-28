(function () {
    var $chat = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //send message
        factory.sendMessage = function (toUser, message) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/message',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {toUser: toUser, sender: $cookieStore.get('user_nick'), message: message}
            });
        };

        //get message
        factory.getMessages = function (withUser) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/messages/?nickname=' + $cookieStore.get('user_nick') + '&withUser=' + withUser,
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };

        //get conversations
        factory.getConversations = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/inbox/?nickname=' + $cookieStore.get('user_nick'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };


        return factory;
    };

    $chat.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$chat", $chat);

}());