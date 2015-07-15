(function () {
    var $users = function ($http, $cookieStore, $transformRequestToForm, Upload, appConstants) {
        var factory = {};
        //SIGNUP
        factory.signup = function (email, password) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/register',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: email, password: password}
            })
        }
        //LOGOUT
        factory.logout = function () {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/logout',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
            });
        };
        //LOGIN
        factory.login = function (user) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/login',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {email: user.email, password: user.password}
            });
        };
        
        //LOGIN
        factory.loginExternal = function () {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/auth/loginexternal',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };

        factory.updateIam = function (selectedIams) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + "/iam/update",
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {iams: JSON.stringify(selectedIams)}
            })
        };

        factory.updateInterests = function (selectedInterests) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + "/interests/update",
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {interests: JSON.stringify(selectedInterests)}
            })
        };
        factory.updateCategories = function (categories) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + "/categories/update",
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {categories: JSON.stringify(categories)}
            })
        };
        factory.updateLookingForAndIams = function (lookingfor, iams) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + "/lookingforandiams/update",
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {lookingfor: JSON.stringify(lookingfor), iams: JSON.stringify(iams) }
            })
        };
        //CREATE Update First Login
        factory.updateUserFirstLogin = function () {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/firstlogin/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            })
        };
        //CREATE Update Live profile
        factory.updateUserLiveProfile = function (live) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/live/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {live: live}
            })
        };
        //GET PROFILE
        factory.getUserData = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id'),
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        
        
        
        //GET Public PROFILE
        factory.getPublicUserData = function (nickname) {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/public/users/' + nickname,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        
        factory.initMockUsers = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/public/app/init',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        //GET ALL
        factory.loadAllLive = function () {
            return $http({
                method: 'GET',
                url: appConstants.server + appConstants.context + 'rest/users/alllive',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {}
            });
        };
        //UPDATE User Data
        factory.updateUserData = function (firstname, lastname, location, bio, title) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {firstname: firstname, lastname: lastname, location: location, bio: bio, title: title}
            });
        };

        //UPDATE User Data
        factory.updateBothNames = function (firstname, lastname) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/bothnames/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {firstname: firstname, lastname: lastname}
            });
        };
        
        //UPDATE User Data
        factory.updateBioLongBioIams = function (bio, longbio, iams) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/biolongbioiams/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {bio: bio, longbio: longbio, iams: JSON.stringify(iams) }
            });
        };

        //UPDATE User Data
        factory.updateOriginallyFrom = function (originallyfrom) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/originallyfrom/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {originallyfrom: originallyfrom}
            });
        };

        //UPDATE User Data
        factory.updateJobTitle = function (jobtitle) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/jobtitle/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {jobtitle: jobtitle},
            });
        };
        
        //UPDATE User Data
        factory.updateAdvice = function (advice) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/advice/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {advice: advice},
            });
        };
        
        //UPDATE User Data
        factory.updateHobbies = function (hobbies) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/hobbies/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {hobbies: hobbies},
            });
        };
        
        //UPDATE User Data
        factory.updateShare = function (share) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/share/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {share: share},
            });
        };
        
        //UPDATE User Data
        factory.updateMessageMe = function (messageme) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/messageme/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {messageme: messageme},
            });
        };
        
        //UPDATE User Data
        factory.updateResources = function (resources) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/resources/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {resources: resources},
            });
        };

        factory.updateTwitter = function (twitter) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/twitter/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {twitter: twitter},
            });
        };

        factory.updateLinkedin = function (linkedin) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/linkedin/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {linkedin: linkedin},
            });
        };

        factory.updateWebsite = function (website) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/website/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {website: website},
            });
        };

        //UPDATE User Bio
        factory.updateBio = function (bio) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/bio/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {bio: bio},
            });
        };

        //UPDATE User Long Bio
        factory.updateLongBio = function (longbio) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/longbio/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {longbio: longbio},
            });
        };



        //UPDATE User Location
        factory.updateLocation = function (location, lon, lat) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/location/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {location: location, lon: lon, lat: lat},
            });
        };

        //UPDATE User Last Name
        factory.updateLastName = function (lastname) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/lastname/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {lastname: lastname}
            });
        };

        //UPDATE User  First Name
        factory.updateFirstName = function (firstname) {
            return $http({
                method: 'POST',
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/firstname/update',
                headers: {'Content-Type': 'application/x-www-form-urlencoded', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                transformRequest: $transformRequestToForm.transformRequest,
                data: {firstname: firstname}
            });
        };
        //UPLOAD AVATAR
        factory.uploadAvatar = function (file) {
            return Upload.upload({
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/avatar/upload',
                method: 'POST',
                headers: {'Content-Type': 'multipart/form-data', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                file: file
            })
        };

        //UPLOAD COVER
        factory.uploadCover = function (file) {
            return Upload.upload({
                url: appConstants.server + appConstants.context + 'rest/users/' + $cookieStore.get('user_id') + '/cover/upload',
                method: 'POST',
                headers: {'Content-Type': 'multipart/form-data', service_key: 'webkey:' + $cookieStore.get('email'), auth_token: $cookieStore.get('auth_token')},
                file: file,
            })
        };


        return factory;
    };

    $users.$inject = ['$http', '$cookieStore', '$transformRequestToForm', 'Upload', 'appConstants'];
    angular.module("codename").factory("$users", $users);

}());