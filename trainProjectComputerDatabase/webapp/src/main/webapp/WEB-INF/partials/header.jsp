<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<c:url var="logoutUrl" value="/logout" />
			<form class="navbar-form navbar-right" action="${logoutUrl}"
				method="post">
				<button type="submit" class="btn btn-default"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-log-out" aria-hidden="true">
						<spring:message code="logout.buttonLabel" />
					</span>
				</button>
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>
		</c:if>
		<a class="icon-bar" href="dashboard?lang=en"> <img alt="en"
			title="English"
			src="<c:url value="/images/drapeau-grande-bretagne-uk-royaume-uni-icone-4330-32.png"/>">
		</a> <a class="icon-bar" href="dashboard?lang=fr"> <img alt="fr"
			title="French" src="<c:url value="/images/drapeau_france.png"/>">
		</a> <a class="navbar-brand" href="dashboard"> Application - Computer
			Database </a>
	</div>
</header>