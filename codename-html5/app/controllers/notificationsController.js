(function () {

    var notificationsController = function ($scope, $rootScope) {
        

//        $rootScope.websocket.onmessage = function (evt) {
//
//            var msg = JSON.parse(evt.data);
//            switch (msg.type) {
//                case 'message':
//                    console.log("On Message Notification Controller! " + evt.data);
//                    $presence.newNotifications = $presence.newNotifications + 1;
//                    $presence.notifications.push({date: Date.now(), message: 'text: ' + msg.text});
//                    $scope.$apply(function () {
//                        $scope.newNotifications = $presence.newNotifications;
//                        $scope.notifications = $presence.notifications;
//                    });
//                    break;
//            }
//        };
        $scope.checkNew = function (notification_id) {

        }

        $scope.loadNotifications = function () {



        }

        //JQ


        $('.dropdown.dropdown-li').data('open', false);
        $('#notifications-dropdown').click(function () {
            if ($('.dropdown.dropdown-li').data('open')) {
                $('.dropdown.dropdown-li').data('open', false);
                $scope.clearNewNotifications();
            } else
                $('.dropdown.dropdown-li').data('open', true);
        });
        $(document).click(function () {
            if ($('.dropdown.dropdown-li').data('open')) {
                $('.dropdown.dropdown-li').data('open', false);
                $scope.clearNewNotifications();
            }
        });

    };
    notificationsController.$inject = ['$scope', '$presence'];
    angular.module("codename").controller("notificationsController", notificationsController);
}());
