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
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAUC9VWHP1FfCpjU5Rs-wpN7vRwSFp4-bw&sensor=true">
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
                            <li class="nav-header">Your Events</li>
                            <div id="yourevents">
                                <c:forEach items="${events}" var="e">
                                    <li id="event_${e.id}" starttime="${e.startTime}" endtime="${e.endTime}" 
                                        startlocation="${e.startLocation}" endlocation="${e.endLocation}" 
                                        comment="${e.comment}" tripdayid="${e.tripdayid}" >
                                        <a href="#">${e.comment}</a>
                                    </li>
                                </c:forEach>
                            </div>
                            <li class="active"><a href="#">Link</a></li>

                            <li class="nav-header">Sidebar</li>
                            <li><a href="#">Link</a></li>
                        </ul>
                    </div>
                </div>
                <div class="span10">
                    <div class="hero-unit">
                        <span>Trip details for 
                            <fmt:formatDate pattern="M-d-yyyy" 
                                            value="${tripday.date}" />
                        </span><br>
                        <form id="dayeditform">
                            Start Location: <input id="startlocation" name="startlocation" value="<c:out value="${tripday.startLocation}"></c:out>" readonly><br>
                            End Location:  <input id="endlocation" name="endlocation" value="<c:out value="${tripday.endLocation}" ></c:out>"readonly><br>
                            Description: <input id="description" name="description" value="<c:out value="${tripday.comment}"></c:out>" readonly><br>
                            <button type="button" id="editday">Edit Day</button>
                            <button type="button" id="submitday">Done</button>
                        </form>
                    </div>
                    <div class="row-fluid">
                        <div class="row-fluid">
                            <div id="mapwrapper"  class="span12">
                                <div style="height:500px;" id="map-canvas"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="../js/daydetails.js"></script>
    </body>
</html>
