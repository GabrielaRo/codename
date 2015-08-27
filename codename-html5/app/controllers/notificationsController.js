(function () {

    var notificationsController = function ($rootScope, $scope, $notifications, appConstants) {
        console.log($notifications.notifications);

        $scope.newNotifications = $notifications.newNotifications;
        $scope.notifications = $notifications.notifications;
        $rootScope.websocket.onmessage = function (evt) {

            var msg = JSON.parse(evt.data);
            switch (msg.type) {
                case 'message':
                    console.log("On Message Notification Controller! " + evt.data);
                    $notifications.newNotifications = $notifications.newNotifications + 1;
                    $notifications.notifications.push({date: Date.now(), message: 'text: ' + msg.text});
                    $scope.$apply(function () {
                        $scope.newNotifications = $notifications.newNotifications;
                        $scope.notifications = $notifications.notifications;
                    });
                    break;
            }
        };
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
    notificationsController.$inject = ['$rootScope', '$scope', '$notifications', 'appConstants'];
    angular.module("codename").controller("notificationsController", notificationsController);
}());
