(function () {
    var $notifications = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};
        factory.newNotifications = 0;
        factory.notifications = [];

        factory.clearNewNotifications = function () {
            console.log("clearing notifications! ");
            factory.newNotifications = 0;
            factory.notifications = [];
        };


        return factory;
    };

    $notifications.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$notifications", $notifications);

}());