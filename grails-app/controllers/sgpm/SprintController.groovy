/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andreas Alanko, Emil Nilsson, Fredrik Larsson, Joakim Strand, Sandra Fridälv
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package sgpm

import org.junit.After
import org.apache.commons.validator.EmailValidator

class SprintController {
	static defaultAction = 'showSprints'
	
	def showSprints() {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		def sprints = Sprint.allSprints(team)
		
		render view: 'index', model: [sprints: sprints]
	}
	
	def createForm() {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		def lastSprint = Sprint.previousSprint(team)
		
		def repoList = null
		def emailList = null
		def startDate = new Date()
		def intervalLength = 14
		
		if(lastSprint) {
			repoList = lastSprint.repos
			emailList = lastSprint.emails
			startDate = lastSprint.startDate + 1
			if(lastSprint.endDate) {
				startDate = lastSprint.endDate + 1
				intervalLength = lastSprint.length
			}
		}
		
		render view: "sprintForm", model: [emailList: emailList, repoList: repoList, startDate: startDate, intervalLength: intervalLength]
	}
	
	def editForm() {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		if(params.sprint == null) {
			response.sendError(404)
		}
		
		def sprintId
		
		try {
			sprintId = Integer.parseInt(params.sprint)
		} catch (NumberFormatException e) {
			response.sendError(404)
		}
		
		def sprint = Sprint.get(sprintId)
		
		def openEnded = (sprint.endDate == null) ? true : false
		def repoList = sprint.repos
		def emailList = sprint.emails
		
		def startDate = sprint.startDate
		def endDate = (sprint.endDate) ? sprint.endDate : startDate + 14
		def endOption = (openEnded) ? 'openEnded' : 'endDate'
		
		render view: 'sprintForm', model: [emailList: emailList, repoList: repoList, openEnded: openEnded, startDate: startDate, endDate: endDate, edit: true, endOption: endOption]
	}
	
	def deleteSprint() {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		if(params.sprint == null) {
			response.sendError(404)
		}
		
		def sprintId
		
		try {
			sprintId = Integer.parseInt(params.sprint)
		} catch (NumberFormatException e) {
			response.sendError(404)
		}
		
		def sprint = Sprint.get(sprintId)
		
		if(sprint.team == team) {
			sprint.delete()
			flash.sprintMessageSuccess = "Sprint was successfully deleted.";
		} else {
			response.sendError(404)
		}
		
		redirect(controller: "sprint", action: "showSprints", params: [id: params.id])
	}
	
	def processSubmit() {
		def emailList = null
		def repoList = null
		def emailToAdd = (params.emailToAdd) ? params.emailToAdd.replaceAll(" ", "") : params.emailToAdd
		def repoToAdd = (params.repoToAdd) ? params.repoToAdd.replaceAll(" ", "") : params.repoToAdd
		def openEnded = (params.endOption == 'openEnded') ? true : false
		def startDate = params.startDate
		def endDate = params.endDate
		def edit = params.editValue
		def endOption = params.endOption
		def nbrOfSprints = (params.nbrOfSprints == null) ? 1 : Integer.parseInt(params.nbrOfSprints)
		def intervalLength = (params.intervalLength == null) ? 14 : Integer.parseInt(params.intervalLength)
		
		if(params.repoList == null) {
			repoList = new ArrayList<Object>()
		} else {
			repoList = getClosureValues(params.repoList.toString())
		}
		
		if(params.emailList == null) {
			emailList = new ArrayList<Object>()
		} else {
			emailList = getClosureValues(params.emailList.toString())
		}
		
		/* processFormData */
		
		if(!emailToAdd.isEmpty()) {
			EmailValidator emailValidator = EmailValidator.getInstance()
			
			if(emailValidator.isValid(emailToAdd)) {
				if(!emailList.contains(emailToAdd)) {
					emailList.add(emailToAdd)
				} else {
					flash.emailError = "'" + emailToAdd + "' is already in the members list."
				}
			} else {
				flash.emailError = "'" + emailToAdd + "' is not a valid email address"
			}
		}
		
		if(!repoToAdd.isEmpty()) {
			if(!repoList.contains(repoToAdd)) {
				repoList.add(repoToAdd)
			} else {
				flash.repoError = "'" +  repoToAdd + "' is already in the repository list."
			}
		}
		
		def submitDisabled = submitDisabled(emailList, repoList)
		
		/** Map with all the params values */
		def paramsMap = [emailList: emailList, repoList: repoList, openEnded: openEnded, 
						startDate: startDate, endDate: endDate, edit: edit, 
						emailToAdd: emailToAdd, repoToAdd: repoToAdd, endOption: endOption,
						nbrOfSprints: nbrOfSprints, intervalLength: intervalLength]
		
		if(params._action_processSubmit == "Create Sprint") {
			if(!submitDisabled) {
				createNewSprint(paramsMap)
				redirect(controller: "overview", action: "overview", params: [id: params.id])
			} else {
				flash.formMessage = "Couldn't create a new sprint. Please check that you have fulfilled the requirements to create a new sprint."
			}
		} 
		
		else if(params._action_processSubmit == "Save Sprint") {
			if(!submitDisabled) {
				saveSprint(paramsMap)
				redirect(controller: "sprint", action: "showSprints", params: [id: params.id])
			} else {
				flash.formMessage = "Couldn't save the sprint. Please check that you have fulfilled the requirements to save the sprint."
			}
		}
		
		render view: "sprintForm", model: paramsMap
	}
	
	private void createNewSprint(Map data) {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		def newSprint
		boolean createComplete = true
		
		def startDate = data.startDate
		def intervalLength = data.intervalLength
		def endDate = (data.openEnded) ? null : startDate + intervalLength
		
		for (int i = 0; i < data.nbrOfSprints; i++) {
			newSprint = new Sprint(startDate: startDate, endDate: endDate, team: team)
			
			newSprint.repos = new ArrayList<String>()
			newSprint.emails = new ArrayList<String>()
			
			for (String email : data.emailList) {
				newSprint.emails.add(email)
			}
			
			for (String repo : data.repoList) {
				newSprint.repos.add(repo)
			}
			
			if(endDate) {
				startDate = endDate + 1
				endDate = startDate + intervalLength
			}
			
			if(!newSprint.save()) {
				createComplete == false
			}
		}
		
		if(createComplete) {
			flash.sprintMessageSuccess = "A new sprint was successfully created.";
		} else {
			flash.sprintMessageError = "A new sprint couldn't be created. Please try again.";
		}
	}
	
	private void saveSprint(Map data) {
		def team = Team.findByUrl(params.id)
		
		if(team == null) {
			response.sendError(404)
		}
		
		if(params.sprint == null) {
			response.sendError(404)
		}
		
		def sprintId
		try {
			sprintId = Integer.parseInt(params.sprint)
		} catch (NumberFormatException e) {
			response.sendError(404)
		}
		
		def oldSprint = Sprint.get(sprintId)
		
		oldSprint.startDate = data.startDate
		oldSprint.endDate = (data.openEnded) ? null : data.endDate
		
		oldSprint.repos.clear()
		oldSprint.emails.clear()
		
		for (String email : data.emailList) {
			oldSprint.emails.add(email)
		}
		
		for (String repo : data.repoList) {
			oldSprint.repos.add(repo)
		}
		
		if(oldSprint.save()) {
			flash.sprintMessageSuccess = "Sprint was successfully saved.";
		} else {
			flash.sprintMessageError = "Sprint couldn't be saved properly. Please try again.";
		}
	}
	
	static boolean submitDisabled(List<String> emails, List<String> repos) {
		return 	(emails == null && repos == null) ? true : 
				(emails == null && repos != null) ? ((repos.size() > 0) ? false : true) : 
				(repos == null && emails != null) ? ((emails.size() > 2) ? false : true) : 
				(emails.size() > 2 && repos.size() > 0) ? false : 
				(emails.size() > 2 && repos.size() == 0) ? false :
				(emails.size() == 0 && repos.size() > 0) ? false :
				true
	}
	
	private ArrayList<String> getClosureValues(String s) {
		if(s.equals("null")) {
			return new ArrayList<String>()
		} else {
			s = s.replace("[", "").replace("]", "")
			String[] sb = s.split(", ")
			
			return new ArrayList(Arrays.asList(sb))
		}
	}
}
