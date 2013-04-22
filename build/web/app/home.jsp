
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
    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a class="brand" href="#">Triplanner</a>
                    <div class="nav-collapse collapse">
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
                <div class="span3">
                    <!--Sidebar content-->
                    <div class="well sidebar-nav">
                        <ul class="nav nav-list">
                            <li id="yourtrips" class="nav-header">Your Trips</li>
                            <li class="active"><a href="#">Link</a></li>
                            
                            <li class="nav-header">Sidebar</li>
                            <li><a href="#">Link</a></li>
                            <li><a href="#">Link</a></li>
                            <li>Hi</li>
                        </ul>
                    </div><!--/.well -->
                    <div id="setting">
                        <form id="newtrip">
                            <div>Title: </div>
                            <input name="title" class="settingInput"/>
                            <div>Description</div>
                            <input name="description" class="settingInput"/>
                            <div>Start location</div>
                            <input id="startLocation" name="startLocation" class="settingInput required"/>
                            <div>End location</div>
                            <input id="endLocation" name="endLocation" class="settingInput"/>
                            <div>Start time</div>
                            <input id="startTime" name="startTime" class="settingInput startTime required"/>
                            <div>End time</div>
                            <input id="endTime" name="endTime" class="settingInput endTime required"/><br>
                            <input id="createtrip" type="submit" value="Create" class="submit"/>
                        </form>
                    </div>
                </div>

                <div class="span9">
                    <div class="hero-unit">
                        
                    </div>
                    <div class="row-fluid">
                        <div id="wrap">
                            <div id="timeline">
                                <ul>            
                                    <li class="July first, don't forget!" title="Wed Jul 1 2009">Microsoft wants to buy yahoo. What do you think?</li>
                                    <li class="Idependance Day" title="Sat Jul 4 2009">Happy independance day America</li>
                                    <li class="Jenny day" title="Sun Jul 19 2009">Must get the biggest toy I can to make Jenny really happy :)</li>
                                    <li class="American Pie 4 is coming out" title="Thu Jul 23 2009">We really need to watch this movie. The ratings are so high!</li>
                                    <li class="DenonStudio is shows off the new Timeline calendar" title="Wed Jul 22 2009">This script is really hot. Can't wait to have it</li>
                                    <li class="ThemeForest launches the JavaScript component" title="Fri Jul 31 2009">Make sure to check regularly for hot new scripts</li>
                                    <li class="Dentist appointment" title="Wed Jul 22 2009">4:35 pm. Make sure not to eat anything for the last 24 hours. Got I hope I don't die :|</li>
                                </ul>    
                            </div>
                        </div>
                        <div class="span4">
                            <h2>Heading</h2>
                            <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                            <p><a class="btn" href="#">View details &raquo;</a></p>
                        </div><!--/span-->
                        <div class="span4">
                            <h2>Heading</h2>
                            <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                            <p><a class="btn" href="#">View details &raquo;</a></p>
                        </div><!--/span-->
                        <div class="span4">
                            <h2>Heading</h2>
                            <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
                            <p><a class="btn" href="#">View details &raquo;</a></p>
                        </div><!--/span-->
                    </div><!--/row-->                    
                </div>
            </div>
        </div>

        <script src="../js/app.js"></script>
    </body>
</html>


