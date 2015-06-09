(function (){
    var notificationsController = function ($rootScope, $scope, $cookieStore, $users,  $notifications) {
        var not = this; 
        $scope.notifications = []; ; 
        $scope.newMatchingsNotificationsArray = $notifications.newMatchingsNotifications;
    
        $scope.loadNotifications = function(){
            
            $notifications.loadAll().success(function (data) {
                   $scope.notifications = data;  
                
                }).error(function (data) {
                    console.log("Error: " + data);
                });
        }
    
        
        
        $scope.checkNew =  function(notification_id){
            console.log("CHECK NOTIFICATONS " + $notifications.newMatchingsNotifications.length )
            if($notifications.newMatchingsNotifications.length > 0){
                for( i = 0; i <  $notifications.newMatchingsNotifications.length; i++){
                    console.log(notification_id + " / " + $notifications.newMatchingsNotifications[i]);
                    if(notification_id == $notifications.newMatchingsNotifications[i]){
                        return true;
                    }
                    
                    
                }
            }else{
                return false;
            }
        };
        
        //JQ
        

            $('.dropdown.dropdown-li').data('open', false);

            $('#notifications-dropdown').click(function() {
                if($('.dropdown.dropdown-li').data('open')) {
                    $('.dropdown.dropdown-li').data('open', false);
                    $scope.clearNewNotifications();

                } else
                    $('.dropdown.dropdown-li').data('open', true);
            });

            $(document).click(function() {
                if($('.dropdown.dropdown-li').data('open')) {
                    $('.dropdown.dropdown-li').data('open', false);
                     
                     $scope.clearNewNotifications();
                    
              }
            });

        $scope.clearNewNotifications = function(){
            console.log($notifications.newMatchingsNotifications)
            console.log("CLEAR NOTIFICATION FROM CONTROLLER");
            $notifications.clearNewNotifications();
            $scope.newMatchingsNotificationsArray = $notifications.newMatchingsNotifications; 
            $scope.$apply();
        }
        
        
    };
    
    
    
    notificationsController.$inject = ['$rootScope','$scope','$cookieStore', '$users', '$notifications'];
    angular.module( "codename").controller("notificationsController", notificationsController);

}());
