(function () {

    var NavigationController = function ($scope, $location, $rootScope, $invites) {


        $scope.isActive = function (route) {
            return route === $location.path();
        };

        $rootScope.$on('goTo', function (event, data) {
            $location.path(data);
        });


        //INVITE NAV
        $scope.animationRunning = false;

        $scope.goToSubSection = function (target) {


            if ($scope.animationRunning == false) {

                var targetElement = $("#" + target);
                var targetPosition = (targetElement.position().top - 60) + $('#mainview').scrollTop();

                $scope.animationRunning = true;
                console.log(targetElement);
                console.log(targetPosition);
                console.log($('#mainview').scrollTop());

                $('#mainview').animate({
                    'scrollTop': targetPosition
                }, 1000, 'swing', function () {
                    $scope.animationRunning = false;
                });
            }

        }

       




    };

    NavigationController.$inject = ['$scope', '$location', '$rootScope', '$invites'];
    angular.module("codename").controller("NavigationController", NavigationController);

}());
