<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ attribute name="itemByPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="pageNum" required="true" type="java.lang.Integer" %>
<%@ attribute name="maxPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="startPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="endPage" required="true" type="java.lang.Integer" %>
	
	<div class="container text-center">
			<ul class="pagination">
				<li><a href="<c:url value="dashboard">
						<c:param name="pageNum" value="1"/>
						<c:param name="itemByPage" value="${itemByPage}"/>
					</c:url>" aria-label="Last"> <span aria-hidden="true">&larrb;</span>
				</a></li>
				<c:choose>
					<c:when test="${pageNum > 1}">
						<li><a href="<c:url value="dashboard">
								<c:param name="pageNum" value="${pageNum - 1}"/>
								<c:param name="itemByPage" value="${itemByPage}"/>
							</c:url>" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="#" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
					</c:otherwise>
				</c:choose>
			
				<c:forEach var="i" begin="${startPage}" end="${endPage}" step="1">
			    	<a href="<c:url value="dashboard">
						<c:param name="pageNum" value="${i}"/>
						<c:param name="itemByPage" value="${itemByPage}"/>
					</c:url>">${i}</a>
				</c:forEach>
				
				<c:choose>
					<c:when test="${pageNum < maxPage}">
						<li><a href="<c:url value="dashboard">
								<c:param name="pageNum" value="${pageNum + 1}"/>
								<c:param name="itemByPage" value="${itemByPage}"/>
							</c:url>" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
					</c:otherwise>
				</c:choose>
				<li><a href="<c:url value="dashboard">
						<c:param name="pageNum" value="${maxPage}"/>
						<c:param name="itemByPage" value="${itemByPage}"/>
					</c:url>" aria-label="Last"> <span aria-hidden="true">&rarrb;</span>
				</a></li>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<form action="" method="GET">
					<input type="hidden" name="pageNum" value="${pageNum}">
					<button type="submit" name="itemByPage" class="btn btn-default" value="10">10</button>
					<button type="submit" name="itemByPage" class="btn btn-default" value="50">50</button>
					<button type="submit" name="itemByPage" class="btn btn-default" value="100">100</button>
				</form>
			</div>
		</div>