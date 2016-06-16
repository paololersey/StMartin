define(['app'], function (app) {

   app.controller('loginPageCtrl',['$scope', '$http','$location','$modal','authenticationSvc','commonFactory','commonMethodFactory', function ($scope, $http,$location,$modal,authenticationSvc,commonFactory,commonMethodFactory) {
    	$scope.submitOk = function() {
        	$scope.submitCredentials = {"username": $scope.username, "password": $scope.password};
        	commonFactory.username=$scope.username;
        	authenticationSvc.login($scope.submitCredentials).then(function(data){
        		if(data != null && data!="" && data.department!=null){    			
        			commonFactory.selectedDepartment=data.department;
        			$location.path("/homePage");
        		}
        		else{
        			commonMethodFactory.openDialogMessage("Your username/password are wrong",null,'error');
        		}
    			
        	});
        	
        };
        
        $scope.submitChangePwd = function() {
            	$modal.open({
                    templateUrl: '/StMartin/root/view/dialog/changePasswordDialog.html',
                    controller: changePasswordController,
                    size: 'lg',
                    resolve: {
                      items: function () {
                    	  return new Object();
                      }
                    }
                  });
            };
        	
        
        var changePasswordController = function($scope, $http, $location, $modalInstance, commonFactory, commonMethodFactory, items){
        	$scope.changePassword = function(item){
        		var credentials={"username": item.password.userName, "password": item.password.oldPassword};
        		
        		if(item.password.newPassword!= item.password.newPasswordConfirm){
        			commonMethodFactory.openDialogMessage("The new password is not the same",null);
        		}
        		authenticationSvc.login(credentials).then(function(data){
            		if(data != null && data!="" && data.department!=null){    			
            			commonFactory.selectedDepartment=data.department;
            			authenticationSvc.updateOldPassword(credentials).then(function(result){
            				if(result.data.endDate!=null){
            					var newCredentials={"username":  item.password.userName, "password": item.password.newPassword};
                    			authenticationSvc.changePassword(newCredentials).then(function(data){
                    				commonMethodFactory.openDialogMessage("The password has been successfully changed",null,'confirm');
                    				$location.path("/homePage");
                    				$modalInstance.dismiss('cancel');	
                    			});
            				}
            				
            			});
            			
            		}
            		else{
            			commonMethodFactory.openDialogMessage("Your old password is wrong",null,'error');
            		}   			
            	});
        			
        	};
        	
        	$scope.submitCancel = function() {
        		$modalInstance.dismiss('cancel');	
            };
        }
        
        $scope.submitCancel = function() {
        	$scope.password=null;
        	$scope.username=null;
        };
    }]);
}); 
