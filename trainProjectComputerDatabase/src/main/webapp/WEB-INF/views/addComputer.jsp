<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="addComputer.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<%@ include file="/WEB-INF/partials/includeCSS.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/partials/header.jsp"%>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>
						<spring:message code="addComputer.label" />
					</h1>
					<spring:message code="addComputer.namePlaceholder"
						var="namePlaceholder" />
					<spring:message code="addComputer.introducedPlaceholder"
						var="introducedPlaceholder" />
					<spring:message code="addComputer.discontinuedPlaceholder"
						var="discontinuedPlaceholder" />
					<form:form id="formAddEditComputer" method="POST"
						action="addComputer" commandName="computerForm">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="addComputer.name" /></label>
								<form:input name="computerName" path="name" class="form-control"
									id="computerName" placeholder="${namePlaceholder}" />
								<form:errors path="name" cssClass="error" />
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="addComputer.introduced" /></label>
								<form:input name="introduced" path="introducedDate"
									class="form-control" id="introduced"
									placeholder="${introducedPlaceholder}" />
								<form:errors path="introducedDate" cssClass="error" />
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="addComputer.discontinued" /></label>
								<form:input name="discontinued" path="discontinuedDate"
									class="form-control" id="discontinued"
									placeholder="${discontinuedPlaceholder}" />
								<form:errors path="discontinuedDate" cssClass="error" />
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="addComputer.company" /></label>
								<form:select name="companyId" path="companyId">
									<form:option value="">
										<spring:message code="addComputer.defaultCompany" />
									</form:option>
									<form:options items="${companies}" />
								</form:select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit"
								value="<spring:message code="addComputer.addButton" />"
								class="btn btn-primary"> or <a href="dashboard"
								class="btn btn-default"><spring:message
									code="addComputer.cancelButton" /></a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/partials/includeJS.jsp"%>
</body>
</html>