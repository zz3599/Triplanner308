;
(function() {
    var LOGINURL = 'login';
    var REGISTERURL = 'register';
    var index = {
        initHandlers: function() {
            $('#login').click(index.loginHandler);
            $('#register').click(index.registerHandler);
        },
        loginHandler: function() {
            var data = {
                'email': $('#loginemail').val(),
                'password': $('#loginpassword').val()
            };
            index.post(LOGINURL, data, function(e){}, null);

        },
        registerHandler: function(){
            
        },
        post: function(url, data, s) {
            $.post(url, data, s);
        },
        registerHandler: function() {

        }

    };
    index.initHandlers();

})();


