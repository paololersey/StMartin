define(['angularAMD'], function (angularAMD) {
	angularAMD.factory('authenticationSvc',['$http','$q', '$window', function($http, $q, $window) {
 	   var userInfo;

 	   
 	   function login(credentials) {
 	     var deferred = $q.defer();

 	     $http.post("../views/submitCredentials",credentials).then(function(result) {
 	       userInfo = {
 	    		 accessToken: result.data.accessToken,
 	 	         userName: result.data.username,
 	 	         department: result.data.department
 	       };
 	       $window.sessionStorage["userInfo"] = JSON.stringify(userInfo);
 	       deferred.resolve(userInfo);
 	       return userInfo;
 	     }, function(error) {
 	       deferred.reject(error);
 	     });

 	     return deferred.promise;
 	   }
 	   
 	   
 	  function logout(accessTokenInSession) {
 		 var deferred = $q.defer();

 		 var userInfoinSessionJson= sessionStorage.getItem('userInfo');
 		 var userInfoinSession = $.parseJSON(userInfoinSessionJson); 
 		 if(userInfoinSession.accessToken == accessTokenInSession){
 		   $window.sessionStorage["userInfo"] = null;
 		   userInfo = null;
 		 }
 		 else{			
 		   alert("The session ended abnormally: please login again");
 		 }
 		 deferred.resolve(null);
 		 return deferred.promise;
// 		  $http({
// 		    method: "POST",
// 		    url: logoutUrl,
// 		    headers: {
// 		      "access_token": userInfo.accessToken
// 		    }
// 		  }).then(function(result) {
// 		    $window.sessionStorage["userInfo"] = null;
// 		    userInfo = null;
// 		    deferred.resolve(result);
// 		  }, function(error) {
// 		    deferred.reject(error);
// 		  });		  
 		}
 	   
 	   function getUserInfo() {
 	 	    return userInfo;
 	   }

 	   
 	  function changePassword(credentials) {
  	     var deferred = $q.defer();

  	     $http.post("../views/changePassword",credentials).then(function(result) {
  	       deferred.resolve(result);
  	       return result;
  	     }, function(error) {
  	       deferred.reject(error);
  	     });

  	     return deferred.promise;
  	   }
 	  
 	  function updateOldPassword(credentials) {
  	     var deferred = $q.defer();

  	     $http.post("../views/updateOldPassword",credentials).then(function(result) {
  	       deferred.resolve(result);
  	       return result;
  	     }, function(error) {
  	       deferred.reject(error);
  	     });

  	     return deferred.promise;
  	   }
  	   
 	   return {
 	     login: login,
 	     logout: logout,
 	     getUserInfo: getUserInfo,
 	     changePassword: changePassword,
 	     updateOldPassword: updateOldPassword
 	   };
 	 }]);
});
    