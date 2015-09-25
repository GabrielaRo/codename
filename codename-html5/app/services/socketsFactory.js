(function () {
    var $sockets = function ($rootScope, $cookieStore, appConstants) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function () {

            var wsUri = "ws://" + appConstants.address + ":" + appConstants.port + "/" + appConstants.context
                    + "fhellow?nickname=" + $cookieStore.get('user_nick');

            if ($rootScope.chat_session_token) {
                $rootScope.presenceWebsocket = new WebSocket(wsUri);
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

                $rootScope.chatWebsocket = new WebSocket('wss://api.layer.com/websocket?session_token=' + $rootScope.chat_session_token, 'layer-1.0');
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
                                if (msg.body.data.sender.user_id !== $cookieStore.get('user_nick')) {
                                    $rootScope.$apply(function () {
                                        $rootScope.newNotifications = $rootScope.newNotifications + 1;

                                    });
                                    if ($rootScope.selectedConversation.id === msg.body.data.conversation.id) {
                                       
                                        $rootScope.$apply(function () {
                                            $rootScope.messageHistory.push(msg.body.data);
                                        });
                                        for (var i = 0; i < $rootScope.inbox.length; i++) {
                                            if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
                                                $rootScope.$apply(function () {
                                                    console.log($rootScope.inbox[i].last_message);
                                                    $rootScope.inbox[i].last_message.sender.user_id = msg.body.data.sender.user_id;
                                                    $rootScope.inbox[i].last_message.parts[0].body = msg.body.data.parts[0].body;
                                                    $rootScope.inbox[i].last_message.received_at = new Date();
                                                    $rootScope.inbox[i].unread_message_count = $rootScope.inbox[i].unread_message_count + 1;
                                                });
                                                var newListHeight = $(".messages-history").height();
                                                $("#user-messages-chat").animate({scrollTop: newListHeight}, 200);
                                            }

                                        }
                                    } else {
                                        for (var i = 0; i < $rootScope.inbox.length; i++) {
                                            if ($rootScope.inbox[i].url === msg.body.data.conversation.url) {
                                                $rootScope.$apply(function () {
                                                    $rootScope.inbox[i].last_message.sender.user_id = msg.body.data.sender.user_id;
                                                    $rootScope.inbox[i].last_message.parts[0].body = msg.body.data.parts[0].body;
                                                    $rootScope.inbox[i].last_message.received_at = new Date();
                                                    $rootScope.inbox[i].unread_message_count = $rootScope.inbox[i].unread_message_count + 1;
                                                });
                                            }

                                        }
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
    $sockets.$inject = ['$rootScope', '$cookieStore', 'appConstants'];
    angular.module("codename").factory("$sockets", $sockets);
}());