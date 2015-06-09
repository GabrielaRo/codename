(function () {
    var $interests = function($http, $transformRequestToForm, appConstants) {
        var factory = {};
        //LOAD ALL INTERESTS
        factory.loadAll = function(){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/public/interests/all',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        return factory;
    };
    $interests.$inject = ['$http', '$transformRequestToForm', 'appConstants'];
    angular.module("codename").factory("$interests",$interests);
}());