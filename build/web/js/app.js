;
(function(world) {

    var TRIPTEMPLATE = "<li class='atrip' id={{id}} title='{{title}}' start='{{startTime}}' end='{{endTime}}' startLocation='{{startLocation}}' endLocation='{{endLocation}}' description='{{description}}'>\
<a href='#'>{{title}}</a></li> ";
    /* Main app */
    var app = {
        waypoints: [], //the waypoints of the stopovers - will be on map
        markers: [], //markers of the stopovers for any given day -- should stop using this 
        TRIPURL: "trip",
        TRIPEVENTS: "events",
        TRIPDAY: "tripday",
        PHOTO: "photo",
        WAYPOINT: "waypoint",
        HOTEL: "hotel",
        timelineend: null,
        timelinestart: null,
        tripid: null,
        dayid: null,
        eventid: null,
        timeline: null,
        init: function() {
            app.initSpinner('hero');
            app.initCalendar();
            navigator.geolocation.getCurrentPosition(GetLocation);
            function GetLocation(location) {
                var lat = location.coords.latitude, long = location.coords.longitude;
                app.initMap(lat, long);
            }
            app.initData();
            app.initHandlers();
            app.initTimepickers($('#startTime'), $('#endTime'), null, null);
            app.stopSpinner();
        },
        initData: function() {
            $.get(app.TRIPURL, function(data) {
                var trips = data;
                if (trips.length === 0) {
                    $('<p>', {text: 'You have no trips'}).appendTo($('#yourtrips'));
                    return;
                }
                $.each(trips, function(i, e) {
                    var elem = $(Mustache.render(TRIPTEMPLATE, e));
                    elem.appendTo($('#yourtrips'));
                });
            });
        },
        initHandlers: function() {
            $('#viewalbum').click(function(){
               app.firstimage.find('a').trigger('click');
            });
            $('#editevents').click(function(e) {
                e.preventDefault();
                window.location = "daydetails.jsp";
            });
            $('#tripeditform').hide();
            $('#edittrip').click(function() {
                var parent = $('#tripeditform');
                app.initTimepickers(parent.find('input#tripsd'), parent.find('input#triped'), null, null);
                $('#tripeditform input').attr('readonly', function(i, attr) {
                    if (attr === 'readonly')
                        app.edittrip = true;
                    else
                        app.edittrip = false;
                    return !attr;
                });
            });
            $('#submitedittrip').click(function(e) {
                e.preventDefault();
                if (!app.edittrip)
                    return;
                app.initSpinner('hero');
                $.post(app.TRIPURL + '?action=update', $('#tripeditform').serialize()).success(function(d) {
                    app.stopSpinner();
                    var tripdata = d;
                    app.timeline.updateInterval(new Date(tripdata.startTime), new Date(tripdata.endTime));
                });
            });
            $('#waypointsortable').sortable({
                update: function(e, ui) {
                    app.updateAddresses();
                }
            })
                    .on('click', '.deletestop', function(e) {
                e.preventDefault();
                $(this).parent().remove();
                app.updateAddresses();
            }).on('blur', 'input:text', app.updateAddresses);
            $('#updatewaypoints').click(function(e) {
                e.preventDefault();
                var waypointform = $('#waypointform');
                var locations = waypointform.find('input[name="locations[]"]');
                for (var i = 0; i < locations.length; i++) {
                    if (locations[i].value.length === 0) {
                        waypointform.find('#errors').text('Cannot submit with empty location');
                        return;
                    }
                }
                app.initSpinner('map-canvas');
                $.post(app.WAYPOINT + "?action=update&tripid=" + app.timeline.tripid + "&tripdayid=" + app.timeline.tripdayid,
                        waypointform.serialize()).
                        success(function(d) {
                    app.stopSpinner();
                    waypointform.find('#errors').text('');
                });
            });
            $('#createwaypoint').click(function() {
                $(Mustache.render(app.timeline.WAYPOINTTEMPLATE, {location: ''})).
                        appendTo($('#waypointsortable'));
            });

            $('#newtripbutton').click(function() {
                $('#newtripinfo').toggle(100).siblings().hide();
            });
            $('#newphoto').click(function() {
                $('#uploadphotodiv').toggle(100).siblings().hide();
            });
            //ajax file upload handler
            $('#addphoto').click(function(e) {
                e.preventDefault();
                var formdata = new FormData(), file = document.getElementById('photofile').files[0], xhr;
                formdata.append('file', file);
                formdata.append('eventid', $('#photoeventid').val());
                formdata.append('tripdayid', $('#phototripdayid').val());
                formdata.append('description', $('#photodescription').val());
                formdata.append('action', 'trip');
                app.postPhoto(formdata);
            });
            //update the page to show the data for the particular trip
            $('#yourtrips').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, elem) {
                    $(elem).removeClass('selected');
                });
                app.clearForms();
                app.waypoints = [];
                $('#timelineinfo').hide(100);
                app.tripid = $(this).attr('id');
                app.timelinestart = new Date($(this).attr('start'));
                app.timelineend = new Date($(this).attr('end'));
                app.startLocation = $(this).attr('startLocation');
                app.endLocation = $(this).attr('endLocation');
                $('#daystart').val(app.startLocation);
                $('#dayend').val(app.endLocation).trigger('change');
                //$('#daystart').val(app.startLocation).trigger('change');
                $(this).addClass('selected');
                //get all events for the trip and update timeline
                app.initSpinner('hero');
                $.get(app.TRIPEVENTS + '?action=trip', {'tripid': app.tripid}).success(function(result) {
                    app.stopSpinner();
                    if (!result) {/*no events - show error*/
                        return;
                    }
                    //set timeline start and end of the timeline
                    app.timeline.updateIntervalAndEvents(app.timelinestart, app.timelineend, app.tripid, result);
                });
                //update the hero div
                var parent = $('.hero-unit');
                parent.find('input').attr('readonly', true);
                parent.find('input#triptitle').val(this.getAttribute('title'));
                parent.find('input#tripsl').val(this.getAttribute('startLocation'));
                parent.find('input#tripel').val(this.getAttribute('endLocation'));
                parent.find('input#tripsd').val(this.getAttribute('start')).removeClass('hasDatepicker');
                parent.find('input#triped').val(this.getAttribute('end')).removeClass('hasDatepicker');
                parent.find('input#tripdesc').val(this.getAttribute('description'));
                $('#tripeditform').show();

//                        .append("<div class='span6'>\
//                                <span id='photoprogress'></span>\
//                                <label for=thumbnails'>Photo (click for album)</label><div id='thumbnails' class='span12'></div></div>"
//                        );
                //load all photos - put on separate page
                app.loadPhotos('trip', app.tripid);
            });
            //create new trip handler
            $('#createtrip').click(function(e) {
                e.preventDefault();
                app.initSpinner('hero');
                $.post(app.TRIPURL + '?action=add', $('#newtrip').serialize()).success(
                        function(result) {
                            app.stopSpinner();
                            var parent = $('#yourtrips')
                            if ($.isEmptyObject(result))
                                return;
                            parent.find('p').remove();
                            var elem = $(Mustache.render(TRIPTEMPLATE, result));
                            elem.appendTo(parent);
                        });
            });
            //edit the information for a day
            $('#editday').click(function(e) {
                e.preventDefault();
                app.initSpinner('hero');
                $.post(app.TRIPDAY + '?action=update', $('#tripdayform').serialize()).success(
                        function(result) {
                            app.stopSpinner();
                            app.timeline.updateDayForm(result);
                        });
            });
        },
        loadPhotos: function(action, id) {
            $.get(app.PHOTO, {'action': action, 'id': id}).success(function(d) {
                var data = d;
                if ($.isEmptyObject(data))
                    return;
                $.each(data, function(i, e) {
                    var img = $('<div>', {
                        class: 'single',
                        style: 'float:left;'
                    }).append($('<a>', {
                        href: e.url,
                        rel: 'lightbox[album]',
                        title: e.comment || '' + '(' + e.uploadtime + ')'
                    }).append($('<img>', {
                        src: e.url,
                        width: '50px',
                        height: '70px'
                    })));
                    if(i === 0) app.firstimage = img;
                    $('#thumbnails').append(img);
                    img.hide();                    
                });
            });
        },
        postPhoto: function(formdata) {
            var xhr = new XMLHttpRequest(), completed = false;
            function onProgressHandler(e) {
                if (e.lengthComputable) {
                    var progress = e.loaded / e.total;
                    $('#photoprogress').text(progress + '%');
                }
            }
            function onCompleteHandler(e) {
                if (e.target.status === 200 && e.target.responseText && !completed) {
                    app.stopSpinner();
                    $('#photoprogress').text('done');
                    var parent = $('#thumbnails');
                    completed = true;
                    var photo = JSON.parse(e.target.responseText);
                    
                    if ($.isEmptyObject(photo))
                        return;
                    var img = $('<div>', {
                        class: 'single',
                        style: 'float:left;'
                    }).append($('<a>', {
                        href: photo.url,
                        rel: 'lightbox[album]',
                        title: photo.comment
                    }).append($('<img>', {
                        src: photo.url,
                        height: '70px'
                    }))).hide();
                    img.appendTo(parent);
                    

                }
            }
            app.initSpinner('hero');
            xhr.open('POST', app.PHOTO, true);
            xhr.upload.addEventListener('progress', onProgressHandler);
            xhr.addEventListener('readystatechange', onCompleteHandler);
            xhr.send(formdata);
        },
        initCalendar: function() {
            app.timeline = new Timeline('timeline', new Date()); //new Date());
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
        initMap: function(lat, long) {
            var self = this;
            var mapOptions = {
                center: new google.maps.LatLng(lat, long),
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
            new google.maps.places.Autocomplete(document.getElementById('startLocation'));
            new google.maps.places.Autocomplete(document.getElementById('endLocation'));
            new google.maps.places.Autocomplete(document.getElementById('daystart'));
            new google.maps.places.Autocomplete(document.getElementById('dayend'));
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
                    waypoints: app.waypoints,
                    optimizeWaypoints: false,
                    travelMode: google.maps.DirectionsTravelMode.DRIVING
                };
                self.directionsService.route(request, function(response, status) {
                    if (status === google.maps.DirectionsStatus.OK) {
                        self.directionsDisplay.setDirections(response);
                    }
                });
            }
        },
        updateAddresses: function() {
            //submit a geocoding request for each address and add it to the map
            var locations = $('#waypointsortable input:text');
//            for (var i = 0; i < app.markers.length; i++) {
//                app.markers[i].setMap(null);
//            }
            //app.markers = [];
            app.waypoints = [];
            for (var i = 0; i < locations.length; i++) {
                var location = locations[i].value;
                app.waypoints.push({
                    location: location,
                    stopover: true
                });
                //app.geocodeAddress(location);
            }
            $('#daystart').trigger('change'); //update map

        },
        geocodeAddress: function(address) {
            app.geocoder.geocode({'address': address}, function(results, status) {
                if (status === google.maps.GeocoderStatus.OK) {
                    var marker = new google.maps.Marker({
                        map: app.map,
                        position: results[0].geometry.location
                    });
                    app.markers.push(marker);
                }
            });
        },
        initSpinner: function(elemid) {
            var target = document.getElementById(elemid);
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
            app.spinner = new Spinner(opts).spin(target);
        },
        stopSpinner: function() {
            app.spinner.stop();
        },
        clearForms: function() {
            var uploadphotodiv = $('#uploadphotodiv');
            uploadphotodiv.find('input[type=text]').val("");
            uploadphotodiv.find('input[type=hidden]').removeAttr('value');
            var neweventdiv = $('#neweventform');
            neweventdiv.find('input[type=text]').val('');
            neweventdiv.find('input[type=hidden]').removeAttr('value');
        }
    };
    world.app = app;
    app.init();
})(window);


