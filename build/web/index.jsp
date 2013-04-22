<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Triplanner</title>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
        <meta http-equiv="content-language" content="en" />
        <meta name="description" content="" />
        <meta name="language" content="en" />
        <link type="text/css" rel="stylesheet" href="./css/style.css"/>
    </head>
    <body>

        <form id="login_frm" action="login" method="post">
            <table id="head">
                <tr>
                    <td>
                        <a href="index.xhtml"><img style="margin-left:10%;" src="images/logo.png"/></a>
                    </td>

                    <td>
                        <table id="loginbox">
                            <tr>
                                <td width="40%">
                                    <span style="font-size: 16px;">Email:</span><br/><br/>
                                    <input name="loginemail" class="login"/>
                                </td>
                                <td width="40%" style="margin-left: 10px;">
                                    <span style="font-size: 16px;">Password:</span><br/><br/>
                                    <input name="loginpassword" type="password" class="password"/>
                                </td>
                                <td width="20%">
                                    <input id="login" value="Login" type="submit" class="submit"/>
                                </td>
                            </tr>
                            <tr>
                                <div style="color:red;">
                                    <c:out value="${requestScope.errorMessage}"/>
                                </div>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
        <form id="register_frm" action="register" method="post">
            <center>
                <table id="mainTable">
                    <tr>
                        <td>
                            <img src="images/indexmap.png"/>
                        </td>
                        <td>
                            <table id="signup">
                                <tr><td id="siupupImg"><img src="images/signUp.png" style="margin-left: 5px; margin-bottom: 20px"/></td></tr>
                                <tr>   
                                    <td width="80" align="left">
                                        <span style="font-size: 16px; margin-left:5px;" class="required">First:</span><br/>
                                        <input name="firstname" class="input_text"/></td>
                                    <td width="80" align="left">
                                        <span style="font-size: 16px; margin-left:5px;" class="required">Last:</span><br/>
                                        <input name="lastname" class="input_text"/></td>
                                </tr>
                                <tr>
                                    <td colspan="2"  align="left">
                                        <span style="font-size: 16px; margin-left:5px;" class="required">Email:</span><br/>
                                        <input name="email" class="input_long"  />
                                    </td>    
                                </tr>
                                <tr>
                                    <td colspan="2"  align="left">
                                        <span style="font-size: 16px; margin-left:5px;" class="required">Password:</span><br/>
                                        <input name="password" type="password" class="input_long"/>
                                    </td>    
                                </tr>
                                <tr>
                                    <td width="80" align="left">
                                        <input id="register" type="submit" value="Register"/>
                                    </td>    
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </center>
        </form>
        <br />
       
    </body>

</html>

