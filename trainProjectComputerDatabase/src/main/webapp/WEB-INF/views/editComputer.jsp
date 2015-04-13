<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="editComputer.title" /></title>
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
					<div class="label label-default pull-right">id: ${computerBean.id}</div>
					<h1><spring:message code="editComputer.label" /></h1>
					<c:if test="${!empty errorMessage}">
						<c:out value="${errorMessage}" />
					</c:if>
					<c:if test="${!empty validMessage}">
						<c:out value="${validMessage}" />
					</c:if>
					<form id="formAddEditComputer" action="editComputer" method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message code="addComputer.name" /></label> <input
									type="text" class="form-control" id="computerName"
									name="computerName"
									placeholder="Computer name" value="${computerBean.name}">
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message code="addComputer.introduced" /></label> <input
									type="date" class="form-control" id="introduced"
									name="introduced"
									placeholder="Introduced date" value="${computerBean.introducedDate}">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message code="addComputer.discontinued" /></label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinued"
									placeholder="Discontinued date" value="${computerBean.discontinuedDate}">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message code="addComputer.company" /></label>
								<select name="companyId"
									class="form-control" id="companyId">
									<option value=""><spring:message code="addComputer.defaultCompany" /></option>
									<c:forEach var="company" items="${companies}">
										<option <c:if test="${company.id == computerBean.companyId}">selected="selected"</c:if> value="${company.id}">${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="<spring:message code="editMode.button" />" class="btn btn-primary">
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