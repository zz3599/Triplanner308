;
(function(world) {
    var TRIPURL = "trip";
    var TRIPEVENTS = "events";
    var TRIPDAY = "tripday";
    var TRIPTEMPLATE = "<li class='atrip' id={{id}} title='{{title}}' start={{startTime}} end={{endTime}} startLocation='{{startLocation}}' endLocation='{{endLocation}}' description='{{description}}'>\
<a href='#'>{{title}}</a></li> ";
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
            app.initTimepickers($('#startTime'), $('#endTime'), null, null);
        },
        initData: function() {
            $.get(TRIPURL, function(data) {
                var trips = JSON.parse(data);
                if (trips.length === 0) {
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
            $('#newtripbutton').click(function() {
                $('#newtripinfo').toggle(100);
                $('#timelineinfo').hide();
            });
            //update the page to show the data for the particular trip
            $('#yourtrips').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, e) {
                    $(e).removeClass('selected');
                });
                $('#timelineinfo').hide(100);
                app.tripid = $(this).attr('id');
                app.timelinestart = new Date($(this).attr('start'));
                app.timelineend = new Date($(this).attr('end'));
                app.startLocation = $(this).attr('startLocation');
                app.endLocation = $(this).attr('endLocation');
                $('#daystart').val(app.startLocation).trigger('change');
                $('#dayend').val(app.endLocation).trigger('change');
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
                $('.hero-unit').empty();
                var row = $('<div>', {
                    class: 'row-fluid'
                }).append("<div class='span6 tripcontent'><p>Trip: " + $(this).attr('title') +
                        '</p><p>Start: ' + $(this).attr('startLocation') + '(' + $(this).attr('start') + ')' +
                        '</p><p>Destination: ' + $(this).attr('endLocation') + '(' + $(this).attr('end') + ')' +
                        '</p><p>Description: ' + $(this).attr('description') + '</p></div>')
                        .append("<form action='photo' method='post' enctype='multipart/form-data'>\
                                Photo Description<input type='text' name='description' />\
                                <input type='file' name='file' />\
                                <input type='hidden' name='eventid' id='photoeventid'/>\
                                <input type='hidden' name='tripdayid' id='phototripdayid'/>\
                                <input type='text' name='eventdescription' id='eventdescription' readonly>\
                                <input type='submit' value='Add photo'></form>"
                        );

                $('.hero-unit').append(row);

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
                            app.timeline.updateDayForm(result);
                        });
            });
        },
        initCalendar: function() {
            app.timeline = new Timeline('timeline', new Date());//new Date());
        },
        initTimepickers: function(startTime, endTime, minDate, maxDate) {
            var startTimeProps = {
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
            };
            var endTimeProps = {
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
            };
            if (minDate) {
                startTimeProps.minDate = minDate;
                endTimeProps.minDate = minDate;
            }
            if (maxDate) {
                startTimeProps.maxDate = maxDate;
                endTimeProps.maxDate = maxDate;
            }
            startTime.datetimepicker(startTimeProps);
            endTime.datetimepicker(endTimeProps);
        },
        initMap: function() {
            var self = this;
            var mapOptions = {
                center: new google.maps.LatLng(40.7, -74.1),
                zoom: 5,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            this.map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
            this.geocoder = new google.maps.Geocoder();
            this.directionsService = new google.maps.DirectionsService();
            this.directionsDisplay = new google.maps.DirectionsRenderer();
            this.directionsDisplay.setMap(this.map);
            $('#startLocation').keyup(codeAddress).change(codeAddress).focus(codeAddress);
            $('#endLocation').keyup(codeAddress).change(codeAddress).focus(codeAddress);
            $('#daystart').keyup(codeAddress).change(codeAddress).focus(codeAddress);
            $('#dayend').keyup(codeAddress).change(codeAddress).focus(codeAddress);
            function codeAddress() {
                var startaddress, endaddress;
                if (this.name.toLowerCase().indexOf('day') !== -1) {
                    startaddress = $('#daystart').val();
                    endaddress = $('#dayend').val();
                } else {
                    startaddress = $('#startLocation').val();
                    endaddress = $('#endLocation').val();
                }
                var request = {
                    origin: startaddress,
                    destination: endaddress,
                    travelMode: google.maps.DirectionsTravelMode.DRIVING
                };
                self.directionsService.route(request, function(response, status) {
                    if (status === google.maps.DirectionsStatus.OK) {
                        self.directionsDisplay.setDirections(response);
                    }
                });
            }
        },
    };

    world.app = app;
    app.init();

})(window);


