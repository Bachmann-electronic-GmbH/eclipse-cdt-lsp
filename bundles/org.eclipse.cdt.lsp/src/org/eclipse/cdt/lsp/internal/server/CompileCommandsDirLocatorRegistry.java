package org.eclipse.cdt.lsp.internal.server;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.cdt.lsp.server.ICompileCommandsDirLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

public class CompileCommandsDirLocatorRegistry extends RegistryBase {
	private static final String EXTENSION_ID = LspPlugin.PLUGIN_ID + ".compileCommands"; //$NON-NLS-1$
	private static final String LOCATOR_ELEMENT = "locator"; //$NON-NLS-1$
	private final IExtensionPoint cExtensionPoint;
	
	public CompileCommandsDirLocatorRegistry() {
		this.cExtensionPoint = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID);
	}
		
	public ICompileCommandsDirLocator[] createCompileCommandsDirLocators() {
		ICompileCommandsDirLocator[] locator = null;
		var configuratonElements = cExtensionPoint.getConfigurationElements();
		locator = new ICompileCommandsDirLocator[configuratonElements.length];
		int i = 0;
		for (IConfigurationElement configurationElement : configuratonElements) {
			if (LOCATOR_ELEMENT.equals(configurationElement.getName())) {
				 locator[i] = (ICompileCommandsDirLocator) getInstanceFromExtension(configurationElement, ICompileCommandsDirLocator.class);
				 i++;
			}
		}
		return locator;
	}
}
