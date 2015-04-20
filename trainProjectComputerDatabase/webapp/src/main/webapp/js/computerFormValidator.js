(function($, W, D) {
	var validationComputersOperations = {};
	validationComputersOperations.UTIL = {
			setupFormValidation : function() {
//				Add custom methode to match date
				$.validator.addMethod("DateValidator", function(value, element) {
//					Because date is not required
					if (!value)
						return true;
					// regular expression to match required date format 
					re = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
					
					if(form.startdate.value != '') { 
						if(regs = form.startdate.value.match(re)) { 
							var dd = regs[3];
							var mm = regs[2];
							var yy = regs[1];
							
							// Create list of days of a month [assume there is no leap year by default]  
							var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31]; 
							if (mm==1 || mm>2) {
								if (dd > ListofDays[mm-1]) {
									return false;
								}  
							}  
							if (mm == 2) {
								var lyear = false;  
								if ( (!(yy % 4) && yy % 100) || !(yy % 400)) {
									lyear = true;  
								}  
								if ((lyear == false) && (dd >= 29)) {
									return false;  
								}  
								if ((lyear == true) && (dd > 29)) {
									return false;  
								}  
							}
						}
					}
				}, "Please enter a date in the format yyyy-MM-dd.");
				$("#formAddEditComputer").validate({
//					form validation rules
					rules : {
						computerName : "required",
						introduced : {
							required : false,
							DateValidator : true
						},
						discontinued : {
							required : false,
							DateValidator : true
						}
					},
//					form validation messages
					messages : {
						computerName : "Please enter computer name"
					},
					submitHandler : function(form) {
						form.submit();
					}
				});
			}
	}
//	When document is ready
	$(D).ready(function($) {
		validationComputersOperations.UTIL.setupFormValidation();
	});
})(jQuery, window, document);