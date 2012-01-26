package com.jeroensteenbeeke.bk.ville.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.jayconomy.Jayconomy;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.ApprovedPlayer;

public class VilleApproveMeCommandHandler extends AbstractVilleCommandHandler {
	private final Jayconomy jayconomy;

	public VilleApproveMeCommandHandler(Ville ville, Jayconomy jayconomy) {
		super(ville, Ville.PERMISSION_USE);
		this.jayconomy = jayconomy;
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "approve").andArgIs(1, "me")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentEquals(0, "approve")
				.andArgumentEquals(1, "me").itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		int approved = getVille().getDatabase().find(ApprovedPlayer.class)
				.where().eq("player", player.getName()).findRowCount();

		if (approved == 0 && !player.hasPermission(Ville.PERMISSION_PREMIUM)) {
			if (jayconomy.hasBalance(player, getVille().getApprovePrice())) {
				jayconomy.decreaseBalance(player.getName(), getVille()
						.getApprovePrice());

				ApprovedPlayer ap = new ApprovedPlayer();
				ap.setPlayer(player.getName());
				getVille().getDatabase().save(ap);

				getVille().approvePlayer(player);

				Messages.send(player, "&aBuild rights granted");
			} else {
				Messages.send(player, String.format(
						"&cUniversal build rights cost &e%s",
						jayconomy.formatCurrency(getVille().getApprovePrice())));
				Messages.send(
						player,
						String.format("&cUse the command &e/ville free build &cto locate places where you can earn this"));
			}
		} else {
			Messages.send(player, "&cYou are already an approved builder");
		}
	}
}
