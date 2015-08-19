(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope, $sockets, $notifications, $filter, appConstants) {


        $scope.serverUrlFull = appConstants.server + appConstants.context;

        $scope.inbox = [];
        $scope.messageHistory = [];
        $scope.selectedConversation = [];

        $scope.me = $cookieStore.get('user_nick');

        $rootScope.websocket.onmessage = function (evt) {

            var msg = JSON.parse(evt.data);

            $rootScope.$broadcast('quickNotification', msg, "success");
            switch (msg.type) {
                case 'message':

                    $notifications.newNotifications = $notifications.newNotifications + 1;
                    $notifications.notifications.push({date: Date.now(), message: 'text: ' + msg.text});
                    if ($scope.selectedConversation.conversation_id == msg.conversationId) {
                        $scope.messageHistory.push({owner_nickname: msg.from, text: msg.text, time: Date.now()});
                    }
                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].conversation_id == msg.conversationId) {
                            $scope.inbox[i].excerpt = msg.text;
                            $scope.inbox[i].time = Date.now();
                            $scope.inbox[i].newMessage = true;
                            $scope.inbox[i].from = msg.from;
                            $scope.inbox[i].to = msg.to;
                            $scope.inbox[i].onlineStatus = true;
                        }
                    }
                    $filter('orderBy')($scope.inbox, 'conversation.time', true);

                    break;
                case 'online':

                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].conversation_id == msg.conversationId) {
                            $scope.inbox[i].onlineStatus = true;
                        }
                    }
                    break;
                case 'offline':
                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].conversation_id == msg.conversationId) {
                            $scope.inbox[i].onlineStatus = false;
                        }
                    }
                    break;

            }
        };

        $scope.selectConversation = function (conversation) {

            $scope.selectedConversation = conversation;

            $scope.getMessages(conversation.conversation_id);

        }

        $scope.blockConversation = function (conversationId) {

            $chat.blockConversation(conversationId).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Conversation Blocked !" + data);
                for (var i = 0; i < $scope.inbox.length; i++) {
                    if ($scope.inbox[i].conversation_id == conversationId) {
                        $scope.inbox[i].blocked = true;

                    }

                }

            }).error(function (data) {

                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with blocking the conversation !" + data);

            });


        }
        $scope.unblockConversation = function (conversationId) {

            $chat.unblockConversation(conversationId).success(function (data) {


                $rootScope.$broadcast("quickNotification", "Conversation UnBlocked !" + data);
                for (var i = 0; i < $scope.inbox.length; i++) {
                    if ($scope.inbox[i].conversation_id == conversationId) {
                        $scope.inbox[i].blocked = false;

                    }

                }

            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with unblocking the conversation !" + data);

            });


        }


        $scope.sendMessage = function (conversationId, message) {

            $scope.messageHistory.push({owner_nickname: $cookieStore.get("user_nick"), text: message, time: Date.now()});
            for (var i = 0; i < $scope.inbox.length; i++) {
                if ($scope.inbox[i].conversation_id == conversationId) {
                    $scope.inbox[i].excerpt = message;
                    $scope.inbox[i].time = Date.now();
                    $scope.inbox[i].from = $scope.me;
                }

            }
            $chat.sendMessage(conversationId, message).success(function (data) {

                $scope.newMessage = "";
                var newListHeight = $(".messages-history").height();
                $("#user-messages-chat").animate({scrollTop: newListHeight}, 1000);

            }).error(function (data) {

                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with sending the message!" + data);
                $scope.newMessage = "";
            });


        }

        $scope.getMessages = function (selectedConversationId) {

            $chat.getMessages(selectedConversationId).success(function (data) {
                $scope.messageHistory = data;

                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);
                };
                setTimeout(scrollDown, 200);


            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all messages!" + data);
            });
        }



        $scope.getConversations = function () {

            $chat.getConversations().success(function (data) {
                $scope.inbox = data;

                if ($routeParams.selectedConversation) {

                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].conversation_id == $routeParams.selectedConversation) {
                            $scope.selectConversation($scope.inbox[i]);
                        }
                    }


                } else if ($scope.inbox[0] && !$routeParams.selectedConversation) {
                    $scope.selectedConversationId = $scope.inbox[0].conversation_id;
                    $scope.getMessages($scope.selectedConversationId);
                    $scope.selectedUserName = $scope.inbox[0].description;
                    $scope.selectedConversation = $scope.inbox[0];
                }



            }).error(function (data) {

                console.log("Error: " + data);

                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all conversations!" + data);
            });
        }

        $scope.getConversations();

        $(document).ready(function () {
            $('#sendMessageTextArea').keypress(function (e) {
                if (e.keyCode == 13) {
                    if ($scope.newMessage != "" && $scope.newMessage != undefined) {
                        $('#sendMessageButton').click();
                    }
                }
            });
        });


    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore', '$routeParams', '$rootScope', '$sockets', '$notifications', '$filter', 'appConstants'];
    angular.module("codename").controller("messagesController", messagesController);


}());