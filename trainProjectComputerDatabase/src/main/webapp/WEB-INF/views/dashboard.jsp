<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="p" %>
    
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
	<%@ include file="/WEB-INF/partials/includeCSS.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/partials/header.jsp" %>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle"><c:out value="${page.totalNbEntities}" /></h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="dashboard" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->
						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th>Computer name
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.name" orderWay="ASC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-up"/></i></a>
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.name" orderWay="DESC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-down"/></i></a>
						</th>
						<!-- Table header for Introduced date -->
						<th>Introduced date
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.introduced" orderWay="ASC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-up"/></i></a>
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.introduced" orderWay="DESC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-down"/></i></a>
						</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.discontinued" orderWay="ASC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-up"/></i></a>
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="computer.discontinued" orderWay="DESC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-down"/></i></a>
						</th>
						<!-- Table header for Company -->
						<th>Company
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="company.name" orderWay="ASC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-up"/></i></a>
							<a href="<p:navLink pageNum="${page.pageNum}" itemByPage="${page.maxNbItemsByPage}" 
								column="company.name" orderWay="DESC" search="${page.searchedName}">
							</p:navLink>" onclick=""><i class="fa fa-long-arrow-down"/></i></a>
						</th>
					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="item" items="${page.entities}">
						<tr>
							<td class="editMode"><input id="computerRm${item.id}" 
								type="checkbox" name="cb"
								class="cb" value="${item.id}"></td>
							<td><a id="computer${item.id}" href="<c:url value="editComputer">
								<c:param name="beanId" value="${item.id}"/>
							</c:url>" onclick="">${item.name}</a></td>
							<td>${item.introducedDate}</td>
							<td>${item.discontinuedDate}</td>
							<td>${item.companyName}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<p:paging page="${page}"/>
	</footer>
	<%@ include file="/WEB-INF/partials/includeJS.jsp" %>
</body>
</html>