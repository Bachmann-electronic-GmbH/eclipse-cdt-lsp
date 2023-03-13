package org.eclipse.cdt.lsp.internal.server;

import org.eclipse.cdt.lsp.LspPlugin;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public abstract class RegistryBase {
	private static final String CLASS = "class"; //$NON-NLS-1$
	
	protected <T> Object getInstanceFromExtension(IConfigurationElement configurationElement, Class<T> clazz) {
		Object result = null;
		try {
			Object obj = configurationElement.createExecutableExtension(CLASS);
			result = Adapters.adapt(obj, clazz);
		} catch (CoreException e) {
			LspPlugin.logError(e.getMessage(), e);
		}
		return result;
	}
}
