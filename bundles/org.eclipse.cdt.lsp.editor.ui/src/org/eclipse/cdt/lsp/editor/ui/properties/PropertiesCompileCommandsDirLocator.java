package org.eclipse.cdt.lsp.editor.ui.properties;

import java.net.URI;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.cdt.lsp.editor.ui.LspEditorUiPlugin;
import org.eclipse.cdt.lsp.server.ICompileCommandsDirLocator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PropertiesCompileCommandsDirLocator implements ICompileCommandsDirLocator {

	@Override
	public IPath getCompileCommandsDir(URI uri) {
		var project =  getProject(uri);
		if (project == null) {
			return null;
		}
		IEclipsePreferences node = new ProjectScope(project).getNode(LspEditorUiPlugin.PLUGIN_ID);
		if (node == null) {
			return null;
		}
		var relativePath = node.get(LspEditorPropertiesPage.COMPILE_COMMANDS_DIR, "build/default");
		return project.getLocation().append(relativePath);
	}
	
	private IProject getProject(URI uri) {
		var scheme = uri.getScheme();
		if ("file".equals(scheme)) {
			IContainer[] container = LspPlugin.getDefault().getWorkspace().getRoot().findContainersForLocationURI(uri);
			if (container.length > 0) {
				return container[0].getProject();
			}
		}
		return null;
	}

}
