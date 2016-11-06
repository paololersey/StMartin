define(['angularAMD'], function (angularAMD) {
	angularAMD.factory('commonMethodFactory',['$http','$modal', function($http,$modal){
    	return {
	    	openDialogMessage: function(message, callbackFunction, type) {
				var modalInstance = $modal.open({
				    templateUrl: 'view/dialog/'+type+'Dialog.html',
				    controller: 'messageDialogController',
				    windowClass: "'"+type+"-dialog'",
				    resolve: {
				        message: function () {
				            return message;
				        }
				    }
			  	});
	
				modalInstance.result.then(function() {
				    if (callbackFunction !== undefined && callbackFunction !== null) {
				        callbackFunction();
				    }
				});
			},
			getPeopleList: function(projectPerson) {
				return $http.post('../views/listaBen', projectPerson);
			},
			getActivityList: function(filterActivity){
				return $http.post('../views/activityList', filterActivity);
			},
			getPaymentList: function(filterPayment) {
				return $http.post('../views/paymentList', filterPayment);
			},
			getNatureOfCasePersonList: function(filterNatureOfCase) {
				return $http.post('../views/natureOfCasePersonList', filterNatureOfCase);
			}
    	};
    }]);
angularAMD.controller('messageDialogController', ['$scope', '$modalInstance','message', function($scope, $modalInstance, message) {
	$scope.testo=message;
	$scope.ok = function () {
		   $modalInstance.close("ok");
		};
		$scope.cancel = function () {
		   $modalInstance.dismiss("cancel");
		};
}]);


});
