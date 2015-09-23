
(function () {

    angular.module('codename');

    var MainCtrl = function ($scope, $cookieStore, $rootScope, $users, $auth, appConstants, $sockets, $routeParams, growl, $chat, $error) {
        $rootScope.auth_token = $cookieStore.get('auth_token');
        $rootScope.email = $cookieStore.get('email');
        $rootScope.user_id = $cookieStore.get('user_id');
        $rootScope.user_full = $cookieStore.get('user_full');
        $rootScope.user_id = $cookieStore.get('user_roles');
        $rootScope.user_nick = $cookieStore.get('user_nick');
        $rootScope.firstLogin = $cookieStore.get('firstLogin');
        $scope.index = 0;
        $scope.notifications = [];

        $rootScope.avatarStyle = "";
        $rootScope.socket = {};
        $scope.$routeParams = $routeParams;


        $rootScope.$on('quickNotification', function (event, data, type) {
            var config = {};

            if (!data) {
                $scope.invalidNotification = true;
                return;
            }

            i = $scope.index++;
            $scope.invalidNotification = false;
            $scope.notifications[i] = data;

            switch (type) {
                case "success":
                    growl.success(data, config);
                    break;
                case "info":
                    growl.info(data, config);
                    break;
                case "warning":
                    growl.warning(data, config);
                    break;
                case "error":
                    growl.error(data, config);
                    break;
                default:
                    growl.error(data, config);
            }




        });

        $scope.logoutUser = function () {
            $rootScope.auth_token = "";
            $rootScope.email = "";
            $rootScope.user_id = "";
            $rootScope.user_full = "";
            $rootScope.user_roles = "";
            $rootScope.user_nick = "";
            $rootScope.firstLogin = "";
            $rootScope.live = "";


            $users.logout().success(function (data) {

                $cookieStore.remove('auth_token');
                $cookieStore.remove('email');
                $cookieStore.remove('user_id');
                $cookieStore.remove('user_full');
                $cookieStore.remove('user_roles');
                $cookieStore.remove('user_nick');
                $cookieStore.remove('live');
                $rootScope.avatarStyle = "";
                $sockets.closeWebSocket();
                $rootScope.$broadcast('goTo', "/");
            }).error(function (data, status) {
                $error.handleError(data, status);
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
                    $cookieStore.put('user_full', data.user_full);
                    $cookieStore.put('user_roles', data.user_roles);
                    $cookieStore.put('user_nick', data.user_nick);
                    $cookieStore.put('firstLogin', data.firstLogin);
                    $cookieStore.put('live', data.live);
                    $rootScope.auth_token = $cookieStore.get('auth_token');
                    $rootScope.email = $cookieStore.get('email');
                    $rootScope.user_id = $cookieStore.get('user_id');
                    $rootScope.user_full = $cookieStore.get('user_full');
                    $rootScope.user_roles = $cookieStore.get('user_roles');
                    $rootScope.user_nick = $cookieStore.get('user_nick');
                    $rootScope.firstLogin = $cookieStore.get('firstLogin');
                    $rootScope.live = $cookieStore.get('live');
                    $rootScope.credentials = {};
                    $rootScope.submitted = false;
                    $rootScope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $rootScope.user_nick + '/avatar?size=250' + '&' + new Date().getTime() + ')'};

                    $rootScope.initChat();
                    if ($rootScope.firstLogin) {
                        $rootScope.$broadcast('goTo', "/profile");
                    } else {
                        $rootScope.$broadcast('goTo', "/localfhellows");
                    }

                }).error(function (data, status) {
                    $error.handleError(data, status, "Wrong username and password.");
                });
            }
        };

        $rootScope.initChat = function () {

            $chat.getNonce().success(function (data) {
//                console.log("nonce = ");
//                        console.log(data.nonce);
                $chat.getIdentityToken(data.nonce).success(function (data) {
//                    console.log("identity token = ");
//                        console.log(data.identity_token);
                    $chat.getChatSession(data.identity_token).success(function (data) {

//                        console.log("session_token = ");
//                        console.log(data.session_token);
                        $rootScope.chat_session_token = data.session_token;
                        appConstants.chatHeaders.Authorization = 'Layer session-token=' + data.session_token;
                        $sockets.initWebSocket();
                        $rootScope.chatOnline = true;

                        $chat.getConversations().success(function (data) {
                            var unread = 0;
                            for (var i = 0; i < data.length; i++) {

                                unread += data[i].unread_message_count;
                            }
                            $rootScope.newNotifications = unread;

                        }).error(function (data, status) {
                            console.log("Error: ");
                            console.log(data);
                            console.log(status);
                        });

                    }).error(function (data, status) {
                        console.log("Error: ");
                        console.log(data);
                        console.log(status);
                    });
                }).error(function (data, status) {
                    console.log("Error: ");
                    console.log(data);
                    console.log(status);
                });

            }).error(function (data, status) {
                console.log("Error: ");
                console.log(data);
                console.log(status);
            });


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
                                $cookieStore.put('user_full', data.user_full);
                                $cookieStore.put('user_roles', data.user_roles);
                                $cookieStore.put('user_nick', data.user_nick);
                                $cookieStore.put('firstLogin', data.firstLogin);
                                $cookieStore.put('live', data.live);
                                $rootScope.auth_token = $cookieStore.get('auth_token');
                                $rootScope.email = $cookieStore.get('email');
                                $rootScope.user_id = $cookieStore.get('user_id');
                                $rootScope.user_full = $cookieStore.get('user_full');
                                $rootScope.user_roles = $cookieStore.get('user_roles');
                                $rootScope.user_nick = $cookieStore.get('user_nick');
                                $rootScope.firstLogin = $cookieStore.get('firstLogin');
                                $rootScope.live = $cookieStore.get('live');
                                $rootScope.credentials = {};
                                $rootScope.submitted = false;
                                $rootScope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $rootScope.user_nick + '/avatar?size=250' + '&' + new Date().getTime() + ')'};

                                $rootScope.initChat();
                                if ($rootScope.firstLogin) {
                                    $rootScope.$broadcast('goTo', "/profile");
                                } else {
                                    $rootScope.$broadcast('goTo', "/localfhellows");
                                }

                            }).error(function (data, status) {
                                $error.handleError(data, status);
                            });
                        } else {
                            $rootScope.$broadcast('goTo', "/");
                        }

                    }).catch(function (response) {
                console.log(response);
            });
        };


        if ($rootScope.auth_token && $rootScope.auth_token !== "") {

            $rootScope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $rootScope.user_nick + '/avatar?size=250' + '&' + new Date().getTime() + ')'};

            $rootScope.initChat();
        }

        $rootScope.$on("updateUser", function (event, data) {
            $cookieStore.put('user_nick', data.userNick);
            $rootScope.user_nick = $cookieStore.get('user_nick');
            $rootScope.avatarStyle = {'background-image': 'url(' + appConstants.server + appConstants.context + 'rest/public/users/' + $rootScope.user_nick + '/avatar?size=250' + '&' + new Date().getTime() + ')'};

        });
    };


    MainCtrl.$inject = ['$scope', '$cookieStore', '$rootScope', '$users', '$auth',
        'appConstants', '$sockets', '$routeParams', 'growl', '$chat', '$error'];
    angular.module("codename").controller("MainCtrl", MainCtrl);
}());




