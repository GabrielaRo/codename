(function () {
    var $error = function ( $rootScope, $cookieStore, $sockets) {
        var factory = {};

        
        factory.handleError = function (data, status) {
            console.log(">> Error (" + status + "): ");
            console.log(data);
            if (status == '401') {
                $rootScope.auth_token = "";
                $rootScope.email = "";
                $rootScope.user_id = "";
                $rootScope.user_full = "";
                $rootScope.user_roles = "";
                $rootScope.user_nick = "";
                $rootScope.firstLogin = "";
                $rootScope.live = "";
                $rootScope.avatarStyle = "";
                $cookieStore.remove('auth_token');
                $cookieStore.remove('email');
                $cookieStore.remove('user_id');
                $cookieStore.remove('user_full');
                $cookieStore.remove('user_roles');
                $cookieStore.remove('user_nick');
                $cookieStore.remove('live');
                $sockets.closeWebSocket();
                $rootScope.$broadcast("goTo", "/");
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i>You session was ended, please login again!" + data);
            }else{
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed, please try again.", 'error');
            }
            
        };

        return factory;
    };

    $error.$inject = [ '$rootScope', '$cookieStore', '$sockets'];
    angular.module("codename").factory("$error", $error);

}());