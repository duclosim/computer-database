<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="pageNum" required="false" type="java.lang.Integer" %>
<%@ attribute name="itemByPage" required="false" type="java.lang.Integer" %>

<%@ attribute name="orderWay" required="false" type="java.lang.String" %>
<%@ attribute name="column" required="false" type="java.lang.String" %>

<%@ attribute name="search" required="false" type="java.lang.String" %>

<c:url value="dashboard">
	<c:param name="pageNum" value="${pageNum}"/>
	<c:param name="itemByPage" value="${itemByPage}"/>
	<c:param name="column" value="${column}"/>
	<c:param name="orderWay" value="${orderWay}"/>
	<c:param name="search" value="${search}"/>
</c:url>
