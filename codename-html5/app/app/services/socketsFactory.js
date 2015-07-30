(function () {
    var $sockets = function ($rootScope, $cookieStore, appConstants) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function () {
            //var wsUri = "ws://" + "grog-restprovider.rhcloud.com:8000" + "/grogshop-server/" + "shop";
            var wsUri = "ws://" + appConstants.address + ":" + appConstants.port + "/" + appConstants.context 
                    + "fhellow?nickname=" + $cookieStore.get('user_nick');
            
            $rootScope.websocket = new WebSocket(wsUri);
            console.log("Init websocket for: " + $cookieStore.get('user_nick'));
            $rootScope.websocket.onopen = function (evt) {
                console.log("onOpen client side");

            };
            $rootScope.websocket.onmessage = function (evt) {
                console.log(">>> onMessage: " + evt.data);
                $rootScope.$broadcast('quickNotification', evt.data, "success");
                // $notifications.newMatchingsNotifications.push(evt.data);
            };
            $rootScope.websocket.onerror = function (evt) {
                console.log("Error: " + evt.data);
            };

            $rootScope.websocket.onclose = function () {
                console.log("onClose client side");
            };

        };

        factory.closeWebSocket = function () {
            $rootScope.websocket.onclose = function () {
            };
            $rootScope.websocket.close();
        };



        return factory;
    };

    $sockets.$inject = ['$rootScope', '$cookieStore', 'appConstants'];
    angular.module("codename").factory("$sockets", $sockets);

}());