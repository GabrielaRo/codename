(function () {
    var $auth = function ($cookieStore) {
        var factory = {};
        //SIGNUP
        factory.authorize = function (loginRequired, profileRequired, requiredPermissions, permissionCheckType) {
            console.log('Login Required: ' + loginRequired);
            console.log('Profile Required: ' + profileRequired);
            console.log('requiredPermissions: ' + requiredPermissions);
            console.log('permissionCheckType: ' + permissionCheckType);
            if (loginRequired == true) {
                var status = $cookieStore.get('auth_token') ? 'Authorized' : 'NotAuthorized';
                console.log("Current status: "+ status);
                if (status == 'Authorized') {
                    if (profileRequired) {
                        console.log("is the user live? "+$cookieStore.get('live'));
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