(function () {
    var $contact = function ($http, $cookieStore, $transformRequestToForm, appConstants) {
        var factory = {};
        //send contact message
        factory.sendMessage = function (email, name, subject, text, type) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/public/contact/message',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: email, name: name, subject: subject, text: text, type: type}
            });
        };

        factory.getNotRepliedContactMessages = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/admin/contact/?replied=false',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };


        return factory;
    };

    $contact.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$contact", $contact);

}());