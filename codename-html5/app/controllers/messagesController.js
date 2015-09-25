(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope, $presence, $filter, appConstants, $error) {
        $scope.serverUrlFull = appConstants.server + appConstants.context;


        $scope.messagesLoaded = false;
        $scope.conversationsLoaded = false;

        $scope.sendText = 'Send';

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

            $rootScope.selectedConversation = conversation;

            $scope.getMessages(conversation.url);


        }

        $scope.blockConversation = function (conversationId) {

            $chat.blockConversation(conversationId).success(function (data) {
                $rootScope.$broadcast("quickNotification", "Conversation Blocked !" + data);
                for (var i = 0; i < $scope.inbox.length; i++) {
                    if ($rootScope.inbox[i].conversation_id === conversationId) {
                        $rootScope.inbox[i].blocked = true;

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
                for (var i = 0; i < $rootScope.inbox.length; i++) {
                    if ($rootScope.inbox[i].conversation_id === conversationId) {
                        $rootScope.inbox[i].blocked = false;
                    }
                }

            }).error(function (data, status) {
                $error.handleError(data, status);
            });


        };
        $scope.getOneConversation = function (conversationUrl) {
            $chat.getOneConversation(conversationUrl).success(function (data) {
                var idx = -1;
                var onlineStatus = false;
                for (var i = 0; i < $rootScope.inbox.length; i++) {
                    if ($rootScope.inbox[i].url === conversationUrl) {
                        idx = i;
                        onlineStatus = $rootScope.inbox[i].onlineStatus;
                    }

                }
                if (idx !== -1) {
                    $rootScope.inbox.splice(idx, 1);
                }
                data.metadata.participantsName = JSON.parse(data.metadata.participantsName);

                data.onlineStatus = onlineStatus;

                $rootScope.inbox.push(data);

                var orderBy = $filter('orderBy');

                $rootScope.inbox = orderBy($rootScope.inbox, 'last_message.sent_at', true);

            }).error(function (data, status) {
                $error.handleError(data, status);

            });

        };

        $scope.deleteConversation = function (conversationUrl) {
            $chat.deleteConversation(conversationUrl).success(function (data) {
                var idx = -1;
                for (var i = 0; i < $rootScope.inbox.length; i++) {
                    if ($rootScope.inbox[i].url === conversationUrl) {
                        idx = i;
                    }

                }
                if (idx !== -1) {

                    $rootScope.inbox.splice(idx, 1);
                }
                if ($rootScope.inbox[0] && !$routeParams.selectedUser) {
                    $rootScope.selectedConversation = $rootScope.inbox[0].url;
                    $scope.getMessages($rootScope.selectedConversation);
                }
                if ($rootScope.inbox.length === 0) {
                    $rootScope.selectedConversation = '';
                }
            }).error(function (data, status) {
                $error.handleError(data, status);

            });
        };
        $scope.getLastSentMessageStatus = function (recipient_status) {
            var status;
            for (var prop in recipient_status) {
                if (recipient_status.hasOwnProperty(prop)) {

                    if (prop !== $scope.me) {
                        status = recipient_status[prop];

                    }
                }
            }

            return status;

        };


        $scope.sendMessage = function (conversationUrl, body, mimeType) {
            $scope.sendText = 'Sending...';
            $chat.sendMessage(conversationUrl, body, mimeType).success(function (data) {

                $rootScope.messageHistory.push(data);
                $scope.emojiMessage = {};
                $scope.emojiMessage.replyToUser = function () {
                    if ($scope.emojiMessage.messagetext != "" && $scope.emojiMessage.messagetext != undefined) {
                        $('#sendMessageButton').click();
                    }
                };
                var newListHeight = $(".messages-history").height();
                $("#user-messages-chat").animate({scrollTop: newListHeight}, 200);
                $scope.getOneConversation(conversationUrl);

                $scope.sendText = 'Send';

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

        $scope.markAsRead = function (message) {
            console.log("marking as read: ");
            console.log(message);
            $chat.markAsReadMessage(message.url).success(function (data) {

                $rootScope.newNotifications = $rootScope.newNotifications - 1;
                for (var i = 0; i < $rootScope.inbox.length; i++) {
                    if ($rootScope.inbox[i].id === message.conversation.id) {

                        $rootScope.inbox[i].unread_message_count = $rootScope.inbox[i].unread_message_count - 1;


                    }

                }
            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };

        $scope.getMessages = function (selectedConversation) {

            $scope.messagesLoaded = false;
            $chat.getMessages(selectedConversation).success(function (data) {
                $rootScope.messageHistory = data;

                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);

                };
                setTimeout(scrollDown, 200);
                for (var i = 0; i < data.length; i++) {

                    if (data[i].is_unread) {
                        $scope.markAsRead(data[i]);

                    }

                }
                $scope.messagesLoaded = true;
            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };



        $scope.getConversations = function () {
            $rootScope.inbox = [];
            $scope.conversationsLoaded = false;
            $chat.getConversations().success(function (data) {
                var usernicknames = [];
                for (var i = 0; i < data.length; i++) {
                    data[i].onlineStatus = false;
                    if (data[i].metadata.participantsName) {
                        data[i].metadata.participantsName = JSON.parse(data[i].metadata.participantsName);
                    }

                    for (var j = 0; j < data[i].participants.length; j++) {
                        if (data[i].participants[j] !== $cookieStore.get('user_nick')) {
                            usernicknames.push(data[i].participants[j]);
                        }
                    }
                }

                $presence.getUsersState(usernicknames).success(function (states) {
                    for (var i = 0; i < data.length; i++) {

                        data[i].onlineStatus = states[i];
                    }

                }).error(function (data, status) {
                    $error.handleError(data, status);
                });
                var unread = 0;
                for (var i = 0; i < data.length; i++) {
                    unread += data[i].unread_message_count;
                }
                $rootScope.newNotifications = unread;
                $rootScope.inbox = data;

                if ($routeParams.selectedUser) {


                    $chat.newConversation([$cookieStore.get('user_nick'), $routeParams.selectedUser],
                            [$cookieStore.get('user_full'), $routeParams.firstname + " " + $routeParams.lastname]
                            ).success(function (data) {

                        data.metadata.participantsName = JSON.parse(data.metadata.participantsName);
                        var idx = -1;
                        var onlineStatus = false;
                        for (var i = 0; i < $rootScope.inbox.length; i++) {
                            if ($rootScope.inbox[i].url === data.url) {
                                idx = i;
                                onlineStatus = $rootScope.inbox[i].onlineStatus;
                            }

                        }
                        if (idx !== -1) {
                            $rootScope.inbox.splice(idx, 1);
                        }
                        data.onlineStatus = onlineStatus;
                        $rootScope.inbox.push(data);
                        $scope.selectConversation(data);

                    }).error(function (data, status) {
                        console.log("Error: ");
                        console.log(data);
                        console.log(status);
                    });

                } else if ($rootScope.inbox[0] && !$routeParams.selectedUser) {

                    $rootScope.selectedConversation = $rootScope.inbox[0];
                    $scope.getMessages($rootScope.selectedConversation.url);
                }

                $scope.conversationsLoaded = true;

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
