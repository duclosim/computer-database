<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="computerDatabase.mainTitle" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
	<%@ include file="/WEB-INF/partials/includeCSS.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/partials/header.jsp" %>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				<spring:message code="500.message" /><br />
				<!-- stacktrace -->
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/partials/includeJS.jsp" %>
</body>
</html>