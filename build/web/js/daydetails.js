;
(function(world) {
    var daydetails = {
        yourevents: $('#yourevents'),
        dayform: $('#dayeditform'),
        init: function() {
            
            daydetails.initHandlers();
            daydetails.initMap();
        },
        initHandlers: function() {
            $('#yourevents').on('click', 'li', function(e) {
                $.each($(this).siblings(), function(i, elem) {
                    $(elem).removeClass('selected');
                });
                $(this).addClass('selected');
            });
            $('#editday').click(function(){
                daydetails.dayform.find('input').attr('readonly', function(i, attr) {
                    if(attr==='readonly') daydetails.edittrip = true;
                    else daydetails.edittrip = false;
                    return !attr;
                });
            });
            
        },
        initData: function() {

        },
        initMap: function(){
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
        }
    };

    daydetails.init();
    window.daydetails = daydetails;
})(window);