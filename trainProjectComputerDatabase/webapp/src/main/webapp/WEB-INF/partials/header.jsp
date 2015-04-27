<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<a id="en_ic" class="icon-bar" href="dashboard?lang=en"> <img alt="en"
			title="English"
			src="<c:url value="/images/drapeau-grande-bretagne-uk-royaume-uni-icone-4330-32.png"/>">
		</a> <a id="fr_ic" class="icon-bar" href="dashboard?lang=fr"> <img alt="fr"
			title="French" src="<c:url value="/images/drapeau_france.png"/>">
		</a> <a class="navbar-brand" href="dashboard"> Application - Computer
			Database </a>
	</div>
</header>