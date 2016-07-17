'use strict';

var type;
define(['app'], function (app) {
	app.controller('PaymentPageCtrl',['$scope', '$http', '$modal', '$log', '$location', 'commonFactory','commonMethodFactory', function($scope, $http, $modal, $log, $location, commonFactory, commonMethodFactory) {

	
           var projectCode = commonFactory.selectedDepartment;
           //navigation check: if I refresh the page the code won't be available, so I send to to the login page
//           if(!projectCode){
//        	   $location.path("/loginPage");
//        	   return;
//           }
           
           setDepartment();
           
/**********************************************************************************************************************************************/
 // This function gets the nature of payment type

           $scope.projectPerson = {"projectCode" : projectCode,
                   "personCode"  : 'BE'
                  };
           $http.post('../views/natureOfPaymentList',$scope.projectPerson).success(function(data) {
				$scope.natureOfPaymentList=data;
			 });
           
           
           
          
           $scope.selectFilter=selectFilter;
           selectFilter(null,null,null,null);
           $http.post('../views/listaBen', $scope.projectPerson).success(
					function(data) {
						$scope.beneficiaries = data;
						 $scope.selectFilter(null);
					});
	
/**********************************************************************************************************************************************/
// This function is for filtering beneficiary, person in charge, and period
           
			
		   	
			 function selectFilter(selectedPerson, dateStartPeriod, dateEndPeriod, natureOfPaymentParam) {	
				  
				 var selectedBeneficiary=null;
				    var dateStart=null;
				    var dateEnd=null;
				    var natureOfPayment=null;
				    if(selectedPerson==null)selectedBeneficiary = null;
				    if(selectedPerson!=null && selectedPerson!='' && selectedPerson.personId>0) {
				    	selectedBeneficiary = selectedPerson.personId;
				    }
				    
                    if(dateStartPeriod!='') dateStart= dateStartPeriod;
                    if(dateEndPeriod!='') dateEnd= dateEndPeriod;
                    if(natureOfPaymentParam!='') natureOfPayment=natureOfPaymentParam;
					
					var filter = {"personIdBeneficiary":selectedBeneficiary,
							              "dateStart": dateStart,
							              "dateEnd": dateEnd,
							              "natureOfPayment": natureOfPayment,
							              "projectCode":projectCode};
					$http.post('../views/paymentList', filter).success(
							function(data) {						
								$scope.paymentList = data;		
								commonFactory.paymentData = data;
							});		
			};
			
/**********************************************************************************************************************************************/
// These functions regards the grid row configuration for the activity form
		
			$scope.mySelections = [];

			$scope.paymentList={};

			$scope.totalServerItems = 0;
			$scope.pagingOptions = {
				        pageSizes: [10, 50, 100, 200],
				        pageSize: 10,
				        currentPage: 1
			};	

			 
			$scope.setPagingData = function(data, page, pageSize) {	
			        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			        $scope.paymentList = pagedData;
			        $scope.totalServerItems = data.length;
			        if (!$scope.$$phase) {
			            $scope.$apply(); 
			        }
			    };
			
		    $scope.$watch('pagingOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {	
		        	$scope.setPagingData(commonFactory.paymentData,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);        
		        }
		    }, true);
			    
			$scope.$watch('filterOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal) {
		        	 var data = commonFactory.paymentData.filter(function(item) {
		                 return JSON.stringify(item).toLowerCase().indexOf($scope.filterOptions.filterText.toLowerCase()) != -1;
		             });
		        	$scope.setPagingData(data,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
		        }
			}, true);

			$scope.gridOptions = {
				data : 'paymentList',
				enableCellEdit : false,
				enableRowSelection : true,
				enableCellSelection: false,
				enablePaging : true,
				enableColumnResize :true,
				totalServerItems: 'totalServerItems',
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions : $scope.filterOptions,
				columnDefs : [ {
					field : 'beneficiary',
					displayName : 'Beneficiary'
				},{
					field : 'natureOfPayment',
					displayName : 'Nature Of Payment'
				}, {
					field : 'paymentDate',
					displayName : 'Payment Date',
					cellFilter: "date:'dd-MM-yyyy'"
				},{
					field : 'amount',
					displayName : 'Amount',
					cellFilter: "currency"
				}, {
					field : 'status',
					displayName : 'Status'
				}],
				
				showFooter : true
			};
/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// These functions regards the grid row selection
						
						$scope.backButton = function(size) {
				    		$location.path("/homePage");
				    	};
						$scope.deleteRowButton = function(size) {
							type="delete";
							if ($scope.mySelections[0] == null
									|| $scope.mySelections[0] == "") {
								alert("No case has been selected!");
							} else {
								type="delete";
								openModal(size);					
							}
						};

						// ROW INSERT
						$scope.insertRowButton = function(size) {
							type="insert";
							openModal(size);			 				
						};
	
				  
/**********************************************************************************************************************************************/
// This function opens a modal dialog related to the grid data, and initializes the form values			  

				  function openModal(size){
					  
					  var modalContent= '/StMartin/root/view/dialog/insertUpdatePaymentDialog.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: natureOfPaymentDialogController,
					        windowClass: 'app-modal-window',
					        resolve: {
					          items: function () {
					        	  
					        	  setDepartment();
								  		        	 
							      $http.post('../views/listaBen', $scope.projectPerson).success(
													function(data) {
														$scope.beneficiaries = data;
													});								
					        	  if(type == "insert"){							
					        		
					        		return {"date":null, "natureOfPaymentList": $scope.natureOfPaymentList, 
				  						    "beneficiaries":$scope.beneficiaries,"isCPPD":$scope.isCPPD,"isCPPR":$scope.isCPPR};				  				
					  				
					        	  }
					        	    
					        	  else{
					        		  $scope.mySelections[0].natureOfPayment=null;
					        		  return {"payment": $scope.mySelections[0]};					        		  
					        	  }
					        	  
					          }
					        }
					      });
					}

/**********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog and it is called when pressed ok or cancel after filling the form		  

				  
				  var natureOfPaymentDialogController = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  
					  
					  $scope.ok = function () {		
						$scope.$$childTail.items.payment.paymentDate = $scope.$$childTail.items.date;
						$scope.$$childTail.items.payment.amount = $scope.$$childTail.items.amount;
						update($scope.$$childTail.items);
						$modalInstance.dismiss('cancel');				    
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');					
					  };
					  
					  
					 
					};

/*********************************************************************************************************************************************/
//This Function is called when I click ok on the form, and it handles INSERT,UPDATE, AND DELETE of a record of the table activity
				function update($newScope) {	
					$scope.payment = $newScope.payment;
					var filter={'personType':$scope.projectPerson.personCode,
						    'projectCode':$scope.projectPerson.projectCode};
							// delete
							if( !$scope.payment.amount  &&  !$scope.payment.paymentDate && $scope.payment.paymentId){
								delete $scope.payment.beneficiary;
								$http({
									url : '../views/deletePayment',
									method : 'POST',
									data : $scope.payment
								}).success(function(data) {
									$scope.messages = data.messages;
									commonMethodFactory.getPaymentList(filter).then(function(result){
										$scope.paymentList=result.data;
										commonMethodFactory.openDialogMessage("The payment has been deleted",null,'confirm');
									});
									
								});
							}
							// insert or update
							else{	
								$scope.beneficiary = $newScope.objectBEN;
							    var globalPerson = {
							    	payment : $scope.payment,
							     	person : $scope.beneficiary,
							     	projectPerson: $scope.projectPerson
							    };

							$http({
								url : '../views/insertPayment',
								method : 'POST',
								data : globalPerson
							}).success(function(data) {
								$scope.messages = data.messages;
								commonMethodFactory.getPaymentList(filter).then(function(result){
									$scope.paymentList=result.data;
									commonMethodFactory.openDialogMessage("The payment has been added",null,'confirm');
								});
								
							});
					   }
				};
/*********************************************************************************************************************************************/	
			function setDepartment(){
				$scope.isCPPD = false;	
	        	  $scope.isCPPR = false;
				  if (projectCode=='CPPD') $scope.isCPPD = true;
				  if (projectCode=='CPPR') $scope.isCPPR = true;
			}	
	}]);
});					
