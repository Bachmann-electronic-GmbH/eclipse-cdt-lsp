/*******************************************************************************
 * Copyright (c) 2023 Bachmann electronic GmbH and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Gesa Hentschke (Bachmann electronic GmbH) - initial implementation
 *******************************************************************************/

package org.eclipse.cdt.lsp.internal.server;

import java.net.URI;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.cdt.lsp.server.ICLanguageServerProvider;
import org.eclipse.cdt.lsp.server.ICompileCommandsDirLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;
import org.eclipse.lsp4e.server.StreamConnectionProvider;

public class CLanguageServerStreamConnectionProvider extends ProcessStreamConnectionProvider implements StreamConnectionProvider {
	ICLanguageServerProvider cLanguageServerProvider;
	
	public CLanguageServerStreamConnectionProvider() {
		cLanguageServerProvider = LspPlugin.getDefault().getCLanguageServerProvider();
		setCommands(cLanguageServerProvider.getCommands());
		
		// set the working directory for the Java process which runs the C/C++ language server:
		setWorkingDirectory(System.getProperty("user.dir"));
	}
	
	@Override
	public Object getInitializationOptions(URI rootUri){
		var locators = LspPlugin.getDefault().getCompileCommandsDirLocators();
		IPath directory = null;
		var commands = getCommands();
		commands.removeIf(entry -> (entry.startsWith("--compile-commands-dir")));
		for (ICompileCommandsDirLocator locator : locators) {
			directory = locator.getCompileCommandsDir(rootUri);
			if (directory != null) {
				break;
			}
		}			
		if (directory == null) {
			setCommands(commands);
			return null;
		}
		
//		var commands = getCommands();
//		commands.removeIf(entry -> (entry.startsWith("--compile-commands-dir")));
		var compileCommandsDir = "--compile-commands-dir=" + directory.toOSString();
		commands.add(compileCommandsDir);
		setCommands(commands);
		return null;
	}
	
}


