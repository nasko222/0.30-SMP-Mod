package com.mojang.minecraft;

import com.mojang.minecraft.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class User {
	public static List creativeTiles;
	public static List tools;
	public String name;
	public String sessionid;
	public String mpPass;
	public boolean hasPaid;

	public User(String name, String sessionid) {
		this.name = name;
		this.sessionid = sessionid;
	}

	static {
		(creativeTiles = new ArrayList()).add(Tile.clothRed);
		;
		creativeTiles.add(Tile.clothOrange);
		creativeTiles.add(Tile.clothYellow);
		creativeTiles.add(Tile.clothChartreuse);
		creativeTiles.add(Tile.clothGreen);
		creativeTiles.add(Tile.clothSpringGreen);
		creativeTiles.add(Tile.clothCyan);
		creativeTiles.add(Tile.clothCapri);
		creativeTiles.add(Tile.clothUltramarine);
		creativeTiles.add(Tile.clothViolet);
		creativeTiles.add(Tile.clothPurple);
		creativeTiles.add(Tile.clothMagenta);
		creativeTiles.add(Tile.clothRose);
		creativeTiles.add(Tile.clothDarkGray);
		creativeTiles.add(Tile.clothGray);
		creativeTiles.add(Tile.clothWhite);
	}
	
	static {
		(tools = new ArrayList()).add(Tile.pickaxeWood);
		;
		tools.add(Tile.axeWood);
		tools.add(Tile.shovelWood);
		
		tools.add(Tile.pickaxeStone);
		tools.add(Tile.axeStone);
		tools.add(Tile.shovelStone);
		
		tools.add(Tile.pickaxeIron);
		tools.add(Tile.axeIron);
		tools.add(Tile.shovelIron);
		
		tools.add(Tile.pickaxeGold);
		tools.add(Tile.axeGold);
		tools.add(Tile.shovelGold);
		
		
	
	}
}