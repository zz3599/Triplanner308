
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
        <script src="../js/bootstrap.min.js"></script>
        </script>
        <script type="text/javascript">
        function search(){
            var input = document.getElementById("search").value;
            window.open("search.jsp?NAME="+input,'_self',false);
            //window.open("/search.jsp?NAME="+input,'newwindow','toolbar=yes,location=no,menubar=no,width=450,height=200,resizable=yes,scrollbars=yes,top=200,left=250');return false;;
            
        }
        function getURLParameter(name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
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
                                              
                    </div>
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <table class="table table-hover">
                <caption style="text-align: left; font-size: 20px; margin:5px;">Search Result: <c:out value="${param.NAME}"/> </caption>
            <thead >
              <tr>
                <th width="70px;">Trip Title</th>
                <th width="70px;">From</th>
                <th width="70px;">To</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>test3</td>
                <td>test4</td>
                <td>test4</td>
                <td>test4</td>
              </tr>
              <tr>
                <td>test3</td>
                <td>test4</td>
                <td>test4</td>
                <td>test4</td>
              </tr>
              <tr>
                <td>test3</td>
                <td>test4</td>
                <td>test4</td>
                <td>test4</td>
              </tr>
              <tr>
                <td>test3</td>
                <td>test4</td>
                <td>test4</td>
                <td>test4</td>
              </tr>
            </tbody>
          </table>
        </div>
    </body>
</html>


