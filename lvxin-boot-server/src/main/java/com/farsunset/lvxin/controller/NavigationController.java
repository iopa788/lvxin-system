/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NavigationController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/console/index.action", method = RequestMethod.GET)
	public ModelAndView console(ModelAndView model) {
		model.setViewName("console/index");
		return model;
	}

	@RequestMapping(value = "/webclient/index.action", method = RequestMethod.GET)
	public ModelAndView webclient(ModelAndView model) {
		model.setViewName("webclient/index");
		return model;
	}

	@RequestMapping(value = "/console/broadcast.action", method = RequestMethod.GET)
	public ModelAndView broadcast(ModelAndView model) {
		model.setViewName("console/broadcast/manage");
		return model;
	}

	@RequestMapping(value = "/error/400")
	public ModelAndView error400(ModelAndView model) {
		model.setViewName("error/400");
		return model;
	}

	@RequestMapping(value = "/error/404")
	public ModelAndView error404(ModelAndView model) {
		model.setViewName("error/404");
		return model;
	}

	@RequestMapping(value = "/error/500")
	public ModelAndView error500(ModelAndView model) {
		model.setViewName("error/500");
		return model;
	}
}
