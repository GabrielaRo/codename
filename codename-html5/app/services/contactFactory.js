(function () {
    var $contact = function ($http, $transformRequestToForm,  appConstants) {
        var factory = {};
        //send contact message
        factory.sendMessage = function (email, name, subject,  text, type) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/public/contact/message',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: email, name: name, subject: subject, text: text, type: type}
            })
        }
        


        return factory;
    };

    $contact.$inject = ['$http', '$transformRequestToForm','appConstants'];
    angular.module("codename").factory("$contact", $contact);

}());