(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope) {



        $scope.selectedConversationId;
        $scope.selectedUserName;
        $scope.inbox = [];
        $scope.messageHistory = [];
        $scope.selectedConversation = [];
        $scope.selectedBlocked = false;

        $scope.selectConversation = function (conversation) {
            
            $scope.selectedConversationId = conversation.conversation_id;
            $scope.selectedConversation = conversation;
            $scope.selectedBlocked = conversation.blocked;
            $scope.getMessages($scope.selectedConversationId);
            if (conversation) {
                $scope.selectedUserName = conversation.description;
            } else {
                $scope.selectedUserName = "Select a Conversation ...";
            }
        }
        
        $scope.blockConversation = function (conversationId) {
            $chat.blockConversation(conversationId).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Conversation Blocked !" + data);
                $scope.selectedBlocked = true;
                
            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with blocking the conversation !" + data);
                
            });


        }
        $scope.unblockConversation = function (conversationId) {
            $chat.unblockConversation(conversationId).success(function (data) {
              
                 
                $rootScope.$broadcast("quickNotification", "Conversation UnBlocked !" + data );
                $scope.selectedBlocked = false;
                for(var i = 0; i < $scope.inbox.length; i++){
                    if($scope.inbox[i].conversation_id == $scope.selectedConversationId){
                        console.log("RESELECCIONA ESTO "+ i);
                        $scope.selectConversation($scope.inbox[i]);
                    }
                }
                 
                
            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with unblocking the conversation !" + data);
                
            });


        }


        $scope.sendMessage = function (conversationId, message) {
            
            $scope.messageHistory.push({owner_nickname: $cookieStore.get("user_nick"), text: message, time:  Date.now()});
            for(var i = 0; i < $scope.inbox.length; i++){
                if($scope.inbox[i].conversation_id == conversationId){
                    $scope.inbox[i].excerpt = message;
                    $scope.inbox[i].time = Date.now();
                }
                
            }
            $chat.sendMessage(conversationId, message).success(function (data) {

                $scope.newMessage = "";
                var newListHeight = $(".messages-history").height();
                $("#user-messages-chat").animate({scrollTop: newListHeight}, 1000);

            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with sending the message!" + data);
                $scope.newMessage = "";
            });


        }

        $scope.getMessages = function (selectedConversationId) {

            $chat.getMessages(selectedConversationId).success(function (data) {
                $scope.messageHistory = data;
                
           
                console.log("OK Data: ");
                console.log(data);

                $rootScope.$broadcast("quickNotification", "messages retrieved!");

                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);
                };
                setTimeout(scrollDown, 200);


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all messages!" + data);
            });
        }



        $scope.getConversations = function () {

            $chat.getConversations().success(function (data) {
                $scope.inbox = data;
                console.log("Routes Param selectedConversation: " + $routeParams.selectedConversation);
                if ($routeParams.selectedConversation) {
                    
                    for(var i = 0; i < $scope.inbox.length; i++){
                        if($scope.inbox[i].conversation_id == $routeParams.selectedConversation){
                            $scope.selectConversation($scope.inbox[i]);
                        }
                    }


                } else if ($scope.inbox[0] && !$routeParams.selectedConversation) {
                    $scope.selectedConversationId = $scope.inbox[0].conversation_id;
                    $scope.getMessages($scope.selectedConversationId);
                    $scope.selectedUserName = $scope.inbox[0].description;
                    $scope.selectedConversation = $scope.inbox[0];
                }
//                mySocket.emit('rooms:join', {roomId: $scope.selectedConversationId}, function (data) {
//                    console.log("ws data: ");
//                    console.log(data);
//                });
                console.log("OK Inbox Data: ");
                console.log(data);

                $rootScope.$broadcast("quickNotification", "messages retrieved!");


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all conversations!" + data);
            });
        }

        $scope.getConversations();


    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore', '$routeParams', '$rootScope'];
    angular.module("codename").controller("messagesController", messagesController);


}());
