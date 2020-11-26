package me.mrletsplay.srweb.game.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.impl.PacketClientDiscardCard;
import me.mrletsplay.srweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.srweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.srweb.packet.impl.PacketClientVote;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerUpdateGameState;
import me.mrletsplay.srweb.util.SRWebSocketServer;

public class BotPlayer extends Player {

	public BotPlayer(SRWebSocketServer server) {
		super(server, null, "Botty Botman");
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void send(Packet p) {
		if(p.getData() instanceof PacketServerUpdateGameState) {
			PacketServerUpdateGameState ugs = (PacketServerUpdateGameState) p.getData();
			GameState n = ugs.getNewState();
			if(n.getMoveState() == GameMoveState.VOTE) {
				PacketClientVote vote = new PacketClientVote(new Random().nextBoolean());
				getServer().handle(this, new Packet(vote));
			}else if(n.getMoveState() == GameMoveState.SELECT_CHANCELLOR && n.getPresident().getID().equals(getID())) {
				List<Player> avPlayers = getAvailablePlayersForChancellor(n);
				Player sel = avPlayers.get(new Random().nextInt(avPlayers.size()));
				PacketClientSelectChancellor select = new PacketClientSelectChancellor(sel.getID());
				getServer().handle(this, new Packet(select));
			}else if(n.getMoveState() == GameMoveState.DRAW_CARDS && n.getPresident().getID().equals(getID())) {
				getServer().handle(this, new Packet(new PacketClientDrawCards()));
			}
		}else if(p.getData() instanceof PacketServerPickCards) {
			getServer().handle(this, new Packet(new PacketClientDiscardCard(0)));
		}
	}
	
	private List<Player> getAvailablePlayersForChancellor(GameState state) {
		List<Player> ps = new ArrayList<>();
		for(Player p : state.getRoom().getPlayers()) {
			if(getID().equals(p.getID())) continue;
			if(state.getDeadPlayers().stream().anyMatch(pl -> pl.getID().equals(p.getID()))) continue;
			if(state.getBlockedPlayer() != null && state.getBlockedPlayer().getID().equals(p.getID())) continue;
			if(state.getPreviousPresident() != null && state.getPreviousPresident().getID().equals(p.getID())) continue;
			if(state.getPreviousChancellor() != null && state.getPreviousChancellor().getID().equals(p.getID())) continue;
			ps.add(p);
		}
		return ps;
	}

}
