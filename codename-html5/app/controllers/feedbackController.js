(function () {
    var feedbackController = function ($scope, $rootScope,  $contact, $error) {
       $scope.pageClass = "feedback";
       $( window ).scrollTop( 0 );
       
        $scope.sendContactForm = function (email, name, subject, text, type) {

            $contact.sendMessage(email, name, subject, text, type).success(function (data) {
                $scope.feedbackContactStatus = true;
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Message Sent", 'success');
            }).error(function (data, status) {
                $error.handleError(data, status);
            });

        };
    };

    
    feedbackController.$inject = ['$scope', '$rootScope', '$contact', '$error'];
    angular.module("codename").controller("feedbackController", feedbackController);

}());


