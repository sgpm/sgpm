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

import javax.sound.midi.ControllerEventListener;

class UrlMappings {

	static mappings = {	
		name createTeam: "/createTeam" {
			controller = 'createTeam'
		}
		
		name overview: "/$id/" {
			controller = 'overview'
			action = 'overview'
		}
		
		name sprint_show: "/$id/sprint/" {
			controller = 'sprint'
			action = 'showSprints'
		}
		
		name sprint_create_form: "/$id/sprint/create/" {
			controller = 'sprint'
			action = 'createForm'
		}
		
		name sprint_edit_form: "/$id/sprint/edit/$sprint/" {
			controller = 'sprint'
			action = 'editForm'
		}
		
		name sprint_delete: "/$id/sprint/delete/$sprint/" {
			controller = 'sprint'
			action = 'deleteSprint'
		}
		
		"/"(controller: 'createTeam')
		"500"(view:'/error')
	}
}