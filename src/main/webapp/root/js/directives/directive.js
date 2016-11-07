define(['angularAMD'], function (angularAMD) {
	angularAMD.directive('upcase', [function () {
	    return {
	        require: 'ngModel',
	        link: function (scope, element, attrs, ngModel) {
	            element.attr("autocomplete","off");
	            ngModel.$parsers.unshift(function (viewValue) {
	                if (typeof viewValue === "string" && viewValue.length > 0) {
	                    var caretPos=getCaretPosition(element[0]);
	                    ngModel.$viewValue=viewValue.toUpperCase();
	                    ngModel.$render();
	                    setCaretPosition(element[0], caretPos);
	                }

	                return ngModel.$viewValue;
	            });
	        }
	    };
	}]);
	
	angularAMD.directive('ngfocus', [function () {
	    return {
	        restrict:'A',
	        scope: true,
	        link: function (scope, element, attrs, ngModel) {
	            element[0].focus();	           
	        }
	    };
	}]);

function getCaretPosition(elem) {
		if (document.selection && document.selection.createRange) {
	        var range = document.selection.createRange();
	        var bookmark = range.getBookmark();
	    	return bookmark.charCodeAt(2) - 2;
	    } else {
	    	return elem.selectionStart;
	    }
	}
function setCaretPosition(elem, caretPos) {
	    if (elem != null) {
	        if (elem.createTextRange) {
	            var range = elem.createTextRange();
	            range.move('character', caretPos);
	            range.select();
	        } else {
	            if (elem.selectionStart) {
	                elem.focus();
	                elem.setSelectionRange(caretPos, caretPos);
	            } else
	                elem.focus();
	        }
	    }
	}

});
 