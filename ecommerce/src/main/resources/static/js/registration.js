// <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
// <script>
//$(document).ready(function () {
//    $("#registerForm").submit(function (event) {
//        event.preventDefault(); // Ngăn chặn reload trang
//
//        let userData = {
//            email: $("#email").val(),
//            username: $("#username").val(),
//            password: $("#password").val(),
//            name: $("#name").val(),
//            surname: $("#surname").val(),
//            phone: $("#phone").val()
//        };
//
//        $.ajax({
//            url: "http://localhost:8080/api/register",
//            type: "POST",
//            contentType: "application/json",
//            data: JSON.stringify(userData),
//            success: function (response) {
//                if (response.success) {
//                    alert("Đăng ký thành công!");
//                } else {
//                    alert("Đăng ký thất bại: " + response.message);
//                }
//            },
//            error: function (xhr, status, error) {
//                console.error("Lỗi:", error);
//                alert("Có lỗi xảy ra, vui lòng thử lại!");
//            }
//        });
//    });
//});
//</script>
