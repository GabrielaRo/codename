(function () {

    var messagesController = function ($scope, $chat, $cookieStore, $routeParams, $rootScope) {

       
        
        $scope.selectedConversationId;
        $scope.selectedUserName;
        $scope.inbox = [];
        $scope.messageHistory = [];

        $scope.selectConversation = function (conversationId, userName) {
            console.log(conversationId);
            $scope.selectedConversationId = conversationId;
            $scope.getMessages($scope.selectedConversationId);
            $scope.selectedUserName = userName;
        }



       
        $scope.sendMessage = function (conversationId, message) {
            console.log("Sending message to: " + conversationId + " - " + message);
            $chat.sendMessage(conversationId, message).success(function (data) {
                console.log("OK Data: " + data);
                $rootScope.$broadcast("quickNotification", "Message: " + message + " Sent!");
                $scope.getMessages($scope.selectedConversationId);
                $scope.newMessage = "";
                var newListHeight =  $(".messages-history").height();
                $("#user-messages-chat").animate({ scrollTop:  newListHeight }, 1000);

            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with sending the message!" + data);
                 $scope.newMessage = "";
            });
            
           
        }

        $scope.getMessages = function (selectedConversationId) {

            $chat.getMessages(selectedConversationId).success(function (data) {
                $scope.messageHistory = data;

                console.log("OK Data: ");
                console.log(data);

                $rootScope.$broadcast("quickNotification", "messages retrieved!");
                
                var scrollDown = function(){
                    var newListHeight =  $(".messages-history").height();
                    $("#user-messages-chat").scrollTop( newListHeight );
                };
                setTimeout(scrollDown, 200);


            }).error(function (data) {

                console.log("Error: " + data);
                console.log(data);
                $rootScope.$broadcast("quickNotification", "Something went wrong with getting all messages!" + data);
            });
        }

       

        $scope.getConversations = function () {

            $chat.getConversations().success(function (data) {
                $scope.inbox = data;
                console.log("Routes Param selectedConversation: " + $routeParams.selectedConversation);
                console.log($scope.inbox);
                if ($routeParams.selectedConversation) {
                    
                    $scope.selectConversation($routeParams.selectedConversation)
                    
                }else if($scope.inbox[0] && !$routeParams.selectedConversation){
                    $scope.selectedConversationId = $scope.inbox[0].conversation_id;
                    $scope.getMessages($scope.selectedConversationId);
                    $scope.selectedUserName = $scope.inbox[0].description;
                }
//                mySocket.emit('rooms:join', {roomId: $scope.selectedConversationId}, function (data) {
//                    console.log("ws data: ");
//                    console.log(data);
//                });
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


    };

    messagesController.$inject = ['$scope', '$chat', '$cookieStore', '$routeParams', '$rootScope'];
    angular.module("codename").controller("messagesController", messagesController);
    
    
}());
