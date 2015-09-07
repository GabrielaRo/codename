(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope, $presence, $filter, appConstants, $error) {
        $scope.serverUrlFull = appConstants.server + appConstants.context;

        $scope.inbox = [];
        $scope.messageHistory = [];
        $scope.selectedConversation = [];

        $scope.me = $cookieStore.get('user_nick');
        $scope.emojiMessage = {};
        $scope.emojiMessage.replyToUser = function () {
            if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                $('#sendMessageButton').click();
            }
        };

        $rootScope.websocket.onmessage = function (evt) {

            var msg = JSON.parse(evt.data);
            console.log(evt);
            console.log(msg);
            switch (msg.type) {
                case 'message':

                    $presence.newNotifications = $presence.newNotifications + 1;
                    $presence.notifications.push({date: Date.now(), message: 'text: ' + msg.text});

                    if ($scope.selectedConversation.other_nickname == msg.from) {

                        $scope.$apply(function () {
                            $scope.messageHistory.push({owner_nickname: msg.from, description: msg.description, text: msg.text, time: Date.now()});
                        });
                        var scrollDown = function () {
                            var newListHeight = $(".messages-history").height();
                            $("#user-messages-chat").scrollTop(newListHeight);
                            $scope.$apply(new function () {
                                $presence.clearNewNotifications();
                            });
                        };
                        setTimeout(scrollDown, 200);
                    }
                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].other_nickname == msg.from) {
                            $scope.inbox[i].excerpt = msg.text;
                            $scope.inbox[i].time = Date.now();
                            $scope.inbox[i].newMessage = true;
                            $scope.inbox[i].from = msg.from;
                            $scope.inbox[i].to = msg.to;
                            $scope.inbox[i].onlineStatus = true;
                        }
                    }
                    $scope.$apply(function () {
                        $filter('orderBy')($scope.inbox, 'conversation.time', true);
                    });

                    break;
                case 'online':

                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].other_nickname == msg.from) {
                            $scope.inbox[i].onlineStatus = true;
                        }
                    }
                    break;
                case 'offline':
                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].other_nickname == msg.from) {
                            $scope.inbox[i].onlineStatus = false;
                        }
                    }
                    break;

            }
        };

        $scope.selectConversation = function (conversation) {

            $scope.selectedConversation = conversation;

            $scope.getMessages(conversation.other_nickname);


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

            }).error(function (data, status) {
                $error.handleError(data, status);
            });


        }


        $scope.sendMessage = function (toUser, message) {

            $scope.messageHistory.push({owner_nickname: $cookieStore.get("user_nick"), description: $cookieStore.get("user_full"), text: message, time: Date.now()});
            for (var i = 0; i < $scope.inbox.length; i++) {
                if ($scope.inbox[i].other_nickname == toUser) {
                    $scope.inbox[i].excerpt = message;
                    $scope.inbox[i].time = Date.now();
                    $scope.inbox[i].from = $scope.me;
                }

            }
            $filter('orderBy')($scope.inbox, 'conversation.time', true);
            $chat.sendMessage(toUser, message).success(function (data) {

                $scope.emojiMessage = {};
                $scope.emojiMessage.replyToUser = function () {
                    if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                        $('#sendMessageButton').click();
                    }
                };
                var newListHeight = $(".messages-history").height();
                $("#user-messages-chat").animate({scrollTop: newListHeight}, 200);

            }).error(function (data, status) {
                $error.handleError(data, status);
                $scope.emojiMessage = {};
                $scope.emojiMessage.replyToUser = function () {
                    if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                        $('#sendMessageButton').click();
                    }
                };
            });




        }

        $scope.getMessages = function (other_nickname) {

            $chat.getMessages(other_nickname).success(function (data) {
                $scope.messageHistory = data;

                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);
                    $scope.$apply(new function () {
                        $presence.clearNewNotifications();
                    });
                };
                setTimeout(scrollDown, 200);


            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };



        $scope.getConversations = function () {
            $scope.inbox = [];
            $chat.getConversations().success(function (data) {
                $scope.inbox = data;

                if ($routeParams.selectedUser) {
                    var selected = false;
                    for (var i = 0; i < $scope.inbox.length; i++) {
                        if ($scope.inbox[i].other_nickname == $routeParams.selectedUser) {
                            $scope.selectConversation($scope.inbox[i]);
                            selected = true;
                        }
                    }
                    if (!selected) {
                        $scope.inbox.push({other_nickname: $routeParams.selectedUser, description: $routeParams.firstname + " " + $routeParams.lastname,
                            excerpt: '...', time: Date.now(), onlineStatus: $routeParams.status});
                        $scope.selectConversation($scope.inbox[$scope.inbox.length - 1]);
                    }

                } else if ($scope.inbox[0] && !$routeParams.selectedUser) {
                    $scope.selectedConversationOtherNickname = $scope.inbox[0].other_nickname;
                    $scope.getMessages($scope.selectedConversationOtherNickname);
                    $scope.selectedUserName = $scope.inbox[0].description;
                    $scope.selectedConversation = $scope.inbox[0];
                }
                $filter('orderBy')($scope.inbox, 'conversation.time', true);


            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        }

        $scope.getConversations();




    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore', '$routeParams', '$rootScope',
        '$presence', '$filter', 'appConstants', '$error'];
    angular.module("codename").controller("messagesController", messagesController);


}());
