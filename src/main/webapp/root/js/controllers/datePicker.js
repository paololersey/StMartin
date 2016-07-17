angular.module('datePicker', ['ui.bootstrap']);
var DatepickerDemoCtrl = function ($scope) {
  $scope.today = function() {
    $scope.date = new Date();
  };
  $scope.today();

  $scope.clear = function () {
    $scope.date = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return false;//( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
  };

  /*$scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();*/

  $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  $scope.initDate = new Date('2030-15-20');
  $scope.formats = ['dd/MM/yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate','yyyy-MM-dd HH:mm:ss Z'];
  $scope.format = $scope.formats[0];
};