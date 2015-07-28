(function () {
    var $chat = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //send message
        factory.sendMessage = function (conversationId, message) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/messages',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {userId: $cookieStore.get('user_id'), conversationId: conversationId, message: message}
            });
        };
        
        
        // new  conversation
        factory.newConversation = function (selectedUser) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/'+$cookieStore.get('user_id')+'/create',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {other: selectedUser}
            });
        };
        
        //get message
        factory.getMessages = function (conversationId) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/messages/'+$cookieStore.get('user_id')+"?conversationId="+conversationId,
                headers: {'Content-Type': 'application/x-www-form-urlencoded',service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                
            });
        };
        
        //get conversations
        factory.getConversations = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/conversations/'+$cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded',service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                
            });
        };
        
        //get message
        factory.getConnections = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/chat/connections/'+$cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded',service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                
                
            });
        };

       
        return factory;
    };

    $chat.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$chat", $chat);

}());