define(['app'], function (app) {

   app.controller('loginPageCtrl', function ($scope, $http,$location,commonFactory) {
    	$scope.submitOk = function() {
        	$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
        	$http.post("../views/submitCredentials",$scope.submitCredentials).success(function(department){
        		if(department != null && department!=""){    			
        				//location.assign("../resources/selectPeopleCasesActivities.html?projectCode='"+department+"'");
        			commonFactory.selectedDepartment=department;
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
    });
}); 
