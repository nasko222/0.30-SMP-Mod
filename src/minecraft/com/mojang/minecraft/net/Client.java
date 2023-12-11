package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class Client {
	public ByteArrayOutputStream levelBuffer;
	public SocketConnection serverConnection;
	public Minecraft minecraft;
	public boolean processData = false;
	public boolean connected = false;
	public HashMap players = new HashMap();

	public Client(Minecraft minecraft, String ip, int port, String user, String session) {
		minecraft.hideScreen = true;
		this.minecraft = minecraft;
		(new ConnectionThread(this, ip, port, user, session, minecraft)).start();
	}

	public final void sendTileUpdated(int x, int y, int z, int id1, int id2) {
		this.serverConnection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{x, y, z, id1, id2});
	}

	public final void handleException(Exception e) {
		this.serverConnection.disconnect();
		this.minecraft.setScreen(new ErrorScreen("Disconnected!", e.getMessage()));
		e.printStackTrace();
	}

	public final boolean isConnected() {
		SocketConnection socketConnection1;
		return this.serverConnection != null && (socketConnection1 = this.serverConnection).connected;
	}

	public final List getUsernames() {
		ArrayList arrayList1;
		(arrayList1 = new ArrayList()).add(this.minecraft.user.name);
		Iterator iterator3 = this.players.values().iterator();

		while(iterator3.hasNext()) {
			NetworkPlayer networkPlayer2 = (NetworkPlayer)iterator3.next();
			arrayList1.add(networkPlayer2.name);
		}

		return arrayList1;
	}
}