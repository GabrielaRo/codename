(function () {
    var $sockets = function ($rootScope, $cookieStore, $notifications, appConstants) {
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

                var msg = JSON.parse(evt.data);
                switch(msg.type){
                    case 'online': 
                        console.log("Hey there is a change of state in user : " + msg.user + " online status: "+ msg.online );
                        break;
                    case 'message':
                        $notifications.newNotifications = $notifications.newNotifications + 1;
                        
                        $notifications.notifications.push({date:  Date.now(), message: 'text: '+msg.text });
                        console.log("new Message here updating nav bar: " + $notifications.newNotifications);
                        break;
                }
            };
            $rootScope.websocket.onerror = function (evt) {
                console.log("Error: " + evt.data);
            };

            $rootScope.websocket.onclose = function () {
                console.log("onClose client side");
            };

        };

        factory.closeWebSocket = function () {
            $rootScope.websocket.close();
        };



        return factory;
    };

    $sockets.$inject = ['$rootScope', '$cookieStore','$notifications', 'appConstants'];
    angular.module("codename").factory("$sockets", $sockets);

}());