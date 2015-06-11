(function(){
    var $users = function($http, $cookieStore, $transformRequestToForm, $upload, appConstants){
        var factory = {};
        //SIGNUP
        factory.signup = function(email, password){
            return $http({
                    method: 'POST',
                    url: appConstants.server + appConstants.context + 'rest/auth/register',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    transformRequest: $transformRequestToForm.transformRequest,
                    data: {email: email, password: password}
                })
        }
        //LOGOUT
        factory.logout = function(){
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/logout',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };
        //LOGIN
        factory.login = function(user){
             return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/login',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + user.email},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: user.email, password: user.password}
            });
        };
        //LOAD INTEREST
        factory.loadInterests = function(){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/users/'+$cookieStore.get('user_id')+'/interests',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        }
        factory.updateInterests = function(selectedInterests){
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/'+$cookieStore.get('user_id')+"/interests/update",
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: { interests: JSON.stringify(selectedInterests)}
            })
        };
        //CREATE PROFILE
        factory.createProfile = function(){
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/new',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {user_id: $cookieStore.get('user_id')}
            })
        };
        //GET PROFILE
        factory.getProfile = function(){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        //GET ALL
        factory.loadAll = function(){
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/public/users/all',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        //UPDATE PROFILE
        factory.updateProfile = function(firstname, lastname, location, bio, title){
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {firstname: firstname, lastname: lastname, location: location, bio: bio, title: title},
            }); 
        };
        //UPLOAD AVATAR
        factory.uploadAvatar = function(file) {
            return $upload.upload({
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/avatar/upload',
                method: 'POST',
                headers: {'Content-Type': 'multipart/form-data', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                file: file,
            })
        };
        
        //UPLOAD COVER
        factory.uploadCover = function(file) {
            return $upload.upload({
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/cover/upload',
                method: 'POST',
                headers: {'Content-Type': 'multipart/form-data', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                file: file,
            })
        };
        
        
        return factory;
    };
    
    $users.$inject = ['$http','$cookieStore','$transformRequestToForm', '$upload', 'appConstants'];
    angular.module("codename").factory("$users",$users);
    
}());