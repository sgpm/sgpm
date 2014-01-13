<%--
The MIT License (MIT)

Copyright (c) 2013 Andreas Alanko, Emil Nilsson, Fredrik Larsson, Joakim Strand, Sandra Fridälv

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
--%>
 
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>Sprints - SGPM</title>
</head>
<body>
<div class="page-header"><h1>Sprints</h1></div>
<g:if test="${flash.sprintMessageSuccess}">
	<div class="alert alert-success">${flash.sprintMessageSuccess}</div>
</g:if>
<g:if test="${flash.sprintMessageError}">
	<div class="alert alert-danger">${flash.sprintMessageError}</div>
</g:if>
<g:if test="${sprints}">
<ul class="list-group">
	<li class="list-group-item">
		<div class="row">
  			<div class="col-md-5"><h4>Start Date</h4></div>
  			<div class="col-md-5"><h4>End Date</h4></div>
 	 		<div class="col-md-2">
 	 			<div class="row">
 	 				<div class="col-md-4"></div>
 	 				<div class="col-md-6"></div>
 	 			</div>
			</div>
		</div>
	</li>
	<g:each in="${sprints}" var="sprint">
	<li class="list-group-item">
		<div class="row">
  			<div class="col-md-5"><g:formatDate format="yyyy-MM-dd" date="${sprint.startDate}"/></div>
  			<div class="col-md-5"><g:formatDate format="yyyy-MM-dd" date="${sprint.endDate}"/></div>
 	 		<div class="col-md-2">
 	 			<div class="row">
 	 				<div class="col-md-4"><g:link mapping="sprint_edit_form" id="${params.id}" params="[sprint: sprint.id]" class="label label-default">Edit</g:link></div>
 	 				<div class="col-md-6">
 	 					<g:link mapping="sprint_delete" id="${params.id}" params="[sprint: sprint.id]" class="label label-danger" 
 	 					onclick="return confirm('Are you sure you want to delete this sprint?');">Delete</g:link>
 	 				</div>
 	 			</div>
			</div>
		</div>
	</li>
	</g:each>
</ul>
</g:if>
<g:else>
	<g:render template="noSprints" model="${pageScope.variables}" />
</g:else>
</body>
</html>