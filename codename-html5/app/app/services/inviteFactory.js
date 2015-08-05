(function () {
    var $invites = function ($http, $transformRequestToForm,  appConstants) {
        var factory = {};
        //SIGNUP
        factory.request = function (email) {
            console.log('email in the factory: '+ email);
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/public/invite/request',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: email}
            })
        }
        


        return factory;
    };

    $invites.$inject = ['$http', '$transformRequestToForm','appConstants'];
    angular.module("codename").factory("$invites", $invites);

}());