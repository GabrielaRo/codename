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

        $rootScope.websocket.onmessage = function (evt) {

//            var msg = JSON.parse(evt.data);
//            console.log(evt);
//            console.log(msg);
//            switch (msg.type) {
//                case 'message':
//
//                    $presence.newNotifications = $presence.newNotifications + 1;
//                    $presence.notifications.push({date: Date.now(), message: 'text: ' + msg.text});
//
//                    if ($scope.selectedConversation.other_nickname == msg.from) {
//
//                        $scope.$apply(function () {
//                            $scope.messageHistory.push({owner_nickname: msg.from, description: msg.description, text: msg.text, time: Date.now()});
//                        });
//                        var scrollDown = function () {
//                            var newListHeight = $(".messages-history").height();
//                            $("#user-messages-chat").scrollTop(newListHeight);
////                            $scope.$apply(new function () {
////                                $presence.clearNewNotifications();
////                            });
//                        };
//                        setTimeout(scrollDown, 200);
//                    }
//                    for (var i = 0; i < $scope.inbox.length; i++) {
//                        if ($scope.inbox[i].other_nickname == msg.from) {
//                            $scope.inbox[i].excerpt = msg.text;
//                            $scope.inbox[i].time = Date.now();
//                            $scope.inbox[i].newMessage = true;
//                            $scope.inbox[i].from = msg.from;
//                            $scope.inbox[i].to = msg.to;
//                            $scope.inbox[i].onlineStatus = true;
//                        }
//                    }
////                    $scope.$apply(function () {
////                        $filter('orderBy')($scope.inbox, 'conversation.time', true);
////                    });
//
//                    break;
//                case 'online':
//
//                    for (var i = 0; i < $scope.inbox.length; i++) {
//                        if ($scope.inbox[i].other_nickname == msg.from) {
//                            $scope.inbox[i].onlineStatus = true;
//                        }
//                    }
//                    break;
//                case 'offline':
//                    for (var i = 0; i < $scope.inbox.length; i++) {
//                        if ($scope.inbox[i].other_nickname == msg.from) {
//                            $scope.inbox[i].onlineStatus = false;
//                        }
//                    }
//                    break;
//
//            }
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

//            $scope.messageHistory.push({owner_nickname: $cookieStore.get("user_nick"), description: $cookieStore.get("user_full"), text: message, time: Date.now()});
//            for (var i = 0; i < $scope.inbox.length; i++) {
//                if ($scope.inbox[i].other_nickname == toUser) {
//                    $scope.inbox[i].excerpt = message;
//                    $scope.inbox[i].time = Date.now();
//                    $scope.inbox[i].from = $scope.me;
//                }
//
//            }
//            $filter('orderBy')($scope.inbox, 'conversation.time', true);
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

        $scope.getMessages = function (selectedConversation) {


            $chat.getMessages(selectedConversation).success(function (data) {
                console.log('Messages >>>>>> ');
                console.log(data);
                $scope.messageHistory = data;
                //$filter('orderBy')($scope.messageHistory, 'position', false);
                var scrollDown = function () {
                    var newListHeight = $(".messages-history").height();
                    $("#user-messages-chat").scrollTop(newListHeight);

                };
                setTimeout(scrollDown, 200);


            }).error(function (data, status) {
                $error.handleError(data, status);
            });
        };



        $scope.getConversations = function () {
            $scope.inbox = [];
            $chat.getConversations().success(function (data) {
                
                for(var i = 0; i < data.length; i++){
                    console.log(JSON.parse(data[i].metadata.participantsName));
                    data[i].metadata.participantsName = JSON.parse(data[i].metadata.participantsName);
                }
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
//                    var selected = false;
//                    for (var i = 0; i < $scope.inbox.length; i++) {
//                        if ($scope.inbox[i].other_nickname == $routeParams.selectedUser) {
//                            $scope.selectConversation($scope.inbox[i]);
//                            selected = true;
//                        }
//                    }
//                    if (!selected) {
//                        $scope.inbox.push({other_nickname: $routeParams.selectedUser, description: $routeParams.firstname + " " + $routeParams.lastname,
//                            excerpt: '...', time: Date.now(), onlineStatus: $routeParams.status});
//                        $scope.selectConversation($scope.inbox[$scope.inbox.length - 1]);
//                    }

                } else if ($scope.inbox[0] && !$routeParams.selectedUser) {
                    //$scope.selectedConversationOtherNickname = $scope.inbox[0].other_nickname;
                    //$scope.getMessages($scope.selectedConversationOtherNickname);
                    //$scope.selectedUserName = $scope.inbox[0].description;
                    $scope.selectedConversation = $scope.inbox[0].url;
                    $scope.getMessages($scope.selectedConversation);
                }



            }).error(function (data, status) {
                console.log("Error: ");
                console.log(data);
                console.log(status);
            });
//            $chat.getConversations().success(function (data) {
//                $scope.inbox = data;
//
//                if ($routeParams.selectedUser) {
//                    var selected = false;
//                    for (var i = 0; i < $scope.inbox.length; i++) {
//                        if ($scope.inbox[i].other_nickname == $routeParams.selectedUser) {
//                            $scope.selectConversation($scope.inbox[i]);
//                            selected = true;
//                        }
//                    }
//                    if (!selected) {
//                        $scope.inbox.push({other_nickname: $routeParams.selectedUser, description: $routeParams.firstname + " " + $routeParams.lastname,
//                            excerpt: '...', time: Date.now(), onlineStatus: $routeParams.status});
//                        $scope.selectConversation($scope.inbox[$scope.inbox.length - 1]);
//                    }
//
//                } else if ($scope.inbox[0] && !$routeParams.selectedUser) {
//                    $scope.selectedConversationOtherNickname = $scope.inbox[0].other_nickname;
//                    $scope.getMessages($scope.selectedConversationOtherNickname);
//                    $scope.selectedUserName = $scope.inbox[0].description;
//                    $scope.selectedConversation = $scope.inbox[0];
//                }
//                $filter('orderBy')($scope.inbox, 'conversation.time', true);
//
//
//            }).error(function (data, status) {
//                $error.handleError(data, status);
//            });
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
