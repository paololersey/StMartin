define(['angularAMD'], function (angularAMD) {
var userModule= angular.module("userModule", ["ngRoute"])
.config(["$routeProvider", function($routeProvider) {
	$routeProvider.when(
    "/", angularAMD.route({
         templateUrl: 'user/view/authentication.html', controller: 'authenticationController',controllerUrl: 'user/controllers/authenticationController' 
     }));
}]);
});
