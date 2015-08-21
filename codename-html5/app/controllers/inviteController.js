(function () {
    var inviteController = function ($location, $scope, $rootScope, $users, $auth, appConstants, $routeParams, $invites, $contact) {
        $scope.pageClass = "invite full";
        $scope.inviteStatus = false;
        $scope.inviteContactStatus = false;
        $scope.credentials = [];

        $scope.sendContactForm = function (email, name, subject, text, type) {

            $contact.sendMessage(email, name, subject, text, type).success(function (data) {
                $scope.inviteContactStatus = true;
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Message Sent", 'success');
            }).error(function (data) {
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });

        }

        $scope.requestInvite = function (email) {

            $invites.request(email).success(function (data) {
                $scope.inviteStatus = true;
                $rootScope.$broadcast("quickNotification", "<i class='fa fa-check'></i> Invitation Sent", 'success');
                $('#mainview').animate({
                    'scrollTop': 0
                }, 1000, 'swing', function () {

                });


            }).error(function (data) {

                $rootScope.$broadcast("quickNotification", "<i class='fa fa-exclamation-triangle'></i> Something failed: " + data, 'error');
                console.log("Error : " + data + "!");

            });

        }

        $scope.carouselPosition = 1;
        $scope.carouselItems = $(".carousel-item");
        $scope.carouselLenght = $scope.carouselItems.length;
        $scope.initCarousel = function () {
            if (!$scope.inviteStatus) {
                setTimeout(function () {
                    $scope.moveCarousel()
                }, 6000);
            } else {
                $scope.carouselPosition = 1;
                $scope.carouselItems.each(function (index) {
                    var itemPosition = 100 - ($scope.carouselPosition - index) * 100;
                    $(this).css("left", itemPosition + "%");
                    $(this).removeClass("active");
                    if ((index + 1) == $scope.carouselPosition) {
                        $(this).addClass("active");
                    }
                });
            }

        }
        $scope.moveCarousel = function () {

            if ($scope.carouselPosition < $scope.carouselLenght) {
                $scope.carouselPosition++
            } else {
                $scope.carouselPosition = 1;
            }

            $scope.carouselItems.each(function (index) {
                var itemPosition = 100 - ($scope.carouselPosition - index) * 100;
                $(this).css("left", itemPosition + "%");
                $(this).removeClass("active");
                if ((index + 1) == $scope.carouselPosition) {
                    $(this).addClass("active");
                }
            });
            $scope.initCarousel();
        }

        $scope.initCarousel();

        var nounAudio = new Audio('static/noun.mp3');
        $scope.playWord = function () {
            nounAudio.play();
        }

        $scope.inviteLogin = $location.search().email;
        $scope.credentials.email = $location.search().email;

    };

    inviteController.$inject = ['$location', '$scope', '$rootScope', '$users', '$auth', 'appConstants', '$routeParams', '$invites', '$contact'];
    angular.module("codename").controller("inviteController", inviteController);

}());


