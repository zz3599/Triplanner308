;
(function(world) {
    var TRIPURL = "trip";
    var TRIPEVENTS = "events";
    var TRIPDAY = "tripday";
    var PHOTO = "photo";
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
            app.initSpinner();
            app.initCalendar();
            app.initMap();
            app.initData();
            app.initHandlers();
            app.initTimepickers($('#startTime'), $('#endTime'), null, null);
            app.stopSpinner();
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
                app.postPhoto(formdata);
            });
            //update the page to show the data for the particular trip
            $('#yourtrips').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, e) {
                    $(e).removeClass('selected');
                });
                app.clearForms();
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
                app.initSpinner();
                $.get(TRIPEVENTS, {'tripid': app.tripid}).success(function(result) {
                    app.stopSpinner();
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
                        .append("<div class='span6'>\
                                <span id='photoprogress'></span>\
                                <label for=thumbnails'>Photo (click for album)</label><div id='thumbnails' class='span12'></div></div>"
                        );
                $('.hero-unit').append(row);
                //load all photos
                app.loadPhotos('trip', app.tripid);
            });
            //create new trip handler
            $('#createtrip').click(function(e) {
                e.preventDefault();
                app.initSpinner();
                $.post(TRIPURL, $('#newtrip').serialize()).success(
                        function(result) {
                            app.stopSpinner();
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
                app.initSpinner();
                $.post(TRIPDAY + '?action=update', $('#tripdayform').serialize()).success(
                        function(result) {
                            app.stopSpinner();
                            app.timeline.updateDayForm(result);
                        });
            });
        },
        loadPhotos: function(type, id) {
            app.initSpinner();
            $.get(PHOTO, {'type': type, 'id': id}).success(function(d) {
                app.stopSpinner();
                var data = JSON.parse(d);
                if ($.isEmptyObject(data))
                    return;
                $.each(data, function(i, e) {
                    var img = $('<div>', {
                        class: 'single',
                        style: 'float:left;'
                    }).append($('<a>', {
                        href: e.url,
                        rel: 'lightbox[album]',
                        title: e.comment
                    }).append($('<img>', {
                        src: e.url,
                        width: '50px',
                        height: '70px'
                    })));
                    $('#thumbnails').append(img);
                    if (i !== 0) //only need to show the first image as the thumbnail
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
                    if (parent.children('div').length === 0)
                        img.show();
                    img.appendTo(parent);

                }
            }
            app.initSpinner();
            xhr.open('POST', PHOTO, true);
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
        initSpinner: function() {
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
            var target = document.getElementById('hero');
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


