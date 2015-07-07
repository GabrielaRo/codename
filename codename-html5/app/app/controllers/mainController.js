
(function () {

    angular.module('codename');

    var MainCtrl = function ($scope, $cookieStore, $rootScope, $users, $sockets, appConstants,  $route, $routeParams) {
        $scope.auth_token = $cookieStore.get('auth_token');
        $scope.email = $cookieStore.get('email');
        $scope.user_id = $cookieStore.get('user_id');
        $scope.firstLogin = $cookieStore.get('firstLogin');
        $scope.index = 0;
        $scope.memberships = [];
        $scope.notifications = {};
        $scope.avatarStyle = "";
        $scope.websocket = {};
        $scope.$routeParams = $routeParams;
        
        
        $rootScope.$on('quickNotification', function (event, data, type) {
            var config = {};

            if (!data) {
                $scope.invalidNotification = true;
                return;
            }

            //i = $scope.index++;
            //$scope.invalidNotification = false;
            //$scope.notifications[i] = data;
            console.log("notification " + data);

//            switch (type) {
//                case "success":
//                    growl.success(data, config);
//                    break;
//                case "info":
//                    growl.info(data, config);
//                    break;
//                case "warning":
//                    growl.warning(data, config);
//                    break;
//                case "error":
//                    growl.error(data, config);
//                    break;
//                default:
//                    growl.error(data, config);
//            }




        });

        $scope.logoutUser = function () {
            $scope.auth_token = "";
            $scope.email = "";
            $scope.user_id = "";
            $scope.firstLogin = "";
            $scope.memberships = [];

            $users.logout().success(function (data) {
                console.log("You have been logged out." + data);
                $cookieStore.remove('auth_token');
                $cookieStore.remove('email');
                $scope.avatarStyle = "";
                $sockets.closeWebSocket();
                $rootScope.$broadcast('goTo', "/");
                $rootScope.$broadcast("quickNotification", "You have been logged out.", 'info');
            }).error(function (data) {
                console.log("Error: " + data);
                $rootScope.$broadcast("quickNotification", "Error: " + data, 'error');
            });

        };

        $scope.loginUser = function (isValid, user) {
            

            $scope.submitted = true;

            if (isValid) {
                console.log("logged user " + user.email + " / password" + user.password);
                $users.login(user).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "You are logged now, have fun!", 'success');
                    console.log("You are signed in! " + data.auth_token);
                    

                    $cookieStore.put('auth_token', data.auth_token);
                    $cookieStore.put('email', data.email);
                    $cookieStore.put('user_id', data.user_id);
                    $cookieStore.put('firstLogin', data.firstLogin);
                    $scope.auth_token = $cookieStore.get('auth_token');
                    $scope.email = $cookieStore.get('email');
                    $scope.user_id = $cookieStore.get('user_id');
                    $scope.firstLogin = $cookieStore.get('firstLogin');
                    $scope.credentials = {};
                    $scope.submitted = false;
                    $scope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $scope.user_id + '/avatar' + '?' + new Date().getTime() + ')'};
                    console.log("firstLogin: " + $scope.firstLogin);
                    $sockets.initWebSocket();
                    if ($scope.firstLogin) {
                        $rootScope.$broadcast('goTo', "/firstlogin");
                    } else {
                        $rootScope.$broadcast('goTo', "/localfhellows");
                    }

                }).error(function (data) {
                    console.log("Error: " + data.error);
                    $rootScope.$broadcast("quickNotification", "You are NOT logged in because:" + data.error, 'error');
                });
            }
        };

        
     
        

        if ($scope.auth_token && $scope.auth_token !== "") {
            $scope.initWebSocket();
            $scope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $scope.user_id + '/avatar' + '?' + new Date().getTime() + ')'};

        }

        $rootScope.$on("updateUser", function (data) {
            $scope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $scope.user_id + '/avatar' + '?' + new Date().getTime() + ')'};
            $scope.auth_token = data;
        });
    };


    MainCtrl.$inject = ['$scope', '$cookieStore', '$rootScope', '$users', '$sockets',  'appConstants',   '$route', '$routeParams' ];
    angular.module("codename").controller("MainCtrl", MainCtrl);
}());




