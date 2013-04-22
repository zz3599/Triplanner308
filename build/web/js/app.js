;
(function(world) {
    var TRIPURL = "trip";
    var TRIPTEMPLATE = "<li class='atrip' id={{id}}>{{title}}<p id='description'>{{description}}</description><br>\
From: {{startLocation}} {{startTime}}, End: {{endLocation}} {{endTime}} </li> ";
    var app = {
        init: function() {
            app.initCalendar();
            app.initData();
            app.initHandlers();
            app.initTimepickers($('#startTime'), $('#endTime'));
        },
        initData: function() {
            $.get(TRIPURL, function(data) {
                var trips = JSON.parse(data);
                $.each(trips, function(i, e) {
                    var elem = $(Mustache.render(TRIPTEMPLATE, e));
                    elem.insertAfter($('#yourtrips'));
                });
            });
        },
        initHandlers: function() {
            $('.atrip').click(function() {
                var tripid = $(this).attr('id');
                //send post request
            });
            $('#createtrip').click(function(e) {
                e.preventDefault();
                app.post(TRIPURL, $('#newtrip').serialize(),
                        function(result) {
                            var json = JSON.parse(result);
                        },
                        function(x, f, t) {

                        }
                );
            });
        },
        post: function(url, data, success, fail) {
            $.post(url, data, success, fail);
        },
        initCalendar: function() {
            var timeline = new Timeline('timeline', new Date("Wed Jul 01 2009"));
        },
        initTimepickers: function(startTime, endTime) {
            startTime.datetimepicker({
                dateFormat: "yy-mm-dd",
                timeFormat: "H:mm:ss",
                onClose: function(dateText, inst) {
                    if (endTime.val() !== '') {
                        var testStartDate = startTime.datetimepicker('getDate');
                        var testEndDate = endTime.datetimepicker('getDate');
                        if (testStartDate > testEndDate)
                            endTime.datetimepicker('setDate', testStartDate);
                    }
                    else {
                        endTime.val(dateText);
                    }
                },
                onSelect: function(selectedDateTime) {
                    endTime.datetimepicker('option', 'minDate', startTime.datetimepicker('getDate'));
                }
            });
            endTime.datetimepicker({
                dateFormat: "yy-mm-dd",
                timeFormat: "H:mm:ss",
                onClose: function(dateText, inst) {
                    if (startTime.val() !== '') {
                        var testStartDate = startTime.datetimepicker('getDate');
                        var testEndDate = endTime.datetimepicker('getDate');
                        if (testStartDate > testEndDate)
                            startTime.datetimepicker('setDate', testEndDate);
                    }
                    else {
                        startTime.val(dateText);
                    }
                },
                onSelect: function(selectedDateTime) {
                    startTime.datetimepicker('option', 'maxDate', endTime.datetimepicker('getDate'));
                }
            });
        }

    };
    world.app = app;
    app.init();
})(window);


