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

package org.eclipse.cdt.lsp.editor.ui.preference;

import org.eclipse.cdt.lsp.editor.ui.LspEditorUiPlugin;
import org.eclipse.cdt.ui.newui.MultiLineTextFieldEditor;
import org.eclipse.cdt.utils.CommandLineUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.cdt.lsp.editor.ui.LspEditorUiMessages;
import org.eclipse.core.runtime.preferences.PreferenceMetadata;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LspEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private FileFieldEditor serverPath;
	private MultiLineTextFieldEditor serverOptions;

	public LspEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(LspEditorUiPlugin.getDefault().getLsPreferences());
		setDescription(LspEditorUiMessages.LspEditorPreferencePage_description);
	}

	@Override
	public void init(IWorkbench workbench) {
	}
	

	@Override
	public void createFieldEditors() {
		PreferenceMetadata<Boolean> prefer = LspEditorPreferences.getPreferenceMetadata();
		var booleanFieldEditor = new BooleanFieldEditor(prefer.identifer(), prefer.name(), getFieldEditorParent());
		addField(booleanFieldEditor);
		
		serverPath = new FileFieldEditor(LspEditorPreferences.SERVER_PATH,
				LspEditorUiMessages.LspEditorPreferencePage_server_path, getFieldEditorParent());
		addField(serverPath);

		
		serverOptions = new MultiLineTextFieldEditor(LspEditorPreferences.SERVER_OPTIONS,
				LspEditorUiMessages.LspEditorPreferencePage_server_options, getFieldEditorParent());
		addField(serverOptions);
	}
		
	@Override
	public boolean performOk() {
		writeServerPathToProvider(serverPath.getStringValue());
		writeServerOptionsToProvider(serverOptions.getStringValue());
		return super.performOk();
	}
	
	private void writeServerPathToProvider(String path) {
		if (path == null || path.isBlank()) {
			return;
		}
		List<String> commands = LspPlugin.getDefault().getCLanguageServerProvider().getCommands();
		if (commands != null && !commands.isEmpty()) {
			commands.set(0, path);
		} else if (commands == null) {
			commands = new ArrayList<>();
			commands.add(path);
		}
		LspPlugin.getDefault().getCLanguageServerProvider().setCommands(commands);
	}
	
	private void writeServerOptionsToProvider(String options) {
		if (options == null) {
			return;
		}
		List<String> commands = LspPlugin.getDefault().getCLanguageServerProvider().getCommands();
		if (commands != null && !commands.isEmpty()) {
			String serverPath = commands.get(0); // save server path
			commands.clear(); // clear all old options
			commands.add(serverPath);
			commands.addAll(Arrays.asList(CommandLineUtil.argumentsToArray(options)));
		}
		LspPlugin.getDefault().getCLanguageServerProvider().setCommands(commands);
	}

}
