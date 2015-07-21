(function (){
    
    var messagesController = function($scope, $rootScope){
        $scope.inbox = [{user: "Mario Cavalli", excerpt: "Sounds good see you then", date: "10:09"}, {user: "Enrique Comba Riepenha", excerpt: "Thanks for the link - really interesting", date: "Tue"}, {user: "Lee Jackson", excerpt: "Awesome!!!!!", date: "Sun"}, {user: "Amy Jones", excerpt: "Hey Natalie, i’m heading", date: "11 Jun"}, {user: "James Vanderson", excerpt: "Thanks", date: "23 May"}, {user: "Sophie Taylor", excerpt: "Not sure will get back tomorrow", date: "19 May"}, {user: "Alannah Ward Thomas", excerpt: "Awesome", date: "2 Abr"}];
        
        $scope.selectedUser = $scope.inbox[0].user;
        $scope.messageHistory = [];
        
        $scope.selectUser = function(user){
            console.log(user);
             $scope.selectedUser = user;
        }
    };
    
    messagesController.$inject = ['$scope', '$rootScope'];
    angular.module( "codename" ).controller("messagesController", messagesController);
    
}());
