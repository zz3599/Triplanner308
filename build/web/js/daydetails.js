;
(function(world) {
    var daydetails = {
        EVENTS: "events",
        EVENTTEMPLATE: '<li id="{{id}}" starttime="{{startTime}}" endtime="{{endTime}}" startlocation="{{startLocation}}" endlocation="{{endLocation}}" comment="{{comment}}" tripdayid="{{tripdayid}}"><a href="#">{{comment}}</a></li>',
        WAYPOINTTEMPLATE: '<li class="ui-state-default"><span class="ui-icon ui-icon-arrowthick-2-n-s"></span><input type="text" class="settingInput2" value="{{location}}" name="locations[]" /><button type="button" class="deletestop">delete</button></li>',
        WAYPOINT: "waypoint",
        EVENTS: "events",
        waypoints: [], // waypoints of the particular day
        events: [], //events for the particular day
        eventmarkers: [], //markers for events
        yourevents: $('#yourevents'),
        editevent: false, //flag for whether the event has been edited
        edittrip: false, //flag for whether the trip has been edited
        dayForm: $('#dayeditform'),
        init: function() {
            this.eventForm = $('#eventform');
            this.date = new Date($('#date').val());
            var tomorrow = new Date(this.date.getTime());
            tomorrow.setDate(this.date.getDate() + 1);
            daydetails.initTimepickers($('#eventstarttime'), $('#eventendtime'), this.date, tomorrow);
            daydetails.initHandlers();
            daydetails.initData();
            daydetails.initMap();
        },
        initHandlers: function() {
            $('#submitevent').click(function(e) {
                e.preventDefault();
                var ok = daydetails.editevent && $('#eventstartlocation').val()  &&
                        $('#eventdescription').val() && $('#eventstarttime').val() && $('#eventendtime').val();
                if (!ok) {
                    $('#errors').text('Fields invalid');
                    return;
                }
                $('#errors').text('');
                var eventid = $('#eventid').val();
                var action = "?action=";
                action += (eventid) ? 'update' : 'add';
                daydetails.initSpinner('hero');
                $.post(daydetails.EVENTS + action, $('#eventform').serialize()).success(function(data) {
                    daydetails.stopSpinner();
                    if(action.indexOf('update') !== -1){
                        $('#' + eventid).attr('startlocation', data.startLocation).attr('endlocation', data.endLocation).
                                attr('starttime', data.startTime).attr('endtime', data.endTime).attr('comment', data.comment).
                                find('a').text(data.comment);
                    } else {
                        var elem = $(Mustache.render(daydetails.EVENTTEMPLATE, data)).appendTo($('#yourevents'));
                    }
                });
            });
            $('#addevent').click(function(e) {
                daydetails.eventForm.find('input').val('').removeAttr('readonly');
                daydetails.editevent = true;
            });
            $('#yourevents').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, elem) {
                    $(elem).removeClass('selected');
                });
                $(this).addClass('selected');
                //update event form, show highlighted marker
                var eventid = $(this).attr('id');
                var starttime = $(this).attr('starttime');
                var endtime = $(this).attr('endtime');
                var startlocation = $(this).attr('startlocation');
                var endlocation = $(this).attr('endlocation');
                var description = $(this).attr('comment');
                daydetails.eventForm.find('input#eventid').val(eventid);
                daydetails.eventForm.find('input#eventstarttime').val(starttime);
                daydetails.eventForm.find('input#eventendtime').val(endtime);
                daydetails.eventForm.find('input#eventstartlocation').val(startlocation);
                daydetails.eventForm.find('input#eventendlocation').val(endlocation);
                daydetails.eventForm.find('input#eventdescription').val(description);
                daydetails.eventForm.find('input').attr('readonly', true);
                daydetails.editevent = false;
            });
            $('#editday').click(function() {
                daydetails.dayForm.find('input').attr('readonly', function(i, attr) {
                    if (attr === 'readonly')
                        daydetails.edittrip = true;
                    else
                        daydetails.edittrip = false;
                    return !attr;
                });
            });
            $('#editevent').click(function() {
                daydetails.eventForm.find('input').attr('readonly', function(i, attr) {
                    if (attr === 'readonly')
                        daydetails.editevent = true;
                    else
                        daydetails.editevent = false;
                    return !attr;
                });
            });

        },
        initData: function() {
            this.tripid = $('#tripid').val();
            this.tripdayid = $('#tripdayid').val();
            this.initSpinner('hero');
            $.get(daydetails.EVENTS + "?action=day", {'tripdayid': $('#tripdayid').val()}).success(function(data){
                $.each(data, function(i, elem){
                    $(Mustache.render(daydetails.EVENTTEMPLATE, elem)).appendTo($('#yourevents'));
                });
            });
            $.get(daydetails.WAYPOINT, {'tripid': daydetails.tripid, 'tripdayid': daydetails.tripdayid}).success(function(d) {
                daydetails.stopSpinner();
                var data = d;
                var parent = $('#waypointsortable');
                parent.empty();
                $.each(data, function(i, waypoint) {
                    var elem = $(Mustache.render(daydetails.WAYPOINTTEMPLATE, waypoint)).appendTo(parent);
                    //window.app.geocodeAddress(waypoint.location);
                    daydetails.waypoints.push({location: waypoint.location, stopover: true});
                });
                $('#startlocation').trigger('change'); //this updates the map
            });
        },
        updateMarkers: function() {
            this.events = [];
            $.each(this.eventmarkers, function(i, elem) {
                elem.setMap(null);
            });
            this.eventmarkers = [];
            this.limits = new google.maps.LatLngBounds();
            $.each($('#yourevents li'), function(i, elem) {
                var startlocation = $(this).attr('startlocation');
                var endlocation = $(this).attr('endlocation');
                var description = $(this).attr('description');
                var starttime = $(this).attr('starttime');
                var endtime = $(this).attr('endtime');
                daydetails.events.push({
                    'startlocation': startlocation,
                    'endlocation': endlocation,
                    'description': description,
                    'starttime': starttime,
                    'endtime': endtime
                });
                daydetails.geocodeAddress(startlocation);
                if (endlocation)
                    daydetails.geocodeAddress(endlocation);
            });
            daydetails.geocodeAddress($('#eventstartlocation').val());
            daydetails.geocodeAddress($('#eventendlocation').val());


        },
        geocodeAddress: function(address) {
            this.geocoder.geocode({'address': address}, function(results, status) {
                if (status === google.maps.GeocoderStatus.OK) {
                    var marker = new google.maps.Marker({
                        map: daydetails.map,
                        position: results[0].geometry.location
                    });
                    daydetails.eventmarkers.push(marker);
                    daydetails.limits.extend(new google.maps.LatLng(marker.position.kb, marker.position.lb));
                    //center on the markers
                    daydetails.map.fitBounds(daydetails.limits);
                }
            });
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
            $('#startlocation').change(codeAddress);
            $('#endlocation').change(codeAddress);
            //let the user see live changes to markers
            $('#eventstartlocation').blur(addMarker);
            $('#eventendlocation').blur(addMarker);
            //autocomplete
            new google.maps.places.Autocomplete(document.getElementById('eventstartlocation'));
            new google.maps.places.Autocomplete(document.getElementById('eventendlocation'));
            function addMarker() {
                daydetails.updateMarkers();
            }
            function codeAddress() {
                var startaddress, endaddress;
                startaddress = $('#startlocation').val();
                endaddress = $('#endlocation').val();
                var request = {
                    origin: startaddress,
                    destination: endaddress,
                    waypoints: self.waypoints,
                    optimizeWaypoints: false,
                    travelMode: google.maps.DirectionsTravelMode.DRIVING
                };
                self.directionsService.route(request, function(response, status) {
                    if (status === google.maps.DirectionsStatus.OK) {
                        self.directionsDisplay.setDirections(response);
                        //put the event markers on map
                        daydetails.updateMarkers();
                    }
                });

            }
        },
        initSpinner: function(element) {
            var target = document.getElementById(element);
            var opts = {
                lines: 13, // The number of lines to draw
                length: 20, // The length of each line
                width: 10, // The line thickness
                radius: 30, // The radius of the inner circle
                corners: 1, // Corner roundness (0..1)
                rotate: 0, // The rotation offset
                direction: 1, // 1: clockwise, -1: counterclockwise
                color: '#000', // #rgb or #rrggbb
                speed: 1.8, // Rounds per second
                trail: 60, // Afterglow percentage
                shadow: false, // Whether to render a shadow
                hwaccel: false, // Whether to use hardware acceleration
                className: 'spinner', // The CSS class to assign to the spinner
                zIndex: 2e9, // The z-index (defaults to 2000000000)
                top: 'auto', // Top position relative to parent in px
                left: 'auto' // Left position relative to parent in px
            };
            this.spinner = new Spinner(opts).spin(target);
        },
        stopSpinner: function() {
            if (this.spinner)
                this.spinner.stop();
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
        }
    };

    daydetails.init();
    window.daydetails = daydetails;
})(window);
