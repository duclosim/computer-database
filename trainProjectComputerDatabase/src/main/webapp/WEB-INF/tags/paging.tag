<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="p" %>

<%@ attribute name="page" required="true" type="com.excilys.computerDatabase.model.Page"%>

<div class="container text-center">
	<ul class="pagination">
		<c:if test="${page.pageNum > 1}">
			<li><a href="<p:navLink pageNum="1" itemByPage="${page.maxNbItemsByPage}" 
						column="${page.column}" orderWay="${page.way}" search="${page.searchedName}">
					</p:navLink>" aria-label="First" id="First"> <span aria-hidden="true">&larrb;</span>
			</a></li>
			<li><a href="<p:navLink pageNum="${page.pageNum - 1}" itemByPage="${page.maxNbItemsByPage}" 
						column="${page.column}" orderWay="${page.way}" search="${page.searchedName}">
					</p:navLink>" aria-label="Previous" id="Previous"> <span aria-hidden="true">&laquo;</span>
			</a></li>
		</c:if>

		<c:forEach var="i" begin="${page.getStartingPage()}" end="${page.getFinishingPage()}" step="1">
			<li <c:if test="${page.pageNum == i}">class="active"</c:if>>
				<a href="<p:navLink pageNum="${i}" itemByPage="${page.maxNbItemsByPage}" 
					column="${page.column}" orderWay="${page.way}" search="${page.searchedName}">
				</p:navLink>">${i}</a>
			</li>
		</c:forEach>

		<c:if test="${page.pageNum < page.lastPageNb}">
			<li><a href="<p:navLink pageNum="${page.pageNum + 1}" itemByPage="${page.maxNbItemsByPage}" 
					column="${page.column}" orderWay="${page.way}" search="${page.searchedName}">
				</p:navLink>" aria-label="Next" id="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
			<li><a href="<p:navLink pageNum="${page.lastPageNb}" itemByPage="${page.maxNbItemsByPage}" 
					column="${page.column}" orderWay="${page.way}" search="${page.searchedName}">
				</p:navLink>" aria-label="Last" id="Last"> <span aria-hidden="true">&rarrb;</span>
			</a></li>
		</c:if>
	</ul>

	<div class="btn-group btn-group-sm pull-right" role="group">
		<form action="" method="GET">
			<input type="hidden" name="pageNum" value="1">
			<input type="hidden" name="column" value="${page.column}"/>
			<input type="hidden" name="orderWay" value="${page.way}"/>
			<input type="hidden" name="search" value="${page.searchedName}"/>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="10">10</button>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="50">50</button>
			<button type="submit" name="itemByPage" class="btn btn-default"
				value="100">100</button>
		</form>
	</div>
</div>