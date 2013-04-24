;
(function(world) {
    var TRIPURL = "trip";
    var TRIPEVENTS = "events";
    var TRIPDAY = "tripday";
    var TRIPTEMPLATE = "<li class='atrip' id={{id}} start={{startTime}} end={{endTime}} startLocation='{{startLocation}}'><a href='#'>{{title}}</a><p id='description'>{{description}}</description><br>\
From: {{startLocation}} {{startTime}}, End: {{endLocation}} {{endTime}} </li> ";
    /* Main app */
    var app = {
        timelineend: null,
        timelinestart: null,
        tripid: null,
        dayid: null,
        eventid: null,
        timeline: null,
        init: function() {
            app.initCalendar();
            app.initMap();
            app.initData();
            app.initHandlers();
            app.initTimepickers($('#startTime'), $('#endTime'));
        },
        initData: function() {
            $.get(TRIPURL, function(data) {
                var trips = JSON.parse(data);
                if(trips.length === 0){
                    $('<p>', {text: 'No trips'}).appendTo($('#yourtrips'));
                    return;
                }                
                $.each(trips, function(i, e) {
                    var elem = $(Mustache.render(TRIPTEMPLATE, e));
                    elem.appendTo($('#yourtrips'));
                });
            });
        },
        initHandlers: function() {
            var self = this;
            //update the page to show the data for the particular trip
            $('#yourtrips').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, e) {
                    $(e).removeClass('selected');
                });
                app.tripid = $(this).attr('id');
                app.timelinestart = new Date($(this).attr('start'));
                app.timelineend = new Date($(this).attr('end'));
                app.startLocation = $(this).attr('startLocation');
                //$('#daystart').val(app.startLocation).trigger('change');
                $(this).addClass('selected');
                //get all events for the trip and update timeline
                $.get(TRIPEVENTS, {'tripid': app.tripid}).success(function(result) {
                    var d = JSON.parse(result), event;
                    if (!d) {/*no events - show error*/
                        return;
                    }
                    //set timeline start and end of the timeline
                    app.timeline.updateIntervalAndEvents(app.timelinestart, app.timelineend, app.tripid, d);
                });
                //update the hero div
            });
            //create new trip handler
            $('#createtrip').click(function(e) {
                e.preventDefault();
                $.post(TRIPURL, $('#newtrip').serialize()).success(
                        function(result) {
                            var json = JSON.parse(result);
                            if ($.isEmptyObject(json))
                                return;
                            var elem = $(Mustache.render(TRIPTEMPLATE, json));
                            elem.appendTo($('#yourtrips'));
                        });
            });
            //edit the information for a day
            $('#editday').click(function(e) {
                e.preventDefault();
                $.post(TRIPDAY + '?action=update', $('#tripdayform').serialize()).success(
                        function(result) {
                            self.timeline.updateDayForm(result);
                        });
            });
        },
        initCalendar: function() {
            app.timeline = new Timeline('timeline', new Date());//new Date());
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
        },
        initMap: function() {
            function initialize() {
                var mapOptions = {
                    center: new google.maps.LatLng(-34.397, 150.644),
                    zoom: 8,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
                var geocoder = new google.maps.Geocoder();

                $('#startLocation').keyup(codeAddress);
                $('#daystart').keyup(codeAddress).change(codeAddress);
                function codeAddress() {
                    var address = $(this).val();
                    geocoder.geocode({'address': address}, function(results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            map.setCenter(results[0].geometry.location);
                            var marker = new google.maps.Marker({
                                map: map,
                                position: results[0].geometry.location,
                                title: 'Start!'
                            });
                        } else {//squelch
                        }
                    });
                }
            }
            google.maps.event.addDomListener(window, 'load', initialize);
        }

    };
    world.app = app;
    app.init();

})(window);


