require.config({
    baseUrl: "js",    
    paths: {
        'angular': 'angular/1.2.16/angular.min',
        'angular-route': 'angular/angular-route.min',
        'angularAMD': 'angular/angularAMD.min',   
        'ui-bootstrap': 'ang-ui-bootstrap/ui-bootstrap-tpls-0.11.0.min',
        'jquery': 'jquery/1.8/jquery',       
        'ng-grid': 'ng-grid/2.0.11/ng-grid-2.0.11.debug',
        'datePicker':'datePicker'
    },
    shim: { 'angularAMD': ['angular'], 
    	'angular-route': ['angular'],
    	'ui-bootstrap': ['angular'],
    	'jquery':['angular'],
    	'ng-grid': ['angular','jquery'],
    	 'datePicker':['angular']},
    deps: ['app']
});
