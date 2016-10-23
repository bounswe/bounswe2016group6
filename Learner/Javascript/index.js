$('.message a').click(function(){
   $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});

$('.login-form').submit(validateLoginForm);
$('.register-form').submit(validateLoginForm);
	

function validateLoginForm(e) {
	e.preventDefault();
    var x = document.forms["login-form"]["lName"].value;
    var y = document.forms["login-form"]["lPassword"].value;
    if (x == null || x == "" || y == null || y == "") {
        alert("All fields must be filled!");
        return false;
    }
   else{
	    
	    window.location.href="registeredHomePage.html";
        return false;
   }
   
    
}
function validateRegisterForm(e) {
	e.preventDefault();
    var x = document.forms["register-form"]["rName"].value;
	var y = document.forms["register-form"]["rPassword"].value;
    var z = document.forms["register-form"]["rMail"].value;
    if (x == null || x == "" || y == null || y == "" || z == null || z == "") {
        alert("All fields must be filled!");
        return false;
    }
    else{
	    window.location.href="registeredHomePage.html";
        return false;
    }
}