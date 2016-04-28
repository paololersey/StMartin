define(['app'], function (app) {

   app.controller('loginPageCtrl',['$scope', '$http','$location','authenticationSvc','commonFactory','commonMethodFactory', function ($scope, $http,$location,authenticationSvc,commonFactory,commonMethodFactory) {
    	$scope.submitOk = function() {
        	$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
        	authenticationSvc.login($scope.submitCredentials).then(function(data){
        		if(data != null && data!="" && data.department!=null){    			
        			commonFactory.selectedDepartment=data.department;
        			$location.path("/homePage");
        		}
        		else{
        			commonMethodFactory.openDialogMessage("Your username/password are wrong",null);
        		}
    			
        	});
        	
        };
        
        $scope.submitCancel = function() {
        	$scope.password=null;
        	$scope.username=null;
        };
    }]);
}); 
