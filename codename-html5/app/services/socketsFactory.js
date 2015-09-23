(function () {
    var $sockets = function ($rootScope, $cookieStore) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function () {
            //var wsUri = "ws://" + "grog-restprovider.rhcloud.com:8000" + "/grogshop-server/" + "shop";
//            var wsUri = "ws://" + appConstants.address + ":" + appConstants.port + "/" + appConstants.context
//                    + "fhellow?nickname=" + $cookieStore.get('user_nick');

            if ($rootScope.chat_session_token) {
                $rootScope.websocket = new WebSocket('wss://api.layer.com/websocket?session_token=' + $rootScope.chat_session_token, 'layer-1.0');
                $rootScope.websocket.onopen = function (evt) {
                    console.log(">>> WS on Open");
                };
                $rootScope.websocket.onmessage = function (evt) {
                    console.log(">> On Message: ");
                    console.log(evt);
//                    $rootScope.$apply(function () {
//                        $rootScope.newNotifications = $rootScope.newNotifications + 1;
//                        console.log("notifications now: " + $rootScope.newNotifications);
//                    });
                    var msg = JSON.parse(evt.data);
                    try {
                        switch (msg.type + "." + msg.body.operation) {
                            case "change.create":
                                console.log("WEBSOCKET CREATE: " + msg.body.object.id + " - by: " + msg.body.data.sender.user_id);
                                if (msg.body.data.sender.user_id !== $cookieStore.get('user_nick')) {
                                    $rootScope.$apply(function () {
                                        $rootScope.newNotifications = $rootScope.newNotifications + 1;
                                        console.log("notifications now: " + $rootScope.newNotifications);
                                    });
                                    
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
                $rootScope.websocket.onerror = function (evt) {
                    console.log("Error: " + evt.data);
                };
                $rootScope.websocket.onclose = function () {
                    console.log(">>> WS on Close");
                };
            } else {
                console.log("not initialized bacause session token not ready yet: " + $rootScope.chat_session_token);
            }
        };
        factory.closeWebSocket = function () {
            if ($rootScope.websocket) {
                $rootScope.websocket.close();
            }
        };
        return factory;
    };
    $sockets.$inject = ['$rootScope', '$cookieStore'];
    angular.module("codename").factory("$sockets", $sockets);
}());