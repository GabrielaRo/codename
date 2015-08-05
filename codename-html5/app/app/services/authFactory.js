(function () {
    var $auth = function ($cookieStore) {
        var factory = {};
        //SIGNUP
        factory.authorize = function (loginRequired, profileRequired, requiredPermissions, permissionCheckType) {

            if (loginRequired == true) {
                var status = $cookieStore.get('auth_token') ? 'Authorized' : 'NotAuthorized';

                if (status == 'Authorized') {
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