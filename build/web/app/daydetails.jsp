<%-- 
    Document   : daydetails
    Created on : May 12, 2013, 2:37:43 PM
    Author     : brook
--%>

<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Day Details - 
            <fmt:formatDate pattern="MM-dd-yyyy" 
                            value="${tripday.date}" />
        </title>
        <link type="text/css" rel="stylesheet" href="../css/style.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/jquery-ui-1.9.2.custom.min.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/timeline.css"/>
        <link type-="text/css" rel="stylesheet" href="../css/bootstrap.min.css"/>
        <link type="text/css" rel="stylesheet" href="../css/lightbox.css"/>
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
            .sidebar-nav {
                padding: 9px 0;
            }
        </style>
        <script src="../js/jquery-1.8.3.min.js"></script>
        <script src="../js/jquery-ui-1.9.2.custom.min.js"></script>
        <script src="../js/jquery-ui-timepicker-addon.js"></script>
        <script src="../js/mustache.min.js"></script>
        <script src="../js/timeline.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <script src="../js/spin.min.js"></script>
        <script src="../js/lightbox.js"></script>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAUC9VWHP1FfCpjU5Rs-wpN7vRwSFp4-bw&sensor=true&libraries=places">
        </script>
    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a class="brand" href="home">Triplanner</a>
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
                            <li class="nav-header">Your Events</li>                            
                            <div id="yourevents">
                            </div>
                        </ul>
                    </div>
                    <div id="setting">
                        <input type="button" id="addevent" value="Add Event">
                        <input type="button" id="addphotos" value="Add photos">
                        <input type="button" id="viewalbum" value="View Album">
                        <form id="waypointform">
                            Waypoints: <br>
                            <ul id="waypointsortable">
                            </ul>
                        </form>
                        <div id="uploadphotodiv" style="display:none;">
                            Upload a photo for this day
                            <form enctype='multipart/form-data'>
                                <label for='description'>Photo Description</label>
                                <input type='text' id='photodescription' name='description' class="settingInput"/>
                                <input type='file' id='photofile' name='file' />
                                <input type='hidden' name='eventid' id='photoeventid' class="settingInput"/>
                                <input type='hidden' name='tripdayid' id='phototripdayid' class="settingInput" value="<c:out value="${tripday.id}"></c:out>"/>
                                    Photo event: <input type='text' name='eventdescription' id='eventdescription' class="settingInput" readonly>
                                    Photo date: <input type='text' name='daydescription' id='daydescription' class="settingInput" value="<fmt:formatDate pattern="M-d-yyyy" value="${tripday.date}" />" readonly>
                                <input type='submit' id='addphoto' value='Add photo'></form>

                        </div>
                    </div>
                </div>
                <div class="span10">
                    <div class="hero-unit" id="hero">
                        <div class="row-fluid">
                            <div class="span4">
                                <span class="form">Trip details for 
                                    <fmt:formatDate pattern="M-d-yyyy" 
                                                    value="${tripday.date}" />
                                </span><br>
                                <form id="dayeditform">
                                    <input id="tripid" name="tripid" value="<c:out value="${tripday.tripid}"></c:out>" type="hidden">
                                    <input id="tripdayid" name="tripdayid" value="<c:out value="${tripday.id}"></c:out>" type="hidden">
                                    <input id="daynum" name="daynum" value="<c:out value="${tripday.daynum}"></c:out>" type="hidden">
                                    <input id="date" name="date" value="<c:out value="${tripday.date}"></c:out>" type="hidden">
                                    <input id="startlocation" name="startlocation" value="<c:out value="${tripday.startLocation}"></c:out>" type="hidden">
                                    <input id="endlocation" name="endlocation" value="<c:out value="${tripday.endLocation}" ></c:out>"type="hidden">
                                    <input id="description" name="description" value="<c:out value="${tripday.comment}"></c:out>" type="hidden">
                                    <span class="form">Start Location: <c:out value="${tripday.startLocation}"></c:out></span><br>
                                    <span class="form">End Location:<c:out value="${tripday.endLocation}"></c:out> </span> <br>
                                    <span class="form">Description:<c:out value="${tripday.comment}"></c:out> </span> <br>

                                </form>
                            </div>
                            <div class="span4">
                                Event info
                                <form id="eventform">
                                    <input id="eventid" name="eventid" type="hidden">
                                    <span class="form required"> Start Location: </span><input id="eventstartlocation" name="eventstartlocation"  readonly><br>
                                    <span class="form"> End Location:</span>  <input id="eventendlocation" name="eventendlocation" readonly><br>
                                    <span class="form required"> Description:</span> <input id="eventdescription" name="eventdescription" readonly><br>
                                    <span class="form required"> Start Time:</span> <input id="eventstarttime" name="eventstarttime" readonly><br>
                                    <span class="form required"> End Time:</span> <input id="eventendtime" name="eventendtime" readonly><br>
                                    <button type="button" id="editevent">Edit Event</button>
                                    <button type="button" id="submitevent">Done</button>
                                </form>
                                <div id="errors"></div>
                            </div>
                            <div class="span4">
                                Hotel info
                                <form id="hotelform">
                                    <input id="eventid" name="eventid" type="hidden">
                                    Location: <input id="hotellocation" name="location"  readonly><br>
                                    <button type="button" id="edithotel">Edit Hotel</button>
                                    <button type="button" id="submithotel">Done</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="row-fluid">
                            <div id="mapwrapper"  class="span12">
                                <div style="height:500px;" id="map-canvas"/>
                            </div>
                        </div>
                        <div id="thumbnails"></div>
                    </div>
                </div>
            </div>
        </div>
        <script src="../js/daydetails.js"></script>
    </body>
</html>
