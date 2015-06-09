(function (){
    var $items = function($transformRequestToForm, $cookieStore, $http, $upload, appConstants){
        var factory = {};
        //POST ITEM
        factory.post = function(item){
            return $http({
                    method: 'POST',
                    url: appConstants.server + appConstants.context + 'rest/items/new',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                    transformRequest: $transformRequestToForm.transformRequest,
                    data: item
                })
        };
        //DELETE ITEM
        factory.remove = function(item_id){
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/items/'+item_id+'/remove',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {id: item_id}
            })
        };
        //UPLOAD IMAGE
        factory.uploadImage = function(item_id, file){
            return $upload.upload({
                url: appConstants.server + appConstants.context + 'rest/items/' + item_id + '/image/upload', 
                method: 'POST',
                headers: {'Content-Type': 'multipart/form-data', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                file: file,
            })
        };
         //LOAD SPECIFIC CLUB ITEMS
        factory.loadItems = function(club_id){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/items/club/' + club_id,
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        
         
        factory.loadPublicItems = function(club_id){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/public/items/club/' + club_id,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        
        factory.loadUserItems = function() {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/items/user/' + $cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        
        
        return factory;
    };
    
    $items.$inject = ['$transformRequestToForm', '$cookieStore', '$http', '$upload', 'appConstants'];
    angular.module( "codename" ).factory("$items",$items);
    
}());