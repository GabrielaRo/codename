(function () {

    var messagesController = function ($scope, $chat, $cookieStore,$routeParams, $rootScope) {
//        $scope.inbox = [{user: "Salaboy", nickname: "salaboy", excerpt: "Sounds good see you then", date: "10:09", onlineStatus: false}, {user: "Grog DJ", nickname:"grogdj" , excerpt: "Thanks for the link - really interesting", date: "Tue", onlineStatus: true}, 
//            {user: "Lee Jackson", nickname:"ttt9" ,excerpt: "Awesome!!!!!", date: "Sun", onlineStatus: false}, 
//            {user: "Amy Jones",nickname:"ttt10" , excerpt: "Hey Natalie, i’m heading", date: "11 Jun", onlineStatus: false}, 
//            {user: "James Vanderson", nickname:"ttt11" ,excerpt: "Thanks", date: "23 May", onlineStatus: true}, 
//            {user: "Sophie Taylor", nickname:"ttt12" ,excerpt: "Not sure will get back tomorrow", date: "19 May", onlineStatus: false}, 
//            {user: "Alannah Ward Thomas",nickname:"ttt13" , excerpt: "Awesome", date: "2 Abr", onlineStatus: false}];


//        $scope.messageHistory = [
//            {day: "Sunday", messages: [
//                    {user: "Other User", nickname:"ttt9", text: "Hey, im going to go work in the Hoxton tomorrow. Fancy joining?", hour: "16:58"},
//                    {user: "You", nickname:"salaboy", text: "Hey James, yeah that would be good. I have a meeting at 11am in Soho but could head over afterwards. What time are you thinking?", hour: "17:04"},
//                    {user: "Other User",nickname:"ttt9", text: "I’ll be there from 10am so come on over whenever your ready.", hour: "17:04"},
//                    {user: "Other User",nickname:"ttt9", text: "Hey, im going to go work in the Hoxton tomorrow. Fancy joining?", hour: "16:58"},
//                    {user: "You", nickname:"salaboy", text: "Hey James, yeah that would be good. I have a meeting at 11am in Soho but could head over afterwards. What time are you thinking?", hour: "17:04"},
//                    {user: "Other User", nickname:"ttt9",text: "I’ll be there from 10am so come on over whenever your ready.", hour: "17:04"}
//                ]
//            },
//            {day: "Today", messages: [{user: "You", text: "On my way!", hour: "12:08"}]}
//        ];
        $scope.selectedConversationId;
        $scope.inbox = [];
        $scope.messageHistory = [];
        $scope.selectConversation = function (conversationId) {
            console.log(conversationId);
            $scope.selectedConversationId = conversationId;
            $scope.getMessages($scope.selectedConversationId);
        }

        $scope.sendMessage = function (conversationId, message) {
            console.log("Sending message to: " + conversationId + " - " + message);
            $chat.sendMessage(conversationId, message).success(function (data) {
                console.log("OK Data: " + data);
                $rootScope.$broadcast("quickNotification", "Message: " + message + " Sent!");
                $scope.getMessages($scope.selectedConversationId);

            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with sending the message!" + data);
            });
        }

        $scope.getMessages = function (selectedConversationId) {

            $chat.getMessages(selectedConversationId).success(function (data) {
                $scope.messageHistory = data;

                console.log("OK Data: ");
                console.log(data);

                $rootScope.$broadcast("quickNotification", "messages retrieved!");


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all messages!" + data);
            });
        }

        $scope.getConnections = function () {

            $chat.getConnections().success(function (data) {
                console.log("OK Data: " + data);
                $rootScope.$broadcast("quickNotification", "connections retrieved!");


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all connections!" + data);
            });
        }

        $scope.getConversations = function () {

            $chat.getConversations().success(function (data) {
                $scope.inbox = data;
                console.log("Routes Param selectedConversation: "+$routeParams.selectedConversation);
                if($routeParams.selectedConversation){
                    $scope.selectedConversationId = $routeParams.selectedConversation;
                }else if($scope.inbox[0] && !$routeParams.selectedConversation){
                    $scope.selectedConversationId = $scope.inbox[0].conversation_id;
                    $scope.getMessages($scope.selectedConversationId);
                }
                console.log("OK Inbox Data: ");
                console.log(data);

                $rootScope.$broadcast("quickNotification", "messages retrieved!");


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all conversations!" + data);
            });
        }

        $scope.getConversations();
        
       // $scope.getConnections();

    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore','$routeParams', '$rootScope'];
    angular.module("codename").controller("messagesController", messagesController);

}());
