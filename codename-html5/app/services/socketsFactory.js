(function () {
    var $sockets = function ($rootScope, $cookieStore, $chat, $filter, appConstants) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function () {

            var wsUri = "ws://" + appConstants.address + ":" + appConstants.port + "/" + appConstants.context
                    + "fhellow?nickname=" + $cookieStore.get('user_nick');

            if ($rootScope.chat_session_token) {
                $rootScope.presenceWebsocket = new RcSocket(wsUri);
                $rootScope.presenceWebsocket.debug = false;
                $rootScope.presenceWebsocket.timeout = 2500;
                $rootScope.presenceWebsocket.maxRetry = 1000;
                $rootScope.presenceWebsocket.logger = console.debug;

                $rootScope.presenceWebsocket.onconnecting = function (evt) {
                    console.log(">>> Presence WS on Connecting");
                };

                $rootScope.presenceWebsocket.onopen = function (evt) {
                    console.log(">>> Presence WS on Open");
                };
                $rootScope.presenceWebsocket.onmessage = function (evt) {
                    console.log(">> Presence On Message: ");
                    console.log(evt);
                    var msg = JSON.parse(evt.data);
                    try {
                        switch (msg.type) {
                            case "online":
                                for (var i = 0; i < $rootScope.inbox.length; i++) {
                                    for (var j = 0; j < $rootScope.inbox[i].participants.length; j++) {
                                        if ($rootScope.inbox[i].participants[j] === msg.user) {
                                            console.log("msg.user = " + msg.user + " is online!");
                                            $rootScope.$apply(function () {
                                                $rootScope.inbox[i].onlineStatus = true;
                                            });
                                        }
                                    }
                                }
                                break;
                            case "offline":
                                for (var i = 0; i < $rootScope.inbox.length; i++) {
                                    for (var j = 0; j < $rootScope.inbox[i].participants.length; j++) {
                                        if ($rootScope.inbox[i].participants[j] === msg.user) {
                                            console.log("msg.user = " + msg.user + " is offline!");
                                            $rootScope.$apply(function () {
                                                $rootScope.inbox[i].onlineStatus = false;
                                            });
                                        }
                                    }
                                }
                                break;

                        }
                    } catch (e) {
                        console.error("presence WS Error: " + e);
                    }


                };
                $rootScope.presenceWebsocket.onerror = function (evt) {
                    console.log("Presence Error: " + evt.data);
                };
                $rootScope.presenceWebsocket.onclose = function () {
                    console.log(">>> Presence WS on Close");
                };

                $rootScope.chatWebsocket = new RcSocket('wss://api.layer.com/websocket?session_token=' + $rootScope.chat_session_token, 'layer-1.0');
                $rootScope.chatWebsocket.debug = false;
                $rootScope.chatWebsocket.timeout = 2500;
                $rootScope.chatWebsocket.maxRetry = 1000;
                $rootScope.chatWebsocket.logger = console.debug;

                $rootScope.chatWebsocket.onconnecting = function (evt) {
                    console.log(">>> Chat WS on Connecting");
                };
                $rootScope.chatWebsocket.onopen = function (evt) {
                    console.log(">>> Chat WS on Open");
                };
                $rootScope.chatWebsocket.onmessage = function (evt) {
//                    console.log(">> Chat On Message: ");
//                    console.log(evt);
                    var msg = JSON.parse(evt.data);
                    try {
                        switch (msg.type + "." + msg.body.operation) {
                            case "change.create":
                                //console.log("WEBSOCKET CREATE: " + msg.body.object.id + " - by: " + msg.body.data.sender.user_id);
                                //console.log(msg.body.data);
                                console.log(msg);
                                // check that the notificatio is not from myself
                                if (msg.body.data.sender.user_id !== $cookieStore.get('user_nick')) {
                                    $rootScope.$apply(function () {
                                        $rootScope.newNotifications = $rootScope.newNotifications + 1;
                                        
                                    });
                                    var conversationUpdated = false;
                                    // If the ws notification is for the selected conversation
                                    if ($rootScope.selectedConversation.id === msg.body.data.conversation.id) {

                                        $rootScope.$apply(function () {
                                            $rootScope.messageHistory.push(msg.body.data);
                                        });
                                        // look for the conversation in the inbox
                                        for (var i = 0; i < $rootScope.inbox.length; i++) {
                                            if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
                                                // apply the new data to the inbox entry
                                                $rootScope.$apply(function () {
                                                    console.log($rootScope.inbox[i].last_message);
                                                    $rootScope.inbox[i].last_message.sender.user_id = msg.body.data.sender.user_id;
                                                    $rootScope.inbox[i].last_message.parts[0].body = msg.body.data.parts[0].body;
                                                    $rootScope.inbox[i].last_message.received_at = new Date();
                                                    //$rootScope.inbox[i].unread_message_count = $rootScope.inbox[i].unread_message_count + 1;
                                                    conversationUpdated = true;


                                                });
                                                var newListHeight = $(".messages-history").height();
                                                $("#user-messages-chat").animate({scrollTop: newListHeight}, 200);
                                            }

                                        }
                                    }
                                    // if the ws notification is not about the selected conversation
                                    else {

                                        // look for the inbox entry to update
                                        for (var i = 0; i < $rootScope.inbox.length; i++) {
                                            if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
//                                                $rootScope.$apply(function () {
//
//                                                    $rootScope.inbox[i].last_message.sender.user_id = msg.body.data.sender.user_id;
//                                                    $rootScope.inbox[i].last_message.parts[0].body = msg.body.data.parts[0].body;
//                                                    $rootScope.inbox[i].last_message.received_at = new Date();
//                                                    $rootScope.inbox[i].unread_message_count = $rootScope.inbox[i].unread_message_count + 1;
//                                                    conversationUpdated = true;
//                                                });
                                                
                                                $chat.getOneConversation(msg.body.data.conversation.url).success(function (data) {
                                                    var idx = -1;
                                                    var onlineStatus = false;
                                                    for (var i = 0; i < $rootScope.inbox.length; i++) {
                                                        if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
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
                                                    console.log("Error: " + data + " - " + status);

                                                });
                                            }

                                        }
                                    }
                                    //if the conversation didn't exist for this user yet -> add it
                                    if (conversationUpdated === false) {
                                        
                                        $chat.getOneConversation(msg.body.data.conversation.url).success(function (data) {
                                            var idx = -1;
                                            var onlineStatus = false;
                                            for (var i = 0; i < $rootScope.inbox.length; i++) {
                                                if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
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
                                            console.log("Error: " + data + " - " + status);

                                        });
                                    }

                                }
                                break;
                            case "change.delete":
                                console.log("WEBSOCKET DELETE: " + msg.body.object.id);
                                break;
                            case "change.patch":
                                break;
                        }
                    } catch (e) {
                        console.error("layer-patch Error: " + e);
                    }


                };
                $rootScope.chatWebsocket.onerror = function (evt) {
                    console.log("Chat Error: " + evt.data);
                };
                $rootScope.chatWebsocket.onclose = function () {
                    console.log(">>> Chat WS on Close");
                };
            } else {
                console.log("not initialized bacause session token not ready yet: " + $rootScope.chat_session_token);
            }
        };
        factory.closeWebSocket = function () {
            if ($rootScope.chatWebsocket) {
                $rootScope.chatWebsocket.close();
            }
        };


        return factory;
    };
    $sockets.$inject = ['$rootScope', '$cookieStore', '$chat', '$filter', 'appConstants'];
    angular.module("codename").factory("$sockets", $sockets);
}());