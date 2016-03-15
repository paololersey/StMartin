define(['angularAMD', 'angular-route','ui-bootstrap', 'ng-grid', 'datePicker'  ], function (angularAMD) {
    var app = angular.module("StMartinModule", ['ngRoute','ngGrid','ui.bootstrap']);
    app.config(function ($routeProvider) {
        $routeProvider.when(
        	"/loginPage", angularAMD.route({
            templateUrl: 'view/loginPage.html', controller: 'loginPageCtrl',controllerUrl: 'loginPageController' 
            }))
            .when("/homePage", angularAMD.route({
            templateUrl: 'view/homePage.html', controller: 'HomePageCtrl',controllerUrl: 'homePageController' 
            }))
            .when("/peoplePage", angularAMD.route({
            templateUrl: 'view/peoplePage.html', controller: 'PeoplePageCtrl',controllerUrl: 'peoplePageController' 
            }))           
            .otherwise({redirectTo: "/loginPage"});
    	});
    // Before bootstrapping define common factory
    app.factory('commonFactory', ['$http',function ($http) {
		return {
			selectedDepartment : null,
			codePage : null
		};
   }]);
    return angularAMD.bootstrap(app);
});




