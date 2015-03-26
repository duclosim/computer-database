<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="page" required="true" type="com.excilys.computerDatabase.model.page.Page"%>
<%@ attribute name="orderWay" required="false" type="java.lang.String" %>
<%@ attribute name="column" required="false" type="java.lang.String" %>

<div class="container text-center">
	<ul class="pagination">
		<c:if test="${page.pageNum > 1}">
			<li><a href="<c:url value="dashboard">
							<c:param name="pageNum" value="1"/>
							<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
							<c:param name="column" value="${column}"/>
							<c:param name="orderWay" value="${orderWay}"/>
						</c:url>"
				aria-label="Last"> <span aria-hidden="true">&larrb;</span>
			</a></li>
			<li><a href="<c:url value="dashboard">
							<c:param name="pageNum" value="${page.pageNum - 1}"/>
							<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
							<c:param name="column" value="${column}"/>
							<c:param name="orderWay" value="${orderWay}"/>
						</c:url>"
				aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
			</a></li>
		</c:if>

		<c:forEach var="i" begin="${page.getStartingPage()}" end="${page.getFinishingPage()}" step="1">
			<c:choose>
				<c:when test="${page.pageNum == i}">
					<li class="active"><a href="<c:url value="dashboard">
						<c:param name="pageNum" value="${i}"/>
						<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
						<c:param name="column" value="${column}"/>
						<c:param name="orderWay" value="${orderWay}"/>
					</c:url>">${i}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="<c:url value="dashboard">
						<c:param name="pageNum" value="${i}"/>
						<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
						<c:param name="column" value="${column}"/>
						<c:param name="orderWay" value="${orderWay}"/>
					</c:url>">${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:if test="${page.pageNum < page.lastPageNb}">
			<li><a href="<c:url value="dashboard">
							<c:param name="pageNum" value="${page.pageNum + 1}"/>
							<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
							<c:param name="column" value="${column}"/>
							<c:param name="orderWay" value="${orderWay}"/>
						</c:url>"
				aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
			<li><a href="<c:url value="dashboard">
							<c:param name="pageNum" value="${page.lastPageNb}"/>
							<c:param name="itemByPage" value="${page.maxNbItemsByPage}"/>
							<c:param name="column" value="${column}"/>
							<c:param name="orderWay" value="${orderWay}"/>
						</c:url>"
				aria-label="Last"> <span aria-hidden="true">&rarrb;</span>
			</a></li>
		</c:if>
	</ul>

	<div class="btn-group btn-group-sm pull-right" role="group">
		<form action="" method="GET">
			<input type="hidden" name="pageNum" value="1">
			<input type="hidden" name="column" value="${column}"/>
			<input type="hidden" name="orderWay" value="${orderWay}"/>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="10">10</button>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="50">50</button>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="100">100</button>
		</form>
	</div>
</div>