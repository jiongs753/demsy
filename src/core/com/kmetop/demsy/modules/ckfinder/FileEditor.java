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
package com.kmetop.demsy.modules.ckfinder;

import com.ckfinder.connector.configuration.Events;
import com.ckfinder.connector.configuration.Events.EventTypes;
import com.ckfinder.connector.configuration.Plugin;

public class FileEditor extends Plugin{


	@Override
	public void registerEventHandlers(Events events) {
		events.addEventHandler(EventTypes.BeforeExecuteCommand, SaveFileCommand.class);

	}



}
