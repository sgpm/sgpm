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

<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><g:layoutTitle default="SGPM" /></title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"
	type="text/css">
<g:layoutHead />
<r:require module="bootstrap" />
<r:layoutResources />
</head>
<body>
	<div id="wrap">
		<g:render template="/layouts/navbar" />
		<div class="container">
			<nav class="nav navbar-default">
				<div class="navbar-brand">
					<g:link mapping="overview" controller="overview" id="${params.id}" action="overview">Start</g:link>
				</div>
				<ul class="nav navbar-nav">
					<li><g:link mapping="sprint_show" controller="sprint" id="${params.id}" action="edit">Sprints</g:link></li>
					<li><g:link mapping="sprint_create_form" controller="sprint" id="${params.id}" action="create">Create Sprint</g:link></li>
				</ul>
			</nav>
		</div>
		
		<div class="container">
			<g:layoutBody />
		</div>
	</div>
	<g:render template="/layouts/footer" />


	<g:javascript library="application" />
	<r:layoutResources />
</body>
</html>
