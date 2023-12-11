package com.mojang.minecraft.gui;

import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.Packet;

import org.lwjgl.input.Keyboard;

public final class ChatScreen extends Screen {
	private String typedMsg = "";
	private int counter = 0;

	public final void init() {
		Keyboard.enableRepeatEvents(true);
	}

	public final void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}
	
	public String giveCMD(String command) {
        // Check if the command starts with "/give"
        if (command.startsWith("/give ")) {
            // Split the command into parts
            String[] parts = command.split("\\s+");

            // Check if there are at least three parts ("/give", int1, int2)
            if (parts.length >= 3) {
                try {
                    int intValue1 = Integer.parseInt(parts[1]);
                    int intValue2 = Integer.parseInt(parts[2]);

                    if (intValue1 > 0 && intValue1 < Tile.amounts) {
                    	if (true) {
                    		this.minecraft.player.inventory.addResource(intValue1, intValue2);
                    	}
                    	
                    	return "I'm cheating some " + intValue1;
                    	
                    	
                    }
                	} catch (NumberFormatException e) {
                }
            } 
        }
        
        return null;
    }

	protected final void keyPressed(char eventCharacter, int eventKey) {
		if(eventKey == 1) {
			this.minecraft.setScreen((Screen)null);
		} else if(eventKey == 28) {
			Client client10000 = this.minecraft.networkClient;
			String eventKey1 = this.typedMsg.trim();
			Client eventCharacter1 = client10000;
			
			String flag = null;
			
			if (eventKey1.startsWith("/give ")) {
				
				//if (this.minecraft.user.name.equals("OtrexDev")) return;
				
				flag = giveCMD(eventKey1);
				this.minecraft.setScreen((Screen)null);
			}
			
			if (eventKey1.equals("/clear")) {
				this.minecraft.player.inventory.clearInv();
				this.minecraft.setScreen((Screen)null);
				return;
			}
			
			if (flag != null) eventKey1 = flag;
			
			if((eventKey1 = eventKey1.trim()).length() > 0) {
				eventCharacter1.serverConnection.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, eventKey1});
			}

			this.minecraft.setScreen((Screen)null);
		} else {
			if(eventKey == 14 && this.typedMsg.length() > 0) {
				this.typedMsg = this.typedMsg.substring(0, this.typedMsg.length() - 1);
			}

			if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(eventCharacter) >= 0 && this.typedMsg.length() < 64 - (this.minecraft.user.name.length() + 2)) {
				this.typedMsg = this.typedMsg + eventCharacter;
			}

		}
	}

	public final void render(int xMouse, int yMouse) {
		fill(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		drawString(this.font, "> " + this.typedMsg + (this.counter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
	}

	protected final void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0 && this.minecraft.gui.hoveredUsername != null) {
			if(this.typedMsg.length() > 0 && !this.typedMsg.endsWith(" ")) {
				this.typedMsg = this.typedMsg + " ";
			}

			this.typedMsg = this.typedMsg + this.minecraft.gui.hoveredUsername;
			x = 64 - (this.minecraft.user.name.length() + 2);
			if(this.typedMsg.length() > x) {
				this.typedMsg = this.typedMsg.substring(0, x);
			}
		}

	}
}