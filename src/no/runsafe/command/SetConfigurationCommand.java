package no.runsafe.command;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IKernel;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.console.ConsoleCommand;
import no.runsafe.framework.internal.configuration.ConfigurationEngine;

import java.util.Map;

public class SetConfigurationCommand extends ConsoleCommand
{
	public SetConfigurationCommand()
	{
		super(
			"set", "Allows run time tweaking of configuration options",
			new PluginArgument(), new OptionalArgument("key"), new TrailingArgument("value", false)
		);
	}

	@Override
	public String OnExecute(Map<String, String> params)
	{
		IKernel plugin = RunsafePlugin.getPlugin(params.get("plugin"));
		String key = params.get("key");
		String value = params.get("value");
		if (plugin == null)
			return "Invalid plugin specified";
		ConfigurationEngine engine = plugin.getComponent(ConfigurationEngine.class);
		IConfiguration configuration = engine.getPluginConfiguration();
		if (configuration == null)
			return String.format("Could not get configuration object from plugin %s!", params.get("plugin"));
		if (key == null)
		{
			StringBuilder builder = new StringBuilder("Available keys for the plugin " + params.get("plugin") + ":\n");
			for (String candidateKey : configuration.getConfigurationKeys())
				builder.append("  ").append(candidateKey).append(" = ").append(configuration.getConfigValueAsString(candidateKey)).append('\n');

			return builder.toString();
		}
		String oldValue = configuration.getConfigValueAsString(key);
		if (oldValue == null)
			return String.format("The configuration key '%s' does not appear valid for plugin %s.", key, params.get("plugin"));

		if (oldValue.equals(value))
			return String.format("%s: %s was already set to %s.", params.get("plugin"), key, value);

		configuration.setConfigValue(key, value);
		configuration.save();
		plugin.getComponent(ConfigurationEngine.class).load();
		return String.format("%s: %s has been changed from %s to %s.", params.get("plugin"), key, oldValue, value);
	}
}
