(function(){
    var $notifications = function($transformRequestToForm, $cookieStore, $http, appConstants){
        var factory = {};
        factory.newMatchingsNotifications = [];
        factory.loadAll  = function(){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/notifications/user/'+ $cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        
        factory.clearNewNotifications  = function(){
            factory.newMatchingsNotifications = [];
            
        };
       
        return factory;
    };
    
    $notifications.$inject = ['$transformRequestToForm','$cookieStore', '$http', 'appConstants'];
    angular.module( "codename" ).factory("$notifications", $notifications);
    
}());