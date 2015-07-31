(function () {

    var notificationsController = function ($scope, $notifications, appConstants) {
        
        
        $scope.newNotifications = $notifications.newNotifications;
        $scope.notifications = $notifications.notifications;
        
        $scope.checkNew =  function(notification_id){
           
        }
        
        $scope.loadNotifications = function(){
            
            console.log("I should be loading notifications here ");
            
            
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

    notificationsController.$inject = ['$scope', '$notifications', 'appConstants'];
    angular.module("codename").controller("notificationsController", notificationsController);


}());
