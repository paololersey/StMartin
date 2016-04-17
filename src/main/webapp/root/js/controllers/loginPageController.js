define(['app'], function (app) {

   app.controller('loginPageCtrl',['$scope', '$http','$location','authenticationSvc','commonFactory', function ($scope, $http,$location,authenticationSvc,commonFactory) {
    	$scope.submitOk = function() {
        	$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
        	authenticationSvc.login($scope.submitCredentials).then(function(data){
        		if(data != null && data!=""){    			
        			commonFactory.selectedDepartment=data.department;
        			$location.path("/homePage");
        		}
        		else{
        			alert ("Your username/password are wrong");
        		}
    			
        	});
        	
        };
        
        $scope.submitCancel = function() {
        	$scope.password=null;
        	$scope.username=null;
        };
    }]);
}); 
