(function () {
    var $chat = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};

        //get conversations
        factory.getNonce = function () {
            return $http({
                method: 'POST',
                url: appConstants.chatServer + '/nonces',
                headers: appConstants.chatHeaders
            });
        };

        //get identity token
        factory.getIdentityToken = function (nonce) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/identity',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {nonce: nonce, nickname: $cookieStore.get('user_nick')}
            });
        };

        //get conversations
        factory.getChatSession = function (identityToken) {
            return $http({
                method: 'POST',
                url: appConstants.chatServer + '/sessions',
                headers: appConstants.chatHeaders,
                data: JSON.stringify({
                    'identity_token': identityToken,
                    'app_id': appConstants.chatAppId
                })
            });
        };




        //new conversation
        factory.newConversation = function (participants, participantsName) {
            return $http({
                method: 'POST',
                url: appConstants.chatServer + '/conversations',
                headers: appConstants.chatHeaders,
                data: JSON.stringify({
                    participants: participants,
                    distinct: false,
                    metadata: {participantsName : JSON.stringify(participantsName)}})
            });
        };

        //get  conversations
        factory.getConversations = function () {
            return $http({
                method: 'GET',
                url: appConstants.chatServer + '/conversations',
                headers: appConstants.chatHeaders
            });

        };
        
        factory.getOneConversation = function (conversationUrl) {
            return $http({
                method: 'GET',
                url: conversationUrl,
                headers: appConstants.chatHeaders
            });

        };
        
        factory.deleteConversation = function (conversationUrl) {
            return $http({
                method: 'DELETE',
                url: conversationUrl+'?destroy=true',
                headers: appConstants.chatHeaders
            });

        };

        //get message
        factory.getMessages = function (conversationUrl) {
            return $http({
                method: 'GET',
                url: conversationUrl + '/messages',
                headers: appConstants.chatHeaders
            });
        };


        //send message
        factory.sendMessage = function (conversationUrl, body, mimeType) {
            return $http({
                method: 'POST',
                url: conversationUrl + '/messages',
                headers: appConstants.chatHeaders,
                data: JSON.stringify({
                    parts: [{
                            body: body,
                            mime_type: mimeType
                        }]
                })
            });
        };
       


        return factory;
    };

    $chat.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$chat", $chat);

}());