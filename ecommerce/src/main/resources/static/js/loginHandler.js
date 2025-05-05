const loginEndpoint = "http://localhost:8080/api/auth/login";
$("#loginForm").submit(function (e) {
    e.preventDefault();
    $.ajax({
        url: loginEndpoint,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val()
        }),
        success: function (response) {
            localStorage.setItem("access_token", response.data);
            console.log("Đăng nhập thành công:", response);
            alert("Login OK");
        },
        error: function (xhr) {
            console.error("Lỗi đăng nhập:", xhr.responseText);
            alert("Login Error");
        }
    });
});