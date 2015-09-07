(function () {
    var contactController = function ($scope, $rootScope, $contact, $error) {
        $scope.pageClass = "contact";
        $(window).scrollTop(0);

        $scope.sendContactForm = function (email, name, subject, text, type) {

            $contact.sendMessage(email, name, subject, text, type).success(function (data) {
                $scope.contactStatus = true;
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Message Sent", 'success');
            }).error(function (data, status) {
                $error.handleError(data, status);
            });

        }

    };

    contactController.$inject = ['$scope', '$rootScope', '$contact', '$error'];
    angular.module("codename").controller("contactController", contactController);

}());


