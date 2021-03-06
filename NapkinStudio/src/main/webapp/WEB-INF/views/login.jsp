<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login page</title>
		<link href="<c:url value='/static/css/bootstrap.css' />"  rel="stylesheet"></link>
		<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
		<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
		
<!-- 		<link href="/static/css/bootstrap.css" rel="stylesheet"></link> -->
<!-- 		<link href="../../static/css/app.css" rel="stylesheet"></link> -->
<%-- 		<link href="<c:url value='../../static/css/bootstrap.css' />"  rel="stylesheet" type="text/css"></link> --%>
<%-- 		<link href="<c:url value='../../static/css/app.css' />"  rel="stylesheet" type="text/css"></link> --%>
<%-- 		<link href="<c:url value='/static/css/bootstrap.css' />"  rel="stylesheet"></link> --%>
<%-- 		<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link> --%>
<!-- 		<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" /> -->
<!-- 		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"> -->
	</head>

<body onload='document.getElementById("username").focus();'>
	
		<div id="mainWrapper">
			<div class="login-container">
				<div class="login-card">
					<div class="login-form">
						<c:url var="loginUrl" value="/login" />
						<form action="${loginUrl}" method="post" class="form-horizontal">
<%-- 							<c:if test="${param.error != null}"> --%>
<!-- 								<div class="alert alert-danger"> -->
<!-- 									<p>Invalid username and password.</p> -->
<!-- 								</div> -->
<%-- 							</c:if> --%>
							<c:if test="${not empty error}">
								<div class="alert alert-danger">
									<p>${error}</p>
								</div>
							</c:if>
<%-- 							<c:if test="${not empty message}"> --%>
<!-- 								<div class="alert alert-danger"> -->
<%-- 									<p>${message}</p> --%>
<!-- 								</div> -->
<%-- 							</c:if> --%>
							<c:if test="${param.logout != null}">
								<div class="alert alert-success">
									<p>You have been logged out successfully.</p>
								</div>
							</c:if>
							
							<div class="input-group input-sm">
								<label class="input-group-addon" for="username"><i class="fa fa-user"></i></label>
								<input type="text" class="form-control" id="username" name="username" placeholder="Enter Username" required>
							</div>
							<div class="input-group input-sm">
								<label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label> 
								<input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
							</div>
							<div class="input-group input-sm">
                              <div class="checkbox">
                                <label><input type="checkbox" id="rememberme" name="remember-me"> Remember Me</label>  
                              </div>
                            </div>
							<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
								
							<div class="form-actions">
								<input type="submit"
									class="btn btn-block btn-primary btn-default" value="Log in"/>

								<a href="<spring:url value="/register"/>" class="btn btn-block btn-primary btn-default">Register</a>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>
<!-- 	<h3>NapkinStudio</h3> -->

<%-- 	<c:if test="${not empty error}"><div>${error}</div></c:if> --%>
<%-- 	<c:if test="${not empty message}"><div>${message}</div></c:if> --%>

<%-- 	<form name='login' action="<c:url value='/login' />" method='POST'> --%>
<!-- 	    <table> -->
<!-- 	        <tr> -->
<!-- 	            <td>UserName:</td> -->
<!-- 	            <td><input type='text' name='username' value=''></td> -->
<!-- 	        </tr> -->
<!-- 	        <tr> -->
<!-- 	            <td>Password:</td> -->
<!-- 	            <td><input type='password' name='password' /></td> -->
<!-- 	        </tr> -->
<!-- 	        <tr> -->
<!-- 	            <td colspan='2'><input name="submit" type="submit" value="submit" /></td> -->
<!-- 	        </tr> -->
<!-- 	    </table> -->
<%-- 	    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
<!-- 	</form> -->
	
	
</body>
</html>
