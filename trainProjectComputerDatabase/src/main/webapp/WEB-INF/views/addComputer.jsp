<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<%@ include file="/WEB-INF/partials/includeCSS.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/partials/header.jsp" %>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<c:if test="${!empty validMessage}">
						<c:out value="${validMessage}" />
					</c:if>
					<form action="addComputer" method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name (required)</label> <input
									type="text" class="form-control" id="computerName"
									name="computerName"
									placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date (yyyy-mm-dd)</label> <input
									type="date" class="form-control" id="introduced"
									name="introduced"
									placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date (yyyy-mm-dd)</label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinued"
									placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label>
								<select name="companyId"
									class="form-control" id="companyId">
									<option value="">-- Select company --</option>
									<c:forEach var="company" items="${companies}">
										<option value="${company.id}">${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Add" class="btn btn-primary">
							or <a href="dashboard" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>