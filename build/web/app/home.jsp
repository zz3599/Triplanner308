
<!DOCTYPE html>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Triplanner</title>
        <link type="text/css" rel="stylesheet" href="../css/style.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/jquery-ui-1.9.2.custom.min.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/timeline.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/bootstrap.min.css"/>
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
            .sidebar-nav {
                padding: 9px 0;
            }
        </style>
        <script src="../js/jquery-1.9.1.min.js"></script>
        <script src="../js/jquery-ui-1.9.2.custom.min.js"></script>
        <script src="../js/jquery-ui-timepicker-addon.js"></script>
        <script src="../js/mustache.min.js"></script>
        <script src="../js/timeline.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAUC9VWHP1FfCpjU5Rs-wpN7vRwSFp4-bw&sensor=true">
        </script>
        <script type="text/javascript">
            function search() {
                var input = document.getElementById("search").value;
                window.open("search.jsp?NAME=" + input, '', 'directories=0,titlebar=0,toolbar=0,location=0,status=0,menubar=0,scrollbars=yes,resizable=no,width=800,height=800');
                //window.open("/search.jsp?NAME="+input,'newwindow','toolbar=yes,location=no,menubar=no,width=450,height=200,resizable=yes,scrollbars=yes,top=200,left=250');return false;;

            }
        </script>


    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a class="brand" href="#">Triplanner</a>
                    <div class="nav-collapse collapse">
                        <p class="navbar-text pull-left" style="margin-top:5px;">
                            <span><input id="search" type="text" class="search square" style="background-color:#353232; border-color: #b3b3b3;"><input type="button" value="Search" onclick="search()"></span>
                        </p>
                        <p class="navbar-text pull-right">
                            Logged in as <a href="#" class="navbar-link"><c:out value="${user.firstname}"/></a>
                        </p>
                        <!--                        <ul class="nav">
                                                    <li class="active"><a href="#">Home</a></li>
                                                    <li><a href="#about">About</a></li>
                                                    <li><a href="#contact">Contact</a></li>
                                                </ul>-->
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span2">
                    <!--Sidebar content-->
                    <div class="well sidebar-nav">
                        <ul class="nav nav-list">
                            <li class="nav-header">Your Trips</li>
                            <div id="yourtrips">

                            </div>
                            <li class="active"><a href="#">Link</a></li>

                            <li class="nav-header">Sidebar</li>
                            <li><a href="#">Link</a></li>
                        </ul>
                    </div><!--/.well -->
                    <div id="controls">
                        <input type="button" id="newtripbutton" value="Create Trip"/>
                        <input type="button" id="newphoto" value="Upload photos"/>
                    </div>
                    <div id="setting">

                        <div id="newtripinfo" style="display:none;">
                            <form id="newtrip">
                                <div>Title: </div>
                                <input name="title" class="settingInput"/>
                                <div>Description</div>
                                <input name="description" class="settingInput"/>
                                <div>Start location</div>
                                <input id="startLocation" name="startLocation" class="settingInput"/>
                                <div>End location</div>
                                <input id="endLocation" name="endLocation" class="settingInput"/>
                                <div>Start time</div>
                                <input id="startTime" name="startTime" class="settingInput startTime required"/>
                                <div>End time</div>
                                <input id="endTime" name="endTime" class="settingInput endTime required"/><br>
                                <input id="createtrip" type="submit" value="Create" class="submit"/>
                            </form>
                        </div>                        
                        <div id="uploadphotodiv" style="display:none;">
                            <form enctype='multipart/form-data'>
                                <label for='description'>Photo Description</label>
                                <input type='text' id='photodescription' name='description' class="settingInput"/>
                                <input type='file' id='photofile' name='file' />
                                <input type='hidden' name='eventid' id='photoeventid' class="settingInput"/>
                                <input type='hidden' name='tripdayid' id='phototripdayid' class="settingInput"/>
                                <input type='text' name='eventdescription' id='eventdescription' class="settingInput" readonly>
                                <input type='submit' id='addphoto' value='Add photo'></form>
                        </div>
                        <div id="timelineinfo" style="display:none;">
                            <form id="tripdayform">
                                Plan the day: <br>
                                <input id="id" name="id" type="hidden" />
                                <input id="tripid" name="tripid" type="hidden"/>
                                <input id="daynum" type="hidden" name="daynum" class="settingInput"/><br>
                                Date: <input id="date" name="date" class="settingInput"/><br>
                                Start: <input id="daystart" name="daystart" class="settingInput" /><br>
                                Destination: <input id="dayend" name="dayend" class="settingInput" /> <br>
                                Plans? <input id="comment" name="comment" class="settingInput"/> <br>
                                <input id="editday"  type="submit" value="Save"/>
                            </form>                            
                        </div>
                    </div>
                </div>

                <div class="span10">
                    <div class="hero-unit" id="hero">

                    </div>
                    <div class="row-fluid">
                        <div id="wrap">

                            <div id="timeline">
                                <ul>            
                                    <li class="July first, don't forget!" title="Wed Jul 1 2009">Microsoft wants to buy yahoo. What do you think?</li>
                                </ul>    
                            </div>
                            <div id="dialog-form" title="Plan event">
                                <p class="validateTips">All form fields are required.</p>
                                <form id="neweventform">
                                    <fieldset>
                                        <input type="hidden" id="eventtripid" name="eventtripid"/>
                                        <input type="hidden" id="eventtripdayid" name="eventtripdayid"/>
                                        <label for="description">Description</label>
                                        <input type="text" name="description" id="description" class="text ui-widget-content ui-corner-all" />
                                        <label for="eventstartLocation">Start Location</label>
                                        <input type="text" name="eventstartLocation" id="eventstartLocation" value="" class="text ui-widget-content ui-corner-all" />
                                        <label for="eventendLocation">End Location</label>
                                        <input type="text" name="eventendLocation" id="eventendLocation" value="" class="text ui-widget-content ui-corner-all" />
                                        <label for="eventStart">Start Time</label>
                                        <input type="text" name="eventStart" id="eventStart" value="" class="text ui-widget-content ui-corner-all" />
                                        <label for="eventEnd">End Time</label>
                                        <input type="text" name="eventEnd" id="eventEnd" value="" class="text ui-widget-content ui-corner-all" />
                                    </fieldset>
                                </form>
                            </div>

                        </div>
                        <div class="row-fluid">
                            <div id="mapwrapper"  class="span12">
                                <div style="height:500px;" id="map-canvas"/>
                            </div>
                        </div>
                    </div><!--/row-->                    
                </div>
            </div>
        </div>
        <script src="../js/app.js"></script>
        <script src="../js/timeline.min.js"></script>
    </body>
</html>


