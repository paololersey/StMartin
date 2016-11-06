'use strict';

/* Controllers */
var type;
define(['app'], function (app) {
app.controller('PeoplePageCtrl',['$scope', '$http', '$modal', '$log', '$location', '$controller','commonFactory','commonMethodFactory', function($scope, $http, $modal, $log, $location, $controller, commonFactory,commonMethodFactory) {
	
	      // $.extend(this, $controller('ButtonsController', {$scope: $scope})); 
	       var personType="BE";	
	       // store department code
           var projectCode = commonFactory.selectedDepartment;
           //navigation check: if I refresh the page the code won't be available, so I send to to the login page
           if(!commonFactory.selectedDepartment){
        	   $location.path("/loginPage");
        	   return;
           }

           if(projectCode=='CPHA') {
        	   personType="OR";	
           }
           
           $scope.toUppercase=toUppercase;

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
           
           // CPHA beneficiaries
           $scope.orphansSelected=function(){
        	   initialSettings("OR");   
           };
           $scope.peopleLivingHIVSelected=function(){
        	   initialSettings("LH");   
           };
           $scope.recovereesSelected=function(){
        	   initialSettings("RE");   
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
        	
        	$http.post('../views/parishesList', projectCode).success(function(data) {          				
    	        var arrayParishes=[];
  				for (var i=0;i<data.length;i++ ){					
  					arrayParishes.push(data[i].parishCode);
  				}
  				$scope.parishes=arrayParishes;
          	});
        	
        	
        	
        	
        	$http.post('../views/supportGroupList', projectCode).success(function(data) {          				
  				var supportGroups=[];
  				for (var i=0;i<data.length;i++ ){					
  					supportGroups.push(data[i].supportGroupName);
  				}
  				$scope.supportGroups=supportGroups;
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
	   	var contactPerson=null;
	   	var filter = null;
	   	
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
				filter = {"personIdPersonInCharge":selectedPersonIncharge,
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
							commonFactory.peopleData=$scope.personData;
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
				commonFactory.peopleData=$scope.personData;
			});
		};
/*********************************************************************************************************************************************/
		
	
		
			$scope.projectPerson = {
			    "projectCode" : projectCode,
				"personCode" : personType
			};

			/** FUNZIONE PER FARE LA PERSISTENZA SUL BACKEND */
			function update($newScope) {
				$scope.personData = $newScope.people;
				// sono in delete
				if($newScope.people.endDate && $newScope.people.personId){
					$http({
						url : '../views/deletePerson',
						method : 'POST',
						data : $scope.personData
					}).success(function(data) {										
						alert("cancelled!");						
					});
				}
				else{
					var arrayData = {
						person : $scope.personData,
						projectPerson : $scope.projectPerson,
						siblingList: $newScope.siblingList
					};
	
					$http({
						url : '../views/inserisciBen',
						method : 'POST',
						data : arrayData
					}).success(function(data) {
						if(data!=null && data!=""){
						   openMessageDialog(data);
						}
						else{
							alert("Insert/update succeeded");			
						}
						commonMethodFactory.getPeopleList($scope.projectPerson).then(function(object) {
			    			$scope.personData = object.data;
			    			commonFactory.peopleData=$scope.personData;	
			    		});
					});
			  }
			};


/*********************************************************************************************************************************************/
			// NG-GRID conf. options

			$scope.mySelections = [];
		
			$scope.personData={};

			$scope.totalServerItems = 0;
			$scope.pagingOptions = {
				        pageSizes: [10, 50, 100, 200],
				        pageSize: 10,
				        currentPage: 1
			};	

			 
			$scope.setPagingData = function(data, page, pageSize) {	
			        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
			        $scope.personData = pagedData;
			        $scope.totalServerItems = data.length;
			        if (!$scope.$$phase) {
			            $scope.$apply(); 
			        }
			    };
			
		    $scope.$watch('pagingOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {	
		        	$scope.setPagingData(commonFactory.peopleData,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);        
		        }
		    }, true);
			    
			$scope.$watch('filterOptions', function (newVal, oldVal) {
		        if (newVal !== oldVal) {
		        	 var data = commonFactory.peopleData.filter(function(item) {
		                 return JSON.stringify(item).toLowerCase().indexOf($scope.filterOptions.filterText.toLowerCase()) != -1;
		             });
		        	$scope.setPagingData(data,$scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
		        }
			}, true);
			
			$scope.gridOptions = {
				data : 'personData',
				enableCellEdit : false,
				enableRowSelection : true,
				enablePaging : true,
				totalServerItems:'totalServerItems',
				multiSelect : false,
				showColumnMenu : true,
				showFilter : true,
				enableColumnResize :true,
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
				},
				{
					field : 'insertDate',
					displayName : 'Adm.date',
					cellFilter: "date:'dd-MM-yyyy'",
					width: 80
				},  {
					field : 'parentGuardian',
					displayName : 'Parent/Guardian',
					width: 80
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
				
				showFooter : true
			};
			// $scope.reset();
			
		
/**********************************************************************************************************************************************/		
/**********************************************************************************************************************************************/
// This functions retrieve the records of current type of person selected 	
	    	function selectPersonTypeFromTab(personTypeParameter) {
	    		commonFactory.personTypeSelectedFromTab=personTypeParameter;
	    		$scope.projectPerson = {
	    			    "projectCode" : projectCode,
	    				"personCode"  : personTypeParameter
	    			};
	    		commonMethodFactory.getPeopleList($scope.projectPerson).then(function(object) {
	    			$scope.personData = object.data;
	    			commonFactory.peopleData=$scope.personData;
	    		});		
	    	}
	    	                   
	    	
	    	 
/**********************************************************************************************************************************************/	    	
// This functions retrieve the records of personInCharge related to a beneficiary

	    	$scope.selectPersonType = function (personTypeSelected){
	    		$scope.projectPerson = {
	    			    "projectCode" : projectCode,
	    				"personCode"  : personTypeSelected
	    			};
	    		commonMethodFactory.getPeopleList($scope.projectPerson).then(function(object) {
	    				$scope.personInCharge = object.data;
	    		});	
	    	};
	    	
/**********************************************************************************************************************************************/	    	
// These functions regards back button and buttons for the grid operations
	    	$scope.backButton = function(size) {
	    		$location.path("/homePage");
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
					  //var modalContent= 'myModalContent.html';
				        var modalContent= '/StMartin/root/view/dialog/insertUpdatePeopleDialog.html';
					    if(type == "delete"){
					    	modalContent= 'myModalContentDelete.html';
					    }
						var modalOpen= $modal.open({
					        templateUrl: modalContent,
					        controller: peopleDialogController,
					        windowClass: 'app-modal-window',
					        size: size,
					        resolve: {
					          
					          items: function () {
					        	 setFlags();
					        	  
					        	  if(type == "insert" || type == "modify"){
					        		  var peopleData=null;
					        		  var dateOfBirthPerson=null;
					        		  if(type == "insert"){
					        			  $("#personId").attr("value", null);
							  				$("#firstNameId").attr("value", null);
							  				$("#lastNameId").attr("value", null);
							  				$("#cityId").attr("value", null);
							  				$("#dateOfBirth").attr("value", null); 
					        		  }
					        		  else{
					        			  $("#personId").attr("value",$scope.mySelections[0].personId);	
								            if($scope.mySelections[0].personState==='A'){
								            	$scope.personState = 'ACTIVE';
								            }
								            peopleData=$scope.mySelections[0];
								            dateOfBirthPerson=$scope.mySelections[0].dateOfBirth;
					        		  }		  				 
					  				  var array = {"people": peopleData, "cities": $scope.citiesList, "zones":$scope.zoneCodes,"parishes":$scope.parishes, "supportGroups":$scope.supportGroups, "personState": $scope.personStateNames,
					  						    "date":dateOfBirthPerson, "volunteerTypeList":$scope.volunteerTypeList,"isVolunteer": $scope.isVolunteer, 
					  						    "isBeneficiary": $scope.isBeneficiary,"isBeneficiaryNotCPPR": $scope.isBeneficiaryNotCPPR,"isVolunteerNotCPPR": $scope.isVolunteerNotCPPR, "isCPPR": $scope.isCPPR,"isCPPRBeneficiary": $scope.isCPPRBeneficiary,
					  						    "majorTrainingList": $scope.majorTrainingList, "isCPHA":$scope.isCPHA, "isCPHAOrphan":$scope.isCPHAOrphan, "isCPHAPlwhiv":$scope.isCPHAPlwhiv, "isCPHARecoveree":$scope.isCPHARecoveree, "isCPPD":$scope.isCPPD};
					  				  return array;
					  				
					        	  }
					        	  else{
					        		  $scope.mySelections[0].endDate=new Date();
					        		  $("#personId").attr("value",$scope.mySelections[0].personId);
					        		  return {"people": $scope.mySelections[0]};					        		  
					        	  }	 
					          }
					        }
					      });
						

						modalOpen.result.then(function (selectedItem) {
						      $scope.selected = selectedItem;
						    }, function () {
						    //  $log.info('Modal dismissed at: ' + new Date());
						    	commonMethodFactory.getPeopleList($scope.projectPerson).then(function(object) {
					    			$scope.personData = object.data;
					    			commonFactory.peopleData=$scope.personData;
					    		});
						});
						
						

					}

/**********************************************************************************************************************************************/
// This function defines a new controller for the modal dialog and it is called when pressed ok or cancel after filling the form		  
				  
			var peopleDialogController = function ($scope, $modalInstance, items) {
	  
					  $scope.items = items;
					  // sibling data when I enter into the update page
					  if(items.people!=null && items.people.siblingList.length>0){
						  	$scope.items.countSiblings=[];
				        	  for (var i=0;i<items.people.siblingList.length;i++){
						   		    $scope.items.countSiblings.push(i);
						   		    $scope.items.siblingSelectedNumber= items.people.siblingList.length;
						   		    $scope.items.siblingList=items.people.siblingList;
						   	}
					  }
					  
					  // function invoked at the click "Number of siblings"
			          $scope.fillCountSiblings = function(selNumber){
			        	  $scope.items.countSiblings=new Array();
			        	  $scope.items.siblingList=new Array();
			        	  $scope.items.siblingList.length=selNumber;
			        	  for (var i=0;i<selNumber;i++){
					   		    $scope.items.countSiblings.push(i);
					   		    $scope.items.siblingList[i]={};
					   		  }  
			          };
			   		  
					  $scope.ok = function () {
						$scope.$$childTail.items.people.dateOfBirth = $scope.$$childTail.items.date;
//						for (var i=0;i<$scope.$$childTail.items.siblingList.length;i++){
//							$scope.items.siblingList.push(items.people.siblingList[i]);
//				     	}
						update($scope.$$childTail.items);
						$modalInstance.dismiss('cancel');
					    
					  };

					  $scope.cancel = function () {						
					    $modalInstance.dismiss('cancel');					   	
					  };
					 
			};
			

			
/**********************************************************************************************************************************************/
// Open message dialog	
			function openMessageDialog(code){
				$modal.open({
					templateUrl: 'messageDialog.html',
					controller: ModalMessageDialog,
			    	size: "",
					resolve: {		          
				          items: function () {
				        	  if(code=="fileNumbermessage"){
				        		  $scope.message = "This file number is already present!";
				        	  }
				        	  if(code=="threeNamesmessage" || "twoNamesVillagesmessage"){
				        		  $scope.message = "This person is already present!";
				        	  }
				        	  if(code=="threeNamesAnotherProgram"){
				        		  $scope.message = "This person is present in another program, we'll update it with the new infos!";
				        	  }
				        	  if(code=="filePdfAlreadyInUse"){
				        		  $scope.message = "Please, store and close the pdf that is already in use";
				        	  }
				        	  if(code=="okPdf"){
				        		  $scope.message = "Pdf correctly generated"; 
				        	  }
				        	  var message = {"message":$scope.message};
				        	  return message;
				          }
					}
				});			
			}
			
			var ModalMessageDialog= function ($scope, $modalInstance, items) {				  
				  
				$scope.items = items;	
				$scope.cancel = function () {
				    $modalInstance.dismiss('cancel');
				  };

		};		
/**********************************************************************************************************************************************/
// Report
			
		$scope.report = function(){
			var globalPdf ={"peopleList":$scope.personData, "projectPerson":$scope.projectPerson, "filter":filter};
			$http.post("../pdf/createPeoplePdf", globalPdf, { responseType: 'arraybuffer' }).success(function(data){
				
				 var file = new Blob([data], { type: 'application/pdf' });
                 var fileURL = URL.createObjectURL(file);
                 window.open(fileURL);
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
			$scope.isCPHA=false;
			$scope.isCPHAOrphan=false;
			$scope.isCPHAPlwhiv=false;
			$scope.isCPHARecoveree=false;
			
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
			  if(projectCode=="CPHA") {
				  $scope.isCPHA=true;
			  }
			  if(projectCode=="CPHA" && personType=="OR") {
				  $scope.isCPHAOrphan=true;
			  }
			  if(projectCode=="CPHA" && personType=="LH") {
				  $scope.isCPHAPlwhiv=true;
			  }
			  if(projectCode=="CPHA" && personType=="RE") {
				  $scope.isCPHARecoveree=true;
			  }
			  if(personType=="BE" || personType=="OR" || personType=="LH" || personType=="RE") {
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
	  	   selectPersonTypeFromTab(personTypeParameter);  
	  	   setFlags();
	  	   resetFormSearch();
	  	   $scope.selectFilter(null, null, null, null, null);
		};	
		
		function includeStatusLevel(){
			if(!items.isCPPRBeneficiary && !items.isCPHARecoveree){
				return true;
			}
			return false;
		}
		
		function toUppercase(field){
			return field.toUppercase();
		}
	}]);
});