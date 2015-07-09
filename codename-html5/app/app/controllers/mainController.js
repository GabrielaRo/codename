
(function () {

    angular.module('codename');

    var MainCtrl = function ($scope, $cookieStore, $rootScope, $users, $auth, $sockets, appConstants, $route, $routeParams, location) {
        $scope.auth_token = $cookieStore.get('auth_token');
        $scope.email = $cookieStore.get('email');
        $scope.user_id = $cookieStore.get('user_id');
        $scope.firstLogin = $cookieStore.get('firstLogin');
        $scope.index = 0;
        $scope.memberships = [];

        $scope.avatarStyle = "";
        $scope.websocket = {};
        $scope.$routeParams = $routeParams;
        
        
        location.get(angular.noop, angular.noop);
        $scope.isModalVisible = false;

        $scope.toggleModal = function() {
          $scope.isModalVisible = !$scope.isModalVisible;
        };

        $scope.$watch('pickedLocation', $scope.toggleModal);
        $scope.$watch('lookedUpLocation', $scope.toggleModal);


        $rootScope.$on('quickNotification', function (event, data, type) {
            var config = {};

            if (!data) {
                $scope.invalidNotification = true;
                return;
            }

            //i = $scope.index++;
            //$scope.invalidNotification = false;
            //$scope.notifications[i] = data;
            //console.log("notification " + data);

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
                $users.login(user).success(function (data) {
                    $rootScope.$broadcast("quickNotification", "You are logged now, have fun!", 'success');

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
                    $sockets.initWebSocket();
                    if ($scope.firstLogin) {
                        $rootScope.$broadcast('goTo', "/profile");
                    } else {
                        $rootScope.$broadcast('goTo', "/localfhellows");
                    }

                }).error(function (data) {
                    console.log("Error: " + data.error);
                    $rootScope.$broadcast("quickNotification", "You are NOT logged in because:" + data.error, 'error');
                });
            }
        };


        $scope.authenticate = function (provider) {
            $auth.authenticate(provider)
                    .then(function () {
                        if ($auth.isAuthenticated()) {
                            $users.loginExternal().success(function (data) {
                                $rootScope.$broadcast("quickNotification", "You are logged now from an external service, have fun!", 'success');

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
                                $sockets.initWebSocket();
                                if ($scope.firstLogin) {
                                    $rootScope.$broadcast('goTo', "/profile");
                                } else {
                                    $rootScope.$broadcast('goTo', "/localfhellows");
                                }

                            }).error(function (data) {
                                console.log("Error: " + data.error);
                                $rootScope.$broadcast("quickNotification", "You are NOT logged in because:" + data.error, 'error');
                            });
                        } else {
                            $rootScope.$broadcast('goTo', "/");
                        }

                    }).catch(function (response) {
                console.log(response);
            });
        };


        if ($scope.auth_token && $scope.auth_token !== "") {

            $scope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $scope.user_id + '/avatar' + '?' + new Date().getTime() + ')'};
            $sockets.initWebSocket();
        }

        $rootScope.$on("updateUser", function (data) {
            $scope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $scope.user_id + '/avatar' + '?' + new Date().getTime() + ')'};

        });
    };


    MainCtrl.$inject = ['$scope', '$cookieStore', '$rootScope', '$users', '$auth', '$sockets', 'appConstants', '$route', '$routeParams', 'location'];
    angular.module("codename").controller("MainCtrl", MainCtrl);
}());




