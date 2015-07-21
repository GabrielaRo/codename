(function (){
    
    var messagesController = function($scope, $rootScope){
        $scope.inbox = [{user: "Mario Cavalli", excerpt: "Sounds good see you then", date: "10:09", onlineStatus: false}, {user: "Enrique Comba Riepenha", excerpt: "Thanks for the link - really interesting", date: "Tue" , onlineStatus: true}, {user: "Lee Jackson", excerpt: "Awesome!!!!!", date: "Sun", onlineStatus: false}, {user: "Amy Jones", excerpt: "Hey Natalie, i’m heading", date: "11 Jun", onlineStatus: false}, {user: "James Vanderson", excerpt: "Thanks", date: "23 May", onlineStatus: true}, {user: "Sophie Taylor", excerpt: "Not sure will get back tomorrow", date: "19 May", onlineStatus: false}, {user: "Alannah Ward Thomas", excerpt: "Awesome", date: "2 Abr", onlineStatus: false}];
        
        $scope.selectedUser = $scope.inbox[0].user;
        $scope.messageHistory = [
            {day: "Sunday", messages:[ 
                            {user:"Other User", text: "Hey, im going to go work in the Hoxton tomorrow. Fancy joining?", hour:"16:58"}, 
                            {user:"You", text: "Hey James, yeah that would be good. I have a meeting at 11am in Soho but could head over afterwards. What time are you thinking?", hour:"17:04"}, 
                            {user:"Other User", text: "I’ll be there from 10am so come on over whenever your ready.", hour:"17:04"},
                            {user:"Other User", text: "Hey, im going to go work in the Hoxton tomorrow. Fancy joining?", hour:"16:58"}, 
                            {user:"You", text: "Hey James, yeah that would be good. I have a meeting at 11am in Soho but could head over afterwards. What time are you thinking?", hour:"17:04"}, 
                            {user:"Other User", text: "I’ll be there from 10am so come on over whenever your ready.", hour:"17:04"}
            ]
            }, 
             {day: "Today", messages:[ {user:"You", text: "On my way!", hour:"12:08"}]}
            ];
        
        $scope.selectUser = function(user){
            console.log(user);
             $scope.selectedUser = user;
        }
    };
    
    messagesController.$inject = ['$scope', '$rootScope'];
    angular.module( "codename" ).controller("messagesController", messagesController);
    
}());
