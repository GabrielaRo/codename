(function () {
    var inviteController = function ($scope, $rootScope, $users, $auth, appConstants, $routeParams, $invites ) {
       $scope.pageClass = "invite full";
       $scope.inviteStatus = false;
       $scope.inviteContactStatus = false;
        $scope.credentials = [];
      
       $scope.sendContactForm = function(){
            $scope.inviteContactStatus = true;
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
       $scope.initCarousel = function(){
           setTimeout(function(){ $scope.moveCarousel() }, 6000);
           
       }
       $scope.moveCarousel = function(){
           
           if($scope.carouselPosition < $scope.carouselLenght){
                $scope.carouselPosition++
           }else {
                $scope.carouselPosition = 1;
           }
           
           $scope.carouselItems.each(function(index){
               var itemPosition = 100 - ($scope.carouselPosition - index) * 100;
               $(this).css( "left", itemPosition+"%" );
               $(this).removeClass("active");
               if((index+1) == $scope.carouselPosition){
                    $(this).addClass("active");
               }
           });
            $scope.initCarousel();
       }
       
       $scope.initCarousel();

        var nounAudio = new Audio('static/noun.mp3');
        $scope.playWord = function(){
            nounAudio.play();
        }
        
        
        if ($routeParams && $routeParams.inviteLogin) {
       
            
            $scope.inviteLogin = $routeParams.inviteLogin;
            $scope.credentials.email = $routeParams.userMail;
            
        }
        
        
    };

    inviteController.$inject = ['$scope', '$rootScope', '$users', '$auth', 'appConstants', '$routeParams','$invites'];
    angular.module("codename").controller("inviteController", inviteController);

}());


