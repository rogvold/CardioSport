function registrationSuccesfulCallback(){
    toastr.success('Регистрация прошла успешно');
    window.location.href = "index.html";
}

function registrationFailedCallback(message){
    toastr.error(message , 'Ошибка');
}

function loginSuccessfulCallback(redir){
    window.location.href = redir;
}

function loginFailedCallback(message){
    toastr.error(message , 'Ошибка');
}

function login(email, password){
    
    $.ajax({
        url: '/CardioSportWeb/resources/auth/login?email='+email+"&password="+password,
        type: "POST",
        success: function(data){
            if (data.responseCode == 0){
                loginFailedCallback(data.error.message);
            }
            if (data.responseCode == 1){
                loginSuccessfulCallback(data.data);
            }
        },
        error: function(){
            loginFailedCallback("Проверьте подключение к интернету");
        }
    });
}


function logout(){
    
    $.ajax({
        url: '/CardioSportWeb/resources/auth/logout',
        type: "POST",
        success: function(data){
            if (data.error != undefined){
                alert(data.error.message);
                return;
            }
            window.location.href = '/CardioSportWeb/login.xhtml';
        },
        error: function(){
            loginFailedCallback("Проверьте подключение к интернету");
        }
    });
}


function register(email, password){
    //    alert('register: email/password = ' + email + "/" + password);
    var d = "email="+email+"&password="+password;
    $.ajax({
        url: "/CardioSportWeb/resources/auth/register/coach?" + d,
        type: "POST",
        success: function(data){
            if (data.responseCode == 0){
                registrationFailedCallback(data.error.message);
            }
            if (data.responseCode == 1){
                login(email, password);
            }
        },
        error: function(){
            registrationFailedCallback("Проверьте подключение к интернету");
        }
    });
}

function register_trainee(){
    var email = $('#email_input').val();
    var password = $('#password_input').val();
    var firstName = $('#firstName_input').val();
    var lastName = $('#lastName_input').val();
    var d = "email="+email+"&password="+password+"&firstName="+firstName + "&lastName=" + lastName;
    $.ajax({
        url: "/CardioSportWeb/resources/auth/register/trainee?" + d,
        type: "POST",
        success: function(data){
            if (data.responseCode == 0){
                registrationFailedCallback(data.error.message);
            }
            if (data.responseCode == 1){
                alert('registered');
                window.location.reload();
            }
        },
        error: function(){
            registrationFailedCallback("Проверьте подключение к интернету");
        }
    });
}

function sustainSession(){
    setInterval(function(){
        $.ajax({
            url: "/CardioSportWeb/resources/auth/sustain",
            type: "POST",
            success: function(data){
                
            },
            error: function(){
//                alert('unable to get the server response');
            }
        });
    }, 10000);
}