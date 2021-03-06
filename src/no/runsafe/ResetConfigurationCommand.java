package no.runsafe;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.console.ConsoleCommand;
import no.runsafe.framework.internal.configuration.ConfigurationEngine;

public class ResetConfigurationCommand extends ConsoleCommand
{
	public ResetConfigurationCommand()
	{
		super("reset", "Resets the configuration for the selected plugin to the defaults.", new PluginArgument());
	}

	@Override
	public String OnExecute(IArgumentList parameters)
	{
		StringBuilder response = new StringBuilder(100);
		Iterable<RunsafePlugin> plugins = parameters.getValue("plugin");
		if (plugins == null)
			return null;
		for (RunsafePlugin plugin : plugins)
		{
			ConfigurationEngine engine = plugin.getComponent(ConfigurationEngine.class);
			if (engine.restoreToDefaults())
				response.append(
					String.format("All configuration for the plugin %s has been rolled back to their defaults.", plugin.getName())
				);
		}
		return response.toString();
	}
}
