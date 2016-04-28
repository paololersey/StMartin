
define(['angularAMD'], function (angularAMD) {
angularAMD.controller('authenticationController',['$scope','authenticationSvc', 'commonFactory','$location', '$http', function($scope, authenticationSvc, commonFactory, $location, $http) {

	
	$scope.isAuthenticated=isAuthenticated;
	$scope.logout=logout;
	$scope.goToHomePage=goToHomePage;
	
	function isAuthenticated(){
		$scope.userAuth = authenticationSvc.getUserInfo();
		var userInfoinSessionJson = sessionStorage.getItem('userInfo');
        var userInfoinSession = $.parseJSON(userInfoinSessionJson);
		if($scope.userAuth==undefined || !userInfoinSession){
			$location.path("/loginPage");
			return false;
			
			
		}
		return true;
	}


    function goToHomePage() {
        $location.path('/homePage');
    };

    function logout(accessTokenInSession){
    	authenticationSvc.logout(accessTokenInSession).then(function(){   		   			
    		commonFactory.selectedDepartment=null;
    		$location.path("/loginPage");   			
    	});
    }
    
	}]);
});
