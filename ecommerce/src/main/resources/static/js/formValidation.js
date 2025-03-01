function emailCheck() {
    if ($("#email").val() === "") {
        $("#email").addClass('is-invalid');
        return false;
    } else {
        var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
        if (regMail.test($("#email").val()) === false) {
            $("#email").addClass('is-invalid');
            return false;
        } else {
            $("#email").removeClass('is-invalid');
            $("#next-form").collapse('show');
        }
    }
}

let password = document.getElementById('password');
let icon1 = document.getElementById('icon1');
icon1.addEventListener('click', () => {
    password.type = password.type === 'password' ? 'text' : 'password';
    icon1.className = icon1.className === 'fas fa-eye' ? 'fas fa-eye-slash' : 'fas fa-eye';
});

//        [START]: PURE JAVASCRIPT
//        
//        let emailElement = document.getElementById("email");
//        let nextForm = document.getElementById("next-form");
//        let password = document.getElementById("password");
//        let icon1 = document.getElementById("icon1");
//        let regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
//        function emailCheck(){
//        if (emailElement.value.trim() === "" || !regMail.test(emailElement.value.trim())){
//        emailElement.classList.add("is-invalid");
//                return false;
//        }
//        else{
//        emailElement.classList.remove("is-invalid");
//                nextForm.classList.add("show");
//        }
//        };
//        icon1.addEventListener("click", () => {
//        password.type = password.type === "password" ? "text" : "password";
//                if (icon1.classList.contains("fa-eye")){
//        icon1.classList.remove("fa-eye");
//                icon1.classList.add("fa-eye-slash");
//        }
//        else{
//        icon1.classList.remove("fa-eye-slash");
//                icon1.classList.add("fa-eye");
//        }
//        });
//
//        [END]: PURE JAVASCRIPT