<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="addComputer.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
	<%@ include file="/WEB-INF/partials/includeCSS.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/partials/header.jsp" %>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1><spring:message code="addComputer.label" /></h1>
					<c:if test="${!empty validMessage}">
						<c:out value="${validMessage}" />
					</c:if>
					<form id="formAddEditComputer" action="addComputer" method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message code="addComputer.name" /></label> <input
									type="text" class="form-control" id="computerName"
									name="computerName"
									placeholder="<spring:message code="addComputer.name.placeholder" />">
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message code="addComputer.introduced" /></label> <input
									type="date" class="form-control" id="introduced"
									name="introduced"
									placeholder="<spring:message code="addComputer.introduced.placeholder" />">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message code="addComputer.discontinued" /></label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinued"
									placeholder="<spring:message code="addComputer.discontinued.placeholder" />">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message code="addComputer.company" /></label>
								<select name="companyId"
									class="form-control" id="companyId">
									<option value=""><spring:message code="addComputer.defaultCompany" /></option>
									<c:forEach var="company" items="${companies}">
										<option value="${company.id}">${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="<spring:message code="addComputer.addButton" />" class="btn btn-primary">
							or <a href="dashboard" class="btn btn-default"><spring:message code="addComputer.cancelButton" /></a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/partials/includeJS.jsp" %>
</body>
</html>