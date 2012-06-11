package com.kmetop.demsy.modules.ckfinder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ckfinder.connector.configuration.ConfigurationFactory;
import com.ckfinder.connector.configuration.Constants;
import com.ckfinder.connector.configuration.Events.EventTypes;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.BeforeExecuteCommandEventArgs;
import com.ckfinder.connector.errors.ConnectorException;
import com.ckfinder.connector.handlers.command.Command;
import com.ckfinder.connector.handlers.command.CopyFilesCommand;
import com.ckfinder.connector.handlers.command.CreateFolderCommand;
import com.ckfinder.connector.handlers.command.DeleteFileCommand;
import com.ckfinder.connector.handlers.command.DeleteFolderCommand;
import com.ckfinder.connector.handlers.command.DownloadFileCommand;
import com.ckfinder.connector.handlers.command.ErrorCommand;
import com.ckfinder.connector.handlers.command.GetFilesCommand;
import com.ckfinder.connector.handlers.command.GetFoldersCommand;
import com.ckfinder.connector.handlers.command.InitCommand;
import com.ckfinder.connector.handlers.command.MoveFilesCommand;
import com.ckfinder.connector.handlers.command.RenameFileCommand;
import com.ckfinder.connector.handlers.command.RenameFolderCommand;
import com.ckfinder.connector.handlers.command.ThumbnailCommand;
import com.ckfinder.connector.handlers.command.XMLCommand;
import com.ckfinder.connector.handlers.command.XMLErrorCommand;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.lang.DemsyException;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;

public class CKFinder {
	private static final Log log = Logs.get();

	public static void getResponse(final HttpServletRequest request, final HttpServletResponse response) {
		String command = request.getParameter("command");
		IConfiguration configuration = null;
		try {
			configuration = Configuration.getInstance();
			configuration = ConfigurationFactory.getInstace().getConfiguration(request);
		} catch (Exception e) {
			log.error("", e);
			response.reset();
			throw new DemsyException(e);
		}
		try {

			if (command == null || command.equals("")) {
				throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false);
			}

			if (configuration == null) {
				throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_CONNECTOR_DISABLED, false);
			}

			BeforeExecuteCommandEventArgs args = new BeforeExecuteCommandEventArgs();
			args.setCommand(command);
			args.setRequest(request);
			args.setResponse(response);
			if (configuration.getEvents() != null) {
				if (configuration.getEvents().run(EventTypes.BeforeExecuteCommand, args, configuration)) {
					CommandHandlerEnum.valueOf(command.toUpperCase()).execute(request, response, configuration, Demsy.servletContext);
				}
			} else {
				CommandHandlerEnum.valueOf(command.toUpperCase()).execute(request, response, configuration, Demsy.servletContext);
			}
		} catch (IllegalArgumentException e) {
			log.error("", e);
			handleError(new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND, false), configuration, request, response, command);
		} catch (ConnectorException e) {
			log.error("", e);
			handleError(e, configuration, request, response, command);
		} catch (Exception e) {
			log.error("", e);
			handleError(new ConnectorException(e), configuration, request, response, command);
		}
	}

	private static void handleError(final ConnectorException e, final IConfiguration configuration, final HttpServletRequest request, final HttpServletResponse response, final String currentCommand) {
		try {
			if (currentCommand != null) {
				Command command = CommandHandlerEnum.valueOf(currentCommand.toUpperCase()).getCommand();
				if (command instanceof XMLCommand) {
					CommandHandlerEnum.XMLERROR.execute(request, response, configuration, Demsy.servletContext, e);
				} else {
					CommandHandlerEnum.ERROR.execute(request, response, configuration, Demsy.servletContext, e);
				}
			} else {
				CommandHandlerEnum.XMLERROR.execute(request, response, configuration, Demsy.servletContext, e);
			}

		} catch (Exception e1) {
			throw new DemsyException(e);
		}
	}

	private enum CommandHandlerEnum {
		/**
		 * init command.
		 */
		INIT(new InitCommand()),
		/**
		 * get subfolders for seleted location command.
		 */
		GETFOLDERS(new GetFoldersCommand()),
		/**
		 * get files from current folder command.
		 */
		GETFILES(new GetFilesCommand()),
		/**
		 * get thumbnail for file command.
		 */
		THUMBNAIL(new ThumbnailCommand()),
		/**
		 * download file command.
		 */
		DOWNLOADFILE(new DownloadFileCommand()),
		/**
		 * create subfolder.
		 */
		CREATEFOLDER(new CreateFolderCommand()),
		/**
		 * rename file.
		 */
		RENAMEFILE(new RenameFileCommand()),
		/**
		 * rename folder.
		 */
		RENAMEFOLDER(new RenameFolderCommand()),
		/**
		 * delete folder.
		 */
		DELETEFOLDER(new DeleteFolderCommand()),
		/**
		 * copy files.
		 */
		COPYFILES(new CopyFilesCommand()),
		/**
		 * move files.
		 */
		MOVEFILES(new MoveFilesCommand()),
		/**
		 * delete file.
		 */
		DELETEFILE(new DeleteFileCommand()),
		/**
		 * file upload.
		 */
		FILEUPLOAD(new FileUploadCommand()),
		/**
		 * quick file upload.
		 */
		QUICKUPLOAD(new QuickUploadCommand()),
		/**
		 * XML error command.
		 */
		XMLERROR(new XMLErrorCommand()),
		/**
		 * error command.
		 */
		ERROR(new ErrorCommand());
		/**
		 * command class for enum field.
		 */
		private Command command;

		/**
		 * Enum contructor to set command.
		 * 
		 * @param command1
		 *            command name
		 */
		private CommandHandlerEnum(final Command command1) {
			this.command = command1;
		}

		/**
		 * Execute command.
		 * 
		 * @param request
		 *            request
		 * @param response
		 *            response
		 * @param configuration
		 *            connector configuraion
		 * @param sc
		 *            servletContext
		 * @param params
		 *            params for command.
		 * @throws ConnectorException
		 *             when error occurs
		 */
		private void execute(final HttpServletRequest request, final HttpServletResponse response, final IConfiguration configuration, final ServletContext sc, final Object... params)
				throws ConnectorException {
			Command com = null;
			try {
				com = command.getClass().newInstance();
			} catch (IllegalAccessException e1) {
				throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
			} catch (InstantiationException e1) {
				throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
			}
			if (com == null) {
				throw new ConnectorException(Constants.Errors.CKFINDER_CONNECTOR_ERROR_INVALID_COMMAND);
			}
			com.runCommand(request, response, configuration, params);
		}

		/**
		 * gets command.
		 * 
		 * @return command
		 */
		public Command getCommand() {
			return this.command;
		}

	}
}
