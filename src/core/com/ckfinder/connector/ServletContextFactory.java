/*
 * CKFinder
 * ========
 * http://ckfinder.com
 * Copyright (C) 2007-2011, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */
package com.ckfinder.connector;

import javax.servlet.ServletContext;

import com.kmetop.demsy.Demsy;

/**
 * Access to servletcontex outside from servlet.
 */
public class ServletContextFactory {

	/**
	 * ServletContext object.
	 */
	@SuppressWarnings("unused")
	private static ServletContext servletContext;

	/**
	 * constructor to be accessed in servlet.
	 * 
	 * @param servletContext1
	 *            from servlet
	 */
	ServletContextFactory(final ServletContext servletContext1) {
		servletContext = servletContext1;
	}

	/**
	 * returns servlet context object or throws exception if isn't set.
	 * 
	 * @return servletcontext object
	 * @throws Exception
	 *             when servletcontext is not set for object.
	 */
	public static ServletContext getServletContext() throws Exception {
		return Demsy.servletContext;
	}

}
