/*******************************************************************************
 * Copyright (c) 2023 Bachmann electronic GmbH and others.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Gesa Hentschke (Bachmann electronic GmbH) - initial implementation
 *******************************************************************************/

package org.eclipse.cdt.lsp.editor.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.lsp.editor.ui.preference.LspEditorPreferences;
import org.eclipse.cdt.lsp.server.DefaultCLanguageServerProvider;
import org.eclipse.cdt.utils.CommandLineUtil;
import org.eclipse.jface.preference.IPreferenceStore;


public class CdtCLanguageServerProvider extends DefaultCLanguageServerProvider {
	private static final IPreferenceStore preferenceStore = LspEditorUiPlugin.getDefault().getLsPreferences();

	
	@Override
	protected List<String> createCommands() {
		List<String> commands = super.createCommands();
		setPreferenceStoreDefaults(commands); // use the server provider settings as default
		List<String> commandsFromStore = getCommandsFromStore();
		if (commandsFromStore.isEmpty()) {
			return commands;
		}
		return commandsFromStore;
	}
	
	private void setPreferenceStoreDefaults(List<String> commands) {
		if (!commands.isEmpty()) {
			//set values in preference store:
			preferenceStore.setDefault(LspEditorPreferences.SERVER_PATH, commands.get(0));
			String args = "";
			for (int i=1; i<commands.size(); i++) {
				args = args + " " + commands.get(i);
			}
			preferenceStore.setDefault(LspEditorPreferences.SERVER_OPTIONS, args);
		}
	}
	
	private List<String> getCommandsFromStore(){
		List<String> commands = new ArrayList<>();
		String serverPath = preferenceStore.getString(LspEditorPreferences.SERVER_PATH);
		if (serverPath.isBlank()) {
			return commands;
		}
		commands.add(serverPath);
		String options = preferenceStore.getString(LspEditorPreferences.SERVER_OPTIONS);
		if (options.isBlank()) {
			return commands;
		}
		commands.addAll(Arrays.asList(CommandLineUtil.argumentsToArray(options)));
		return commands;
	}
}
