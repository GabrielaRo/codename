(function () {
    var homeController = function ($scope, $rootScope, $users, appConstants) {
        $scope.imagePath = "static/img/public-images/";
        $scope.pageClass = "home full";
        $scope.formTabActive = "sign-in";
        
        $scope.tabClicked = function(tab, tabsForm){
            var form = $("#"+tabsForm);
            console.log(form);
            var formsTabs = form.find(".tab");
            var actualTab = $("#"+tab);
            if(tab == "sign-in"){
                $scope.formTabActive = tab;
            }else if(tab == "join") {
                $scope.formTabActive = tab;
            }
            
            formsTabs.each(function (index){
                $(this).removeClass("active");
            });
            actualTab.addClass("active");
        }
    

      

       

       

        if ($scope.auth_token && $scope.auth_token !== "") {

            console.log("loading private clubs because: " + $scope.auth_token);
           
            //
        } else {
            console.log("loading public clubs because: " + $scope.auth_token);
            

        }
        


        
    };

    homeController.$inject = ['$scope', '$rootScope', '$users', 'appConstants'];
    angular.module("codename").controller("homeController", homeController);

}());


