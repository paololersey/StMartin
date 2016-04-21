define(['angularAMD'], function (angularAMD) {
	angularAMD.factory('commonFactory',['$http',function($http) {
		return {
			selectedDepartment : null,
			codePage : null,
			peopleData : null
		};
	}]);

	
	angularAMD.directive('navMenu', function () {
	    return {
	      restrict: 'A',
	      controller: 'navMenuController',
	      templateUrl: 'scripts/main/templates/nav.html'
	    };
	  });
});
