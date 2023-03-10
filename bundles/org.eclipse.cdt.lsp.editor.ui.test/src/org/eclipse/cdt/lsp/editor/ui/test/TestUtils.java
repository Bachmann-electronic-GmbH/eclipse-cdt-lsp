package org.eclipse.cdt.lsp.editor.ui.test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.eclipse.cdt.lsp.editor.ui.LspEditorUiPlugin;
import org.eclipse.cdt.lsp.editor.ui.preference.LspEditorPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class TestUtils {
	
	public static void setLspPreferred(IProject project, boolean value) {
		IEclipsePreferences node = new ProjectScope(project).getNode(LspEditorUiPlugin.PLUGIN_ID);
		node.putBoolean(LspEditorPreferences.getPreferenceMetadata().identifer(), value);		
	}
	
	public static boolean isLspPreferred(IProject project) {
		IEclipsePreferences node = new ProjectScope(project).getNode(LspEditorUiPlugin.PLUGIN_ID);
		return node.getBoolean(LspEditorPreferences.getPreferenceMetadata().identifer(), false);
	}
	
	public static IProject createCProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project.exists()) {
			return project;
		}
		project.create(null);
		project.open(null);
		// configure C nature
		IProjectDescription description = project.getDescription();
		if (description != null) {
			String[] natureIds = {"org.eclipse.cdt.core.cnature"};
			description.setNatureIds(natureIds);
			project.setDescription(description, null);
		}
		return project;
	}
	
	public static void deleteProject(IProject project) throws CoreException {
		if (project != null) {
			project.delete(true, new NullProgressMonitor());
		}
	}
	
	public static IFile createFile(IProject p, String name, String content) throws CoreException, UnsupportedEncodingException {
		IFile testFile = p.getFile(name);
		testFile.create(new ByteArrayInputStream(content.getBytes(testFile.getCharset())), true, null);
		return testFile;
	}
	
	public static IEditorPart openInEditor(URI uri, String editorID) throws PartInitException {
		IEditorPart part = IDE.openEditor(getWorkbenchPage(), uri, editorID, true);		
		part.setFocus();
		return part;
	}
	
	public static IEditorPart openInEditor(IFile file) throws PartInitException {
		IEditorPart part = IDE.openEditor(getWorkbenchPage(), file);
		part.setFocus();
		return part;
	}

	public static boolean closeEditor(IEditorPart editor, boolean save) {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		return page.closeEditor(editor, save);
	}
	
	private static IWorkbenchPage getWorkbenchPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

}
