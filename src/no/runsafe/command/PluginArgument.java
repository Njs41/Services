package no.runsafe.command;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.CommandArgumentSpecification;
import no.runsafe.framework.api.command.argument.ITabComplete;
import no.runsafe.framework.api.command.argument.IValueExpander;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.List;

public class PluginArgument extends CommandArgumentSpecification implements ITabComplete, IValueExpander
{
	public PluginArgument()
	{
		super("plugin");
	}

	@Override
	public boolean isRequired()
	{
		return true;
	}

	@Override
	public boolean isWhitespaceInclusive()
	{
		return false;
	}

	@Override
	public List<String> getAlternatives(IPlayer executor, String partial)
	{
		return Lists.transform(
			RunsafePlugin.getPlugins("*"),
			new Function<RunsafePlugin, String>()
			{
				@Override
				public String apply(@Nullable RunsafePlugin plugin)
				{
					if (plugin == null)
						return null;
					return plugin.getName();
				}
			}
		);
	}

	@Nullable
	@Override
	public String expand(ICommandExecutor context, String value)
	{
		List<RunsafePlugin> alternatives = RunsafePlugin.getPlugins("*");
		if(alternatives.isEmpty() || alternatives.size() > 1)
			return null;

		return alternatives.get(0).getName();
	}
}
