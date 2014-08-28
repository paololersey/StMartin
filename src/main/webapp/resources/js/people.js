'use strict';

/* Controllers */

var app = angular.module('app', ['ngGrid', 'ui.bootstrap']);

var type;
app.controller('StMartinPrograms',
		function($scope, $http, $modal,$log) {
	
	       var personType="BE";	
           var url = window.location.toString();
           url.match(/\?(.+)$/);
           var params= RegExp.$1;
           
           var startIndex = params.indexOf("=");
           var projectCode = params.substring(startIndex+4,startIndex+8);
           var lastIndex = params.lastIndexOf("=");
           var selectionCode = params.substring(lastIndex+4,lastIndex+7);
           
/**********************************************************************************************************************************************/
// These functions regards the tab selection 
           
           $scope.personInChargeTypeList = ['VO','SW','PH','CM'];
           $scope.beneficiariesSelected=function(){
        	   initialSettings("BE"); 
           };
           $scope.volunteersSelected=function(){
        	   initialSettings("VO");     	 
           };
           $scope.socialWorkersSelected=function(){
        	   initialSettings("SW");   
           };           
           $scope.physioterapistsSelected=function(){
        	   initialSettings("PH");   
           };        
           $scope.centerManagersSelected=function(){
        	   initialSettings("CM");   
           };

           function resetFormSearch(){
        	   $("#dateStart").attr("value",null);
        	   $("#dateEnd").attr("value",null);
        	   $("#idPCH").attr("value",null);
           }
           
           setFlags();
           
			

/********************************************************************************************************************************************/
// These functions get info about villages/cities, zones, state of the person (active/inactive)
        	
			// HTTP services
			$http.post('../views/citiesList',projectCode).success(function(data) {
					$scope.citiesList=data;								 	
				 });
        	
        	$http.post('../views/zonesList', projectCode).success(function(data) {
				$scope.zones=data;	
				var arrayZones=[];
				for (var i=0;i<$scope.zones.length;i++ ){					
					arrayZones.push($scope.zones[i].zoneCode);
				}
				$scope.zoneCodes=arrayZones;
			 });
        	
        	$http.post('../views/statesList',projectCode).success(function(data) {				
				$scope.personStateNames=data;
			 });
        	
            $http.post('../views/volunteerTypeList',projectCode).success(function(data) {			
				$scope.volunteerTypeList=data;
			 });
        	
        	$http.post('../views/majorTrainingList', projectCode).success(function(data) {          				
  	          $scope.majorTrainingList=data;
        	});
        	
        	
			

/*********************************************************************************************************************************************/
// This function is for filtering the beneficiaries according to person in charge, and the time period
        
	    var selectedPersonIncharge=null;
	    var dateStart=null;
	    var dateEnd=null;
	   	var zone=null;	
	   	var status=null;
	   	var majorTraining=null;
	   	var volunteerType=null;
	   	var contactPersonParam=null;
	   	
		$scope.selectFilter = function(selectedPerson, dateStartPeriod, dateEndPeriod, zoneParam, statusParam,
				                      majorTrainingParam,volunteerTypeParam, contactPersonParam) {	
								
			if(selectedPerson==null)selectedPersonIncharge = null;
		    if(selectedPerson!=null && selectedPerson!='' && selectedPerson.personId>0) {
		    	selectedPersonIncharge = selectedPerson.personId;
		    }
		    if(dateStartPeriod!='') dateStart= dateStartPeriod;
                if(dateEndPeriod!='') dateEnd= dateEndPeriod;
                if(zoneParam!='') zone= zoneParam;
                if(statusParam!='') status= statusParam;
                if(majorTrainingParam!='') majorTraining= majorTrainingParam;
                if(volunteerTypeParam!='') volunteerType= volunteerTypeParam;
				if(contactPersonParam!=null) contactPerson= contactPersonParam;
				var filter = {"personIdPersonInCharge":selectedPersonIncharge,
						              "dateStart": dateStart,
						              "dateEnd": dateEnd,
						              "personType": personType,
						              "projectCode": projectCode,
						              "zone": zone,
						              "status": status,
						              "majorTraining":majorTraining,
						              "volunteerType":volunteerType,
						              "contactPerson":contactPerson};
				
				$http.post('../views/beneficiarySeen', filter).success(
						function(data) {
							$scope.personData = data;
						});		
		};
/*********************************************************************************************************************************************/
// This function is for filtering active people

		$scope.filterActiveInactive = function(){
			var dataToPost = {"activePerson":$scope.radioModel, "personType":personType};
			$http({
				url : '../views/filterActiveInactive',
				method : 'POST',
				data :  dataToPost
			}).success(function(data) {
				$scope.personData = data;
			});
		};
/*********************************************************************************************************************************************/
		
	
		
			$scope.projectPerson = {
			    "projectCode" : projectCode,
				"personCode" : "BE"
			};

			/** FUNZIONE PER FARE LA PERSISTENZA SUL BACKEND */
			function update($newScope) {
				$scope.personData = $newScope;
				// sono in delete
				if(!$newScope.firstName && $newScope.personId){
					$http({
						url : '../views/deletePerson',
						method : 'POST',
						data : $scope.personData
					}).success(function(data) {
										
						alert("cancelled!");
						location.reload();
					});
				}
				else{
				var arrayData = {
					person : $scope.personData,
					projectPerson : $scope.projectPerson
				};

				$http({
					url : '../views/inserisciBen',
					method : 'POST',
					data : arrayData
				}).success(function(data) {
					if(data!=null && data!=""){
					   openErrorDialog(data);
					   }
					else{
						alert("Insert/update succeeded");
						location.reload();
					}
				});
			  }
			};



			// NG-GRID conf. options

			$scope.mySelections = [];

			$scope.filterOptions = {
				filterText : ''
			};

			$scope.gridOptions = {
				data : 'personData',
				enableCellEdit : false,
				enableRowSelection : true,
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				selectedItems : $scope.mySelections,
				pagingOptions : $scope.pagingOptions,
				filterOptions : $scope.filterOptions,
				columnDefs : [ {
					field : 'firstName',
					displayName : 'Name'
				}, {
					field : 'lastName',
					displayName : 'Surname'
				}, {
					field : 'thirdName',
					displayName : 'Thirdname'
				},{
					field : 'gender',
					displayName : 'M/F',
					width: 50
				},  {
					field : 'parentGuardian',
					displayName : 'Parent/Guardian'
				}, {
					field : 'zone',
					displayName : 'Zone'
				} ,{
					field : 'village',
					displayName : 'Village'
				},{
					field : 'dateOfBirth',
					displayName : 'Date of Birth',
					cellFilter: "date:'dd-MM-yyyy'"
				}, {
					field : 'telephone',
					displayName : 'Tel.'
				}, {
					field : 'fileNumber',
					displayName : 'File Number',
					width:150
				},{
					field : 'email',
					displayName : 'E-mail'
				},{
					field : 'state',
					displayName : 'Status'
				}],
				enablePaging : true,
				showFooter : true
			};
			// $scope.reset();

			$scope.pagingOptions = {
				pageSizes : [ 250, 500, 1000 ],
				pageSize : 250,
				currentPage : 1
			};
			$scope.setPagingData = function(data, page, pageSize) {
				var pagedData = data.slice((page - 1) * pageSize, page
						* pageSize);
				$scope.myData = pagedData;
				$scope.totalServerItems = data.length;
				if (!$scope.$$phase) {
					$scope.$apply();
				}
			};
		
/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// This functions retrieve the records of current type of person selected 	
	    	function personTypeFunction(personTypeParameter) {
	    		$scope.projectPerson = {
	    			    "projectCode" : projectCode,
	    				"personCode"  : personTypeParameter
	    			};
	    		$http.post('../views/listaBen', $scope.projectPerson).success(
	    				function(data) {
	    					$scope.personData = data;      
	    				});	    		
	    	}
	    	                   
	    	
	    	 
/**********************************************************************************************************************************************/	    	
// This functions retrieve the records of personInCharge related to a beneficiary

	    	$scope.selectPersonType = function (personTypeSelected){
	    		$scope.projectPerson = {
	    			    "projectCode" : projectCode,
	    				"personCode"  : personTypeSelected
	    			};
	    		$http.post('../views/listaBen', $scope.projectPerson).success(
	    				function(data) {
	    					$scope.personInCharge = data;      
	    				});
	    	};
	    	
/**********************************************************************************************************************************************/	    	
// These functions regards back button and buttons for the grid operations
	    	$scope.backButton = function(size) {
	    		location.assign("../resources/selectPeopleCasesActivities.html?projectCode='"+projectCode+"'");
	    	};
			$scope.selectRowButton = function(size) {
				type="modify";
				if ($scope.mySelections[0] == null
						|| $scope.mySelections[0] == "") {
					alert("No person has been selected!");
				} else {					
					openModal(size);					
				}
				$scope.personData = $scope.mySelections[0];
			};

			$scope.deleteRowButton = function(size) {
				type="delete";
				if ($scope.mySelections[0] == null
						|| $scope.mySelections[0] == "") {
					alert("No person has been selected!");
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
// This function opens a modal dialog and initializes the form values					 
			function openModal(size){
					  var modalContent= 'myModalContent.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						$modal.open({
					        templateUrl: modalContent,
					        controller: ModalInstanceCtrlUpdate,
					        windowClass: 'app-modal-window',
					        size: size,
					        resolve: {
					          
					          items: function () {
					        	 setFlags();
					        	  
					        	  if(type == "insert"){
					        		
					        		$("#personId").attr("value", null);
					  				$("#firstNameId").attr("value", null);
					  				$("#lastNameId").attr("value", null);
					  				$("#cityId").attr("value", null);
					  				$("#dateOfBirth").attr("value", null);
					  				
					  				 
					  				var array = {"people": null, "cities": $scope.citiesList, "zones":$scope.zoneCodes, "personState": $scope.personStateNames,
					  						    "date":null, "volunteerTypeList":$scope.volunteerTypeList,"isVolunteer": $scope.isVolunteer,
					  						    "isBeneficiary": $scope.isBeneficiary,"isBeneficiaryNotCPPR": $scope.isBeneficiaryNotCPPR,"isVolunteerNotCPPR": $scope.isVolunteerNotCPPR, "isCPPR": $scope.isCPPR,"isCPPRBeneficiary": $scope.isCPPRBeneficiary,
					  						    "majorTrainingList": $scope.majorTrainingList};
					  				return array;
					  				
					        	  }
					        	  else if (type == "modify"){
					        		$("#personId").attr("value",$scope.mySelections[0].personId);	
						            if($scope.mySelections[0].personState==='A'){
						            	$scope.personState = 'ACTIVE';
						            }
						            return {"people": $scope.mySelections[0], "zones":$scope.zoneCodes, "personState": $scope.personStateNames, "isVolunteer": $scope.isVolunteer,
						            	    "cities": $scope.citiesList, "date":$scope.mySelections[0].dateOfBirth,"volunteerTypeList":$scope.volunteerTypeList,
						            	    "isBeneficiary": $scope.isBeneficiary,"isBeneficiaryNotCPPR": $scope.isBeneficiaryNotCPPR,"isCPPR": $scope.isCPPR,"isCPPRBeneficiary": $scope.isCPPRBeneficiary,"isVolunteerNotCPPR": $scope.isVolunteerNotCPPR,
						            	    "majorTrainingList": $scope.majorTrainingList};
						            
					        	  }	  
					        	  else{
					        		  $scope.mySelections[0].firstName=null;
					        		  $("#personId").attr("value",$scope.mySelections[0].personId);
					        		  return {"people": $scope.mySelections[0]};					        		  
					        	  }
					        	 
					        	  
								 
					          }
					        }
					      });
						
						

					}

/**********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog and it is called when pressed ok or cancel after filling the form		  
				  
			var ModalInstanceCtrlUpdate = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  $scope.ok = function () {
						$scope.$$childTail.items.people.dateOfBirth = $scope.$$childTail.items.date;
						update($scope.$$childTail.items.people);
						$modalInstance.dismiss('cancel');
					    
					  };

					  $scope.cancel = function () {
					    $modalInstance.dismiss('cancel');
					    location.reload();
					  };
					 
			};
			
/**********************************************************************************************************************************************/
// Open error dialog	
			function openErrorDialog(code){
				$modal.open({
					templateUrl: 'errorDialog.html',
					controller: ModalErrorDialog,
			    	size: "",
					resolve: {		          
				          items: function () {
				        	  if(code=="fileNumberError"){
				        		  $scope.error = "This file number is already present!";
				        	  }
				        	  if(code=="threeNamesError" || "twoNamesVillagesError"){
				        		  $scope.error = "This person is already present!";
				        	  }
				        	  if(code=="threeNamesAnotherProgram"){
				        		  $scope.error = "This person is present in another program, we'll update it with the new infos!";
				        	  }
				        	  var error = {"error":$scope.error};
				        	  return error;
				          }
					}
				});			
			}
			
			var ModalErrorDialog= function ($scope, $modalInstance, items) {				  
				  
				$scope.items = items;	
				$scope.cancel = function () {
				    $modalInstance.dismiss('cancel');
				  };

		};		
/**********************************************************************************************************************************************/
// Report
			
		$scope.report = function(){
			$http.get("../pdf/createPdf").success(function(data){
				$scope.citiesList=data;	
			});
		};


/*********************************************************************************************************************************************/
// flag personType

		function setFlags(){
			$scope.isBeneficiaryNotCPPR=false;
			$scope.isVolunteerNotCPPR=false;
			$scope.isVolunteer=false;
			$scope.isCPPRBeneficiary=false;
			$scope.isBeneficiary=false;
			$scope.isCPPR=false;
			$scope.isCPPD=false;
			$scope.isCPCN=false;
			  
			  if(personType=="BE" && projectCode=="CPPR") {
				  $scope.isCPPRBeneficiary=true;
			  }
			  if(projectCode=="CPCN") {
				  $scope.isCPCN=true;
			  }
			  if(projectCode=="CPPR") {
				  $scope.isCPPR=true;
			  }
			  if(projectCode=="CPPD") {
				  $scope.isCPPD=true;
			  }
			  if(personType=="BE") {
				  $scope.isBeneficiary=true;
			  }
			  if(personType=="VO") {
				  $scope.isVolunteer=true;
			  }
			  if(personType=="VO" && projectCode!="CPPR") {
				  $scope.isVolunteerNotCPPR=true;
			  }
			  if(personType=="BE" && projectCode!="CPPR") {
				  $scope.isBeneficiaryNotCPPR=true;
			  }
			 
		}
		
	function initialSettings(personTypeParameter){
	   personType =personTypeParameter;
  	   personTypeFunction(personTypeParameter);  
  	   setFlags();
  	   resetFormSearch();
  	   $scope.selectFilter(null, null, null, null, null);
	}	
});