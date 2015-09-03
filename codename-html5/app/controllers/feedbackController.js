(function () {
    var feedbackController = function ($scope, $rootScope, $users, $auth, appConstants, $contact) {
       $scope.pageClass = "feedback";
       $( window ).scrollTop( 0 );
       
        $scope.sendContactForm = function (email, name, subject, text, type) {

            $contact.sendMessage(email, name, subject, text, type).success(function (data) {
                $scope.feedbackContactStatus = true;
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Message Sent", 'success');
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });

        }
       
    };

    
    feedbackController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants', '$contact'];
    angular.module("codename").controller("feedbackController", feedbackController);

}());


