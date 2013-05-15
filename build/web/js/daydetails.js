;
(function(world) {
    var daydetails = {
        HOTELS: 'hotel',
        EVENTS: 'events',
        WAYPOINT: 'waypoint',
        EVENTTEMPLATE: '<li id="{{id}}" starttime="{{startTime}}" endtime="{{endTime}}" startlocation="{{startLocation}}" endlocation="{{endLocation}}" comment="{{comment}}" tripdayid="{{tripdayid}}"><a href="#">{{comment}}</a></li>',
        WAYPOINTTEMPLATE: '<li><span>{{location}} </span></li>',
        waypoints: [], // waypoints of the particular day
        events: [], //events for the particular day
        eventmarkers: [], //markers for events
        yourevents: $('#yourevents'),
        editevent: false, //flag for whether the event has been edited
        edittrip: false, //flag for whether the trip has been edited
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
            $('#viewalbum').click(function() {
                daydetails.firstimage.find('a').trigger('click');
            });
            $('#addphotos').click(function() {
                $('#uploadphotodiv').toggle();
                $('#waypointform').toggle();
            });
            $('#submithotel').click(function(e) {
                e.preventDefault();
                daydetails.initSpinner('hero');
                $.post(daydetails.HOTELS, $('#hotelform').serialize()).success(function(data) {
                    daydetails.stopSpinner();
                    $('#hotellocation').val(data.location);
                });
            });
            $('#submitevent').click(function(e) {
                e.preventDefault();
                var ok = daydetails.editevent && $('#eventstartlocation').val() &&
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
                    if (action.indexOf('update') !== -1) {
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
                $('#photoeventid').val(eventid);
                $('#eventdescription').val(description);

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
            $('#edithotel').click(function() {
                var readonly = $('#hotellocation').attr('readonly');
                $('#hotellocation').attr('readonly', readonly ? false : true);
            });
            $('#addphoto').click(function(e) {
                e.preventDefault();
                var formdata = new FormData(), file = document.getElementById('photofile').files[0], xhr;
                formdata.append('file', file);
                formdata.append('eventid', $('#photoeventid').val());
                formdata.append('tripdayid', $('#phototripdayid').val());
                formdata.append('description', $('#photodescription').val());
                if (!!$('#photoeventid').val()) {
                    formdata.append('action', 'event');
                } else {
                    formdata.append('action', 'day');
                }
                daydetails.postPhoto(formdata);
            });

        },
        initData: function() {
            this.tripid = $('#tripid').val();
            this.tripdayid = $('#tripdayid').val();
            this.initSpinner('hero');
            //load all photos for the specific day
            this.loadPhotos('day', this.tripdayid);
            $.get(daydetails.HOTELS + '?action=day').success(function(data) {
                if ($.isEmptyObject(data))
                    return;
                $.each(data, function(i, e) {
                    $('#hotellocation').val(e.location);
                });

            });
            $.get(daydetails.EVENTS + "?action=day", {'tripdayid': $('#tripdayid').val()}).success(function(data) {
                $.each(data, function(i, elem) {
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
        loadPhotos: function(action, id) {
            //map event id's to lists of photos
            this.eventid_photosmap = {};
            $.get('photo', {'action': action, 'id': id}).success(function(d) {
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
                    if (i === 0)
                        daydetails.firstimage = img;
                    $('#thumbnails').append(img);
                    img.hide();
                    var eventid = e.eventid;
                    if (!!eventid) {
                        if (daydetails.eventid_photosmap.eventid) {
                            daydetails.eventid_photosmap[eventid].push(img);
                        } else {
                            daydetails.eventid_photosmap[eventid] = [img];
                        }
                    }
                });
            });
        },
        postPhoto: function(formdata) {
            var xhr = new XMLHttpRequest(), completed = false;
            function onProgressHandler(e) {
                if (e.lengthComputable) {
                    var progress = e.loaded / e.total;
                    //$('#photoprogress').text(progress + '%');
                }
            }
            function onCompleteHandler(e) {
                if (e.target.status === 200 && e.target.responseText && !completed) {
                    daydetails.stopSpinner();
                    //$('#photoprogress').text('done');
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
            daydetails.initSpinner('hero');
            xhr.open('POST', 'photo', true);
            xhr.upload.addEventListener('progress', onProgressHandler);
            xhr.addEventListener('readystatechange', onCompleteHandler);
            xhr.send(formdata);
        },
        updateMarkers: function() {
            this.events = [];
            $.each(this.eventmarkers, function(i, elem) {
                elem.setMap(null);
            });
            this.eventmarkers = [];
            //map event id's to their locations
            this.eventid_locationsmap = {};
            this.limits = new google.maps.LatLngBounds();
            $.each($('#yourevents li'), function(i, elem) {
                var id = $(this).attr('id');
                var startlocation = $(this).attr('startlocation');
                var endlocation = $(this).attr('endlocation');
                var description = $(this).attr('description');
                var starttime = $(this).attr('starttime');
                var endtime = $(this).attr('endtime');
                var event = {
                    'id': id,
                    'startlocation': startlocation,
                    'endlocation': endlocation,
                    'description': description,
                    'starttime': starttime,
                    'endtime': endtime
                };
                daydetails.events.push(event);
                daydetails.eventid_locationsmap[startlocation] = event;
                daydetails.eventid_locationsmap[endlocation] = event;
                daydetails.geocodeAddress(startlocation, true);
                if (endlocation)
                    daydetails.geocodeAddress(endlocation, true);
            });
            //this is for new events that we have typed but not yet submitted
            daydetails.geocodeAddress($('#eventstartlocation').val(), true);
            daydetails.geocodeAddress($('#eventendlocation').val(), true);
            daydetails.geocodeAddress($('#startlocation').val(), false);
            daydetails.geocodeAddress($('#endlocation').val(), false);
        },
        geocodeAddress: function(address, makemarker) {
            this.geocoder.geocode({'address': address}, function(results, status) {
                if (status === google.maps.GeocoderStatus.OK) {
                    if (makemarker) {
                        var marker = new google.maps.Marker({
                            map: daydetails.map,
                            position: results[0].geometry.location
                        });
                        google.maps.event.addListener(marker, "click", function() {
                            var event = daydetails.eventid_locationsmap[address];
                            if (!!event) {
                                var photos = daydetails.eventid_photosmap[event.id];
                                if (!!photos) {
                                    var contentstring = '';
                                    $.each(photos, function(i, e) {
                                        contentstring += e.html();
                                    });
                                    var infowindow = new google.maps.InfoWindow({
                                        content: contentstring,
                                        maxWidth: 95
                                    });
                                    infowindow.open(daydetails.map, marker);
                                }
                            }
                        });
                        daydetails.eventmarkers.push(marker);
                    }

                    daydetails.limits.extend(new google.maps.LatLng(results[0].geometry.location.kb, results[0].geometry.location.lb));
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
            new google.maps.places.Autocomplete(document.getElementById('hotellocation'));
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
