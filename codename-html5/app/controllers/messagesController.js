(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope, $presence, $filter, appConstants, $error) {
        $scope.serverUrlFull = appConstants.server + appConstants.context;

        $scope.inbox = [];
        $scope.messageHistory = [];
        $scope.selectedConversation = [];



        $scope.me = $cookieStore.get('user_nick');
        $scope.meFull = $cookieStore.get('user_full');
        $scope.emojiMessage = {};
        $scope.emojiMessage.replyToUser = function () {
            if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                $('#sendMessageButton').click();
            }
        };

        $scope.reConnectChat = function () {
            $rootScope.initChat();
            $rootScope.chatOnline = appConstants.chatOnline;
            $scope.getConversations();
        };



        $scope.selectConversation = function (conversation) {

            $scope.selectedConversation = conversation;

            $scope.getMessages(conversation.url);


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


        };
        $scope.getOneConversation = function (conversationUrl) {
            $chat.getOneConversation(conversationUrl).success(function (data) {
                var idx = -1;
                for (var i = 0; i < $scope.inbox.length; i++) {
                    if ($scope.inbox[i].url == conversationUrl) {
                        idx = i;
                    }

                }
                if (idx != -1) {
                    console.log("index for conversation: " + idx)
                    $scope.inbox.splice(idx, 1);
                }
                data.metadata.participantsName = JSON.parse(data.metadata.participantsName);
                $scope.inbox.push(data);

                var orderBy = $filter('orderBy');

                $scope.inbox = orderBy($scope.inbox, 'last_message.sent_at', true);
                console.log("after sorting: ");
                console.log($scope.inbox);
            }).error(function (data, status) {
                $error.handleError(data, status);

            });

        };

        $scope.deleteConversation = function (conversationUrl) {
            $chat.deleteConversation(conversationUrl).success(function (data) {
                var idx = -1;
                for (var i = 0; i < $scope.inbox.length; i++) {
                    if ($scope.inbox[i].url == conversationUrl) {
                        idx = i;
                    }

                }
                if (idx != -1) {
                    console.log("index for conversation: " + idx)
                    $scope.inbox.splice(idx, 1);
                }
                if ($scope.inbox[0] && !$routeParams.selectedUser) {
                    $scope.selectedConversation = $scope.inbox[0].url;
                    $scope.getMessages($scope.selectedConversation);
                }
                if ($scope.inbox.length == 0) {
                    $scope.selectedConversation = '';
                }
            }).error(function (data, status) {
                $error.handleError(data, status);

            });
        };

        $scope.sendMessage = function (conversationUrl, body, mimeType) {

            $chat.sendMessage(conversationUrl, body, mimeType).success(function (data) {

                $scope.messageHistory.push(data);
                $scope.emojiMessage = {};
                $scope.emojiMessage.replyToUser = function () {
                    if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                        $('#sendMessageButton').click();
                    }
                };
                var newListHeight = $(".messages-history").height();
                $("#user-messages-chat").animate({scrollTop: newListHeight}, 200);
                $scope.getOneConversation(conversationUrl);

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

        $scope.markAsRead = function (messageUrl) {

            $chat.markAsReadMessage(messageUrl).success(function (data) {
                console.log('Makr as read >>>>>> ');
                console.log(data);
                $rootScope.newNotifications = $rootScope.newNotifications - 1;


            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };

        $scope.getMessages = function (selectedConversation) {


            $chat.getMessages(selectedConversation).success(function (data) {
                $scope.messageHistory = data;
                console.log(data);
                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);

                };
                setTimeout(scrollDown, 200);
                for (var i = 0; i < data.length; i++) {

                    if (data[i].is_unread) {
                        $scope.markAsRead(data[i].url);
                        console.log("marking as read  message: " + data[i].id);
                    }



                }

            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };



        $scope.getConversations = function () {
            $scope.inbox = [];
            $chat.getConversations().success(function (data) {
                console.log(data);
                for (var i = 0; i < data.length; i++) {

                    data[i].metadata.participantsName = JSON.parse(data[i].metadata.participantsName);
                }
                var unread = 0;
                for (var i = 0; i < data.length; i++) {
                    unread += data[i].unread_message_count;
                }
                $rootScope.newNotifications = unread;
                $scope.inbox = data;

                if ($routeParams.selectedUser) {
                    $chat.newConversation([$cookieStore.get('user_nick'), $routeParams.selectedUser],
                            [$cookieStore.get('user_full'), $routeParams.firstname + " " + $routeParams.lastname]
                            ).success(function (data) {
                        console.log(data);
                        data.metadata.participantsName = JSON.parse(data.metadata.participantsName);
                        $scope.inbox.push(data);
                        $scope.selectConversation(data);

                    }).error(function (data, status) {
                        console.log("Error: ");
                        console.log(data);
                        console.log(status);
                    });

                } else if ($scope.inbox[0] && !$routeParams.selectedUser) {

                    $scope.selectedConversation = $scope.inbox[0];
                    $scope.getMessages($scope.selectedConversation.url);
                }



            }).error(function (data, status) {
                console.log("Error: ");
                console.log(data);
                console.log(status);
            });

        };

        $scope.$watch('chatOnline',
                function (newValue) {
                    if (newValue == false) {
                        console.log("reconnecting chat.. becuase it is offline");
                        $rootScope.initChat();
                        $scope.chatOnline = appConstants.chatOnline;
                    } else {
                        console.log("getting conversations because the chat is online");
                        $scope.getConversations();

                    }
                });





    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore', '$routeParams', '$rootScope',
        '$presence', '$filter', 'appConstants', '$error'];
    angular.module("codename").controller("messagesController", messagesController);


}());
