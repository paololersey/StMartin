'use strict';




define(['app'], function (app) {
    app.controller('HomePageCtrl', ['$scope', '$http','$location','commonFactory', 'commonMethodFactory',function ($scope, $http,$location,commonFactory, commonMethodFactory) {
    	$scope.home={};
    	if(commonFactory.selectedDepartment =='CPPD'){
        	$scope.home.isCPPD= true;
        }
        if(commonFactory.selectedDepartment =='CPHA'){
        	$scope.home.isCPHA = true;
        }
        
    	$scope.setSelection = function(code){
    		commonFactory.codePage=code;
    		if(code==="PEO"){
    			$location.path("/peoplePage");
    		//	location.assign("../resources/people.html?projectCode='"+projectCode+"'&selectionCode='"+code+"' ");	
    		}
    		else if(code==="ACT"){
    			$location.path("/activitiesPage");
    		}
    		else if(code==="APP"){
    			$location.path("/appliancesPage");
    		}
    		else if(code==="NOF"){
    			$location.path("/natureOfCasePage");
    		}
    		else if(code==="PAY"){
    			$location.path("/paymentPage");
    		}
    		else{
    			commonMethodFactory.openDialogMessage("This code is not defined");
    		}
    			
    	};
    }]);
}); 

