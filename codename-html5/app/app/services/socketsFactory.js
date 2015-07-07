(function () {
    var $sockets = function ($cookieStore, appConstants) {
        var factory = {};
        //init web socket for a client
        factory.initWebSocket = function(){
            //var wsUri = "ws://" + "grog-restprovider.rhcloud.com:8000" + "/grogshop-server/" + "shop";
            var wsUri = "ws://" + "localhost:8080" + "/codename-server/" + "fhellow?email=" + $cookieStore.get('email');
            //var wsUri = "ws://" + document.location.hostname + ":" + document.location.port + "/grogshop-server/" + "shop";
            $scope.websocket = new WebSocket(wsUri);
            console.log("Init websocket for: "+$cookieStore.get('email'));
            $scope.websocket.onopen = function (evt) {
                console.log("onOpen client side");

            };
            $scope.websocket.onmessage = function (evt) {
                console.log(">>> onMessage: " + evt.data);
                $rootScope.$broadcast('quickNotification', evt.data, "success");
               // $notifications.newMatchingsNotifications.push(evt.data);
            };
            $scope.websocket.onerror = function (evt) {
                console.log("Error: " + evt.data);
            };

            $scope.websocket.onclose = function () {
                console.log("onClose client side");
            };
            
        };
        
        factory.closeWebSocket = function(){
            $scope.websocket.onclose = function () {};
            $scope.websocket.close(); 
        };
        
        

        return factory;
    };

    $sockets.$inject = ['$cookieStore','appConstants'];
    angular.module("codename").factory("$sockets", $sockets);

}());