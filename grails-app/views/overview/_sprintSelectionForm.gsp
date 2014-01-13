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
 
<g:form mapping="overview" id="${params.id}">
	<div id="content">
		<div class="row">
			<div class="col-md-5"><h4>From sprint</h4></div>
			<div class="col-md-5"><h4>To sprint</h4></div>
		</div>
		<div class="row">
			<div class="col-md-5">
				<g:select name="startSprint" from="${sprints}"
					value="${params.startSprint}" optionKey="id" class="form-control" />
			</div>
			<div class="col-md-5">
				<g:select name="endSprint" from="${sprints}"
					value="${params.endSprint}" optionKey="id" class="form-control" />
			</div>
			<div class="col-md-2">
				<g:submitButton name="submit" value="Generate"
					class="btn btn-primary" />
			</div>
		</div>
	</div>
</g:form>
