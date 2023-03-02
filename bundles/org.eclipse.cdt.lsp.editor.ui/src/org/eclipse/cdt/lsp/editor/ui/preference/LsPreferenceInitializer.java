package org.eclipse.cdt.lsp.editor.ui.preference;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.cdt.lsp.editor.ui.LspEditorUiPlugin;
import org.eclipse.cdt.lsp.editor.ui.properties.LspEditorPropertiesPage;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class LsPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final IPreferenceStore preferenceStore = LspEditorUiPlugin.getDefault().getLsPreferences();

	@Override
	public void initializeDefaultPreferences() {
		var cLanguageServerProvider = LspPlugin.getDefault().getCLanguageServerProvider();
		if (cLanguageServerProvider == null) {
			LspEditorUiPlugin.logError("Cannot determine language server provider");
			return;
		}
		preferenceStore.setDefault(LspEditorPreferences.SERVER_PATH, cLanguageServerProvider.getDefaultServerPath());
		preferenceStore.setDefault(LspEditorPreferences.SERVER_OPTIONS, cLanguageServerProvider.getDefaultOptionsAsString());
		preferenceStore.setDefault(LspEditorPropertiesPage.COMPILE_COMMANDS_DIR, LspEditorPropertiesPage.DEFAULT_COMPILE_COMMANDS_DIR);
		
		preferenceStore.setValue(LspEditorPreferences.SERVER_PATH, cLanguageServerProvider.getServerPath());
		preferenceStore.setValue(LspEditorPreferences.SERVER_OPTIONS, cLanguageServerProvider.getOptionsAsString());
		preferenceStore.setValue(LspEditorPropertiesPage.COMPILE_COMMANDS_DIR, LspEditorPropertiesPage.DEFAULT_COMPILE_COMMANDS_DIR);
	}
}
