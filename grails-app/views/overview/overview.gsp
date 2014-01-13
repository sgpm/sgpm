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
<title>Overview - SGPM</title>
</head>
<body>
	<div class="page-header"><h1>Overview</h1></div>
	<g:if test="${flash.newTeam}">
		<div class="alert alert-success">
			<h2>Congratulations, the team ${flash.newTeam?.name} has been created! </h2>
			<p>Don't forget to save your URL: <g:linkTeam url="${flash.newTeam?.url}" />.</p>
		</div>
	</g:if>
	<g:if test="${flash.sprintMessageSuccess}">
		<div class="alert alert-success">${flash.sprintMessageSuccess}</div>
	</g:if>
	<g:if test="${flash.sprintMessageError}">
		<div class="alert alert-danger">${flash.sprintMessageError}</div>
	</g:if>

	<g:if test="${sprints}">
		<%-- the form used to select the sprints --%>
		<g:render template="sprintSelectionForm"/>

		<g:if test="${data}">
			<%-- visualize selected sprints --%>
			<g:render template="charts"/>
		</g:if>
	</g:if>
	<g:else>
		<%-- no sprints created, ask the user to create some --%>
		<g:render template="/sprint/noSprints" model="${pageScope.variables}" />
	</g:else>
</body>
</html>