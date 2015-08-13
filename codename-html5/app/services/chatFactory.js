(function () {
    var $chat = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //send message
        factory.sendMessage = function (conversationId, message) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/'+conversationId+'/message',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {sender: $cookieStore.get('user_nick'),  message: message}
            });
        };


        // new  conversation
        factory.newConversation = function (selectedUser) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/create',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {initiator: $cookieStore.get('user_nick'), other: selectedUser}
            });
        };
        
        
        // block  conversation
        factory.blockConversation = function (conversationId) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/'+conversationId+'/block',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                
            });
        };
        // unblock  conversation
        factory.unblockConversation = function (conversationId) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/'+conversationId+'/unblock',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                
            });
        };

        //get message
        factory.getMessages = function (conversationId) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/' + conversationId + '/messages',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };

        //get conversations
        factory.getConversations = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/' + $cookieStore.get('user_nick'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };


        return factory;
    };

    $chat.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$chat", $chat);

}());