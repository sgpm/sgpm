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
 
<%@page import="sgpm.SprintController"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<g:set var="pageTitle" value="Create Sprint" />
<g:set var="submitName" value="Create Sprint" />
<g:set var="submitMapping" value="sprint_create_form" />
<g:set var="submitButtonDisabled" value="${SprintController.submitDisabled(emailList, repoList)}" />
<g:set var="recurringChecked" value="${(endOption == 'recurring') ? true : false}" />
<g:set var="endDateChecked" value="${(endOption == 'endDate') ? true : false}" />
<g:set var="openEndedChecked" value="${(endOption == 'openEnded') ? true : false}" />

<g:if test="${!endOption}">
	<g:set var="recurringChecked" value="${true}" />
</g:if>

<g:if test="${!startDate}">
	<g:set var="startDate" value="${new Date()}" />
</g:if>

<g:if test="${!endDate}">
	<g:set var="endDate" value="${new Date() + 14}" />
</g:if>

<g:if test="${!intervalLength}">
	<g:set var="intervalLength" value="${14}" />
</g:if>

<g:if test="${edit}">
	<g:set var="pageTitle" value="Edit Sprint" />
	<g:set var="submitName" value="Save Sprint" />
	<g:set var="submitMapping" value="sprint_edit_form" />
</g:if>
<html>
<head>
<title>${pageTitle} - SGPM</title>
</head>
<body>
<div class="page-header"><h1>${pageTitle}</h1></div>
	<g:if test="${flash.formMessage}">
		<div class="alert alert-danger">${flash.formMessage}</div>
		${flash.formMessage = null}
	</g:if>
	<g:form mapping="${submitMapping}" id="${params.id}" params="${(edit) ? [sprint: params.sprint] : []}" class="form-horizontal" role="form">
	
	<g:hiddenField name="editValue" value="${edit}" />
	
	 <!--START DATE -->
	
	<div class="form-group">
		<label class="col-sm-2 control-label">Start:</label>
		<div class="col-sm-10">
			<g:datePicker name="startDate" value="${startDate}" precision="day" years="${2000..2100}" />
    	</div>
    </div>
    
     <!-- END DATE -->
    
    <div class="form-group">
		<label class="col-sm-2 control-label">End:</label>
		<div class="col-sm-10">
		<g:if test="${!edit}">
			<div class="row">
				<div class="col-md-3"><g:radio name="endOption" value="recurring" checked="${recurringChecked}"/> Recurring:</div>
				<div class="col-md-4">Number of sprints: <g:select name="nbrOfSprints" from="${1..20}" value="${nbrOfSprints}"/></div>
				<div class="col-md-4">Sprint interval length: <g:select name="intervalLength" from="${1..60}" value="${intervalLength}"/></div>
			</div>
		</g:if>
		<g:else>
			<div class="row">
				<div class="col-md-3"><g:radio name="endOption" value="endDate" checked="${endDateChecked}"/> Specific end date:</div>
				<div class="col-md-6"><g:datePicker name="endDate" value="${endDate}" precision="day" years="${2000..2100}" /></div>
			</div>
		</g:else>
			<br>
			<g:radio name="endOption" value="openEnded" checked="${openEndedChecked}"/> Open-ended
    	</div>
    </div>
    
     <!-- ADD MEMBER -->

	<div class="form-group">
		<label class="col-sm-2 control-label">Members:</label>
		
		<div class="col-sm-10">
		<g:if test="${emailList}">
			<g:each in="${emailList}" var="email">
  				<div class="checkbox">
  					<label>
  						<g:checkBox name="emailList" value="${email}" />${email}
  					</label>
				</div>
  			</g:each>
  		</g:if>
			<br>
			<g:if test="${flash.emailError}">
				<div class="alert alert-danger">${flash.emailError}</div>
				${flash.emailError = null}
			</g:if>
			<div class="input-group">
				<span class="input-group-addon">@</span>
				<g:textField name="emailToAdd" placeholder="Email" class="form-control" />
			</div>
    	</div>
    </div>
    
  	<div class="form-group">
   		<div class="col-sm-offset-2 col-sm-10">
			<g:actionSubmit controller="sprints" value="Add member" action="processSubmit" class="btn btn-default" />
    	</div>
    </div>
    
    
    <!-- ADD REPOSITRY -->
    
    <div class="form-group">
		<label class="col-sm-2 control-label">Repositories:</label>
		
		<div class="col-sm-10">
		<g:if test="${repoList}">
			<g:each in="${repoList}" status="i" var="repo">
  				<div class="checkbox">
  					<label>
  						<g:checkBox name="repoList" value="${repo}" />${repo}
  					</label>
				</div>
  			</g:each>
  		</g:if>
			<br>
			<g:if test="${flash.repoError}">
				<div class="alert alert-danger">${flash.repoError}</div>
				${flash.repoError = null}
			</g:if>
			<div class="input-group">
				<span class="input-group-addon">/../..</span>
				<g:textField name="repoToAdd" placeholder="Repository" class="form-control" />
			</div>
    	</div>
    </div>
    
  	<div class="form-group">
   		<div class="col-sm-offset-2 col-sm-10">
			<g:actionSubmit value="Add repository" action="processSubmit" class="btn btn-default" />
    	</div>
    </div>
	<br><br>
	<div class="form-group">
   		<div class="col-sm-10">
   			<g:actionSubmit value="${submitName}" action="processSubmit" class="btn btn-primary" disabled="${submitButtonDisabled}" />
    	</div>
    </div>
</g:form>
</body>
</html>