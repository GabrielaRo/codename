(function () {
    var $auth = function ($cookieStore) {
        var factory = {};
        //SIGNUP
        factory.authorize = function (loginRequired, requiredPermissions, permissionCheckType) {
            console.log('Login Required: '+ loginRequired);
            console.log('requiredPermissions: '+ requiredPermissions);
            console.log('permissionCheckType: '+ permissionCheckType);
            if(loginRequired == true){
                return $cookieStore.get('auth_token') ? 'Authorized': 'NotAuthorized';
            }else{
                return 'Home';
            }
                
        }
        


        return factory;
    };

    $auth.$inject = ['$cookieStore'];
    angular.module("codename").factory("$auth", $auth);

}());