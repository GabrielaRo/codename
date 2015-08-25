(function () {
    var $auth = function ($cookieStore) {
        var factory = {};
        //SIGNUP
        factory.authorize = function (loginRequired, profileRequired, adminRequired, requiredPermissions, permissionCheckType) {

            if (loginRequired == true) {
                var status = $cookieStore.get('auth_token') ? 'Authorized' : 'NotAuthorized';

                if (status == 'Authorized') {
                    if (adminRequired) {
                        if($cookieStore.get('user_roles').indexOf('Admin') >= 0){
                            return 'AdminAuthorized';
                        }else{
                            return 'AdminNotAuthorized';
                        }
                    }
                    if (profileRequired) {

                        if (!$cookieStore.get('live')) {
                            return 'RequiresProfile';
                        } else {
                            return status;

                        }

                    } else {
                        return status;
                    }

                } else {
                    return status;
                }
            } else {
                return 'Home';
            }

        }



        return factory;
    };

    $auth.$inject = ['$cookieStore'];
    angular.module("codename").factory("$auth", $auth);

}());