(function () {
    var $sockets = function ($rootScope, $cookieStore, $presence, appConstants) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function () {
            //var wsUri = "ws://" + "grog-restprovider.rhcloud.com:8000" + "/grogshop-server/" + "shop";
            var wsUri = "ws://" + appConstants.address + ":" + appConstants.port + "/" + appConstants.context
                    + "fhellow?nickname=" + $cookieStore.get('user_nick');

            $rootScope.websocket = new WebSocket(wsUri);

            $rootScope.websocket.onopen = function (evt) {
                console.log(">>> WS on Open");
                
            };
            $rootScope.websocket.onmessage = function (evt) {

                var msg = JSON.parse(evt.data);
                switch (msg.type) {
                    case 'online':

                        break;
                    case 'message':
                        $presence.newNotifications = $presence.newNotifications + 1;
                        $presence.notifications.push({date: Date.now(), message: 'text: ' + msg.text});

                        break;
                }
            };
            $rootScope.websocket.onerror = function (evt) {
                console.log("Error: " + evt.data);
            };

            $rootScope.websocket.onclose = function () {
                console.log(">>> WS on Close");
            };

        };

        factory.closeWebSocket = function () {
            if($rootScope.websocket){
                $rootScope.websocket.close();
            }
        };



        return factory;
    };

    $sockets.$inject = ['$rootScope', '$cookieStore', '$presence', 'appConstants'];
    angular.module("codename").factory("$sockets", $sockets);

}());