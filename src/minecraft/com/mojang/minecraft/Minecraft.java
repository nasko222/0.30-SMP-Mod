package com.mojang.minecraft;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.gamemode.CreativeGameMode;
import com.mojang.minecraft.gamemode.GameMode;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.gui.BlockSelectionScreen;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.gui.CraftMenu;
import com.mojang.minecraft.gui.DeathScreen;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.Gui;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.item.Arrow;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.tile.FoodTile;
import com.mojang.minecraft.level.tile.ItemTile;
import com.mojang.minecraft.level.tile.ItemTile.ItemMode;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.Cube;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.ModelCache;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.NetworkPlayer;
import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.particle.WaterDropParticle;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.KeyboardInput;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.FrustumCuller;
import com.mojang.minecraft.renderer.GameRenderer;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.TileRenderer;
import com.mojang.minecraft.renderer.texture.DynamicTexture;
import com.mojang.minecraft.renderer.texture.LavaTexture;
import com.mojang.minecraft.renderer.texture.WaterTexture;
import com.mojang.minecraft.sound.SoundEngine;
import com.mojang.minecraft.sound.SoundPlayer;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.Mth;

public final class Minecraft implements Runnable {
	public GameMode gamemode = new SurvivalGameMode(this);
	private boolean fullscreen = false;
	public int width;
	public int height;
	private Timer timer = new Timer(20.0F);
	public Level level;
	public LevelRenderer levelRenderer;
	public Player player;
	public ParticleEngine particleEngine;
	public User user = null;
	public String host;
	public Canvas parent;
	public boolean appletMode = false;
	public volatile boolean pause = false;
	private Cursor emptyCursor;
	public Textures textures;
	public Font font;
	public Screen screen = null;
	public LevelLoaderListener loadingScreen = new LevelLoaderListener(this);
	public GameRenderer gameRenderer = new GameRenderer(this);
	public LevelIO levelIo = new LevelIO(this.loadingScreen);
	public SoundEngine soundEngine = new SoundEngine();
	private BackgroundDownloader resourceDownloader;
	private int frames = 0;
	private int clickCounter = 0;
	public String loadMapUser = null;
	public int loadMapId = 0;
	public Robot robot;
	public Gui gui;
	public boolean hideScreen = false;
	public Client networkClient;
	public SoundPlayer soundPlayer;
	public HitResult hitResult;
	public Options options;
	private MinecraftApplet applet;
	String server;
	int port;
	volatile boolean running;
	public String fpsString;
	public boolean mouseGrabbed;
	private int oFrames;
	public boolean raining;
	
	public static boolean DontLoadToolbar;

	public Minecraft(Canvas parent, MinecraftApplet applet, int width, int height, boolean fullscreen) {
		new HumanoidModel(0.0F);
		this.hitResult = null;
		this.server = null;
		this.port = 0;
		this.running = false;
		this.fpsString = "";
		this.mouseGrabbed = false;
		this.oFrames = 0;
		this.raining = false;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

		this.applet = applet;
		new SleepThread(this);
		this.parent = parent;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		if(parent != null) {
			try {
				this.robot = new Robot();
				return;
			} catch (AWTException aWTException8) {
				aWTException8.printStackTrace();
			}
		}

	}

	public final void setScreen(Screen screen) {
		if(!(this.screen instanceof ErrorScreen)) {
			if(this.screen != null) {
				this.screen.removed();
			}

			if(screen == null && this.player.health <= 0) {
				screen = new DeathScreen();
			}

			this.screen = (Screen)screen;
			if(screen != null) {
				if(this.mouseGrabbed) {
					this.player.releaseAllKeys();
					this.mouseGrabbed = false;
					if(this.appletMode) {
						try {
							Mouse.setNativeCursor((Cursor)null);
						} catch (LWJGLException lWJGLException4) {
							lWJGLException4.printStackTrace();
						}
					} else {
						Mouse.setGrabbed(false);
					}
				}

				int i2 = this.width * 240 / this.height;
				int i3 = this.height * 240 / this.height;
				((Screen)screen).init(this, i2, i3);
				this.hideScreen = false;
			} else {
				this.grabMouse();
			}
		}
	}

	private static void checkGlError(String string) {
		int i1;
		if((i1 = GL11.glGetError()) != 0) {
			String string2 = GLU.gluErrorString(i1);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string);
			System.out.println(i1 + ": " + string2);
			System.exit(0);
		}

	}

	public final void destroy() {
		try {
			if(this.soundPlayer != null) {
				SoundPlayer soundPlayer1 = this.soundPlayer;
				this.soundPlayer.running = false;
			}

			if(this.resourceDownloader != null) {
				BackgroundDownloader backgroundDownloader4 = this.resourceDownloader;
				this.resourceDownloader.closing = true;
			}
		} catch (Exception exception3) {
		}

		Minecraft minecraft5 = this;
		
		if (this.player.health > 0) saveToolbar();
		else {
			new File("smpmod_toolbar.dat").delete();
			System.out.println("No Health. Toolbar deleted");
		}

		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}
	
	private void saveToolbar() {
        int[] items = this.player.inventory.slots;
        int[] count = this.player.inventory.count;
        int[] health = new int[1];
        health[0] = this.player.health;

        // Assuming you have a file path where you want to save the data
        String filePath = "smpmod_toolbar.dat";

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(filePath));

            // Write the length of the arrays
            dos.writeInt(items.length);

            // Write each item and its count
            for (int i = 0; i < items.length; i++) {
                dos.writeInt(items[i]);
                dos.writeInt(count[i]);
                
            }
            
            dos.writeInt(health[0]);

            System.out.println("Toolbar data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	private void loadToolbar() {
        // Assuming the file path is the same as where you saved it
        String filePath = "smpmod_toolbar.dat";

        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(filePath));

            // Read the length of the arrays
            int length = dis.readInt();

            // Read items and count arrays
            int[] items = new int[length];
            int[] count = new int[length];

            for (int i = 0; i < length; i++) {
                items[i] = dis.readInt();
                count[i] = dis.readInt();
            }
            
            this.player.health = dis.readInt();

            this.player.inventory.slots = items;
            this.player.inventory.count = count;
            

            System.out.println("Toolbar data loaded successfully.");
            DontLoadToolbar = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No toolbar data found, Loading default");
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public final void run() {
		this.running = true;

		try {
			Minecraft minecraft4 = this;
			if(this.parent != null) {
				Display.setParent(this.parent);
			} else if(this.fullscreen) {
				Display.setFullscreen(true);
				this.width = Display.getDisplayMode().getWidth();
				this.height = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.width, this.height));
			}

			Display.setTitle("Minecraft 0.30");

			try {
				Display.create();
			} catch (LWJGLException lWJGLException46) {
				lWJGLException46.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException interruptedException45) {
				}

				Display.create();
			}

			Keyboard.create();
			Mouse.create();

			try {
				Controllers.create();
			} catch (Exception exception44) {
				exception44.printStackTrace();
			}

			checkGlError("Pre startup");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			checkGlError("Startup");
			String string12 = "minecraft";
			String string13 = System.getProperty("user.home", ".");
			File file14;
			String string16;
			switch(OSMap.osValues[((string16 = System.getProperty("os.name").toLowerCase()).contains("win") ? Minecraft$OS.windows : (string16.contains("mac") ? Minecraft$OS.macos : (string16.contains("solaris") ? Minecraft$OS.solaris : (string16.contains("sunos") ? Minecraft$OS.solaris : (string16.contains("linux") ? Minecraft$OS.linux : (string16.contains("unix") ? Minecraft$OS.linux : Minecraft$OS.unknown)))))).ordinal()]) {
			case 1:
			case 2:
				file14 = new File(string13, '.' + string12 + '/');
				break;
			case 3:
				String string15;
				if((string15 = System.getenv("APPDATA")) != null) {
					file14 = new File(string15, "." + string12 + '/');
				} else {
					file14 = new File(string13, '.' + string12 + '/');
				}
				break;
			case 4:
				file14 = new File(string13, "Library/Application Support/" + string12);
				break;
			default:
				file14 = new File(string13, string12 + '/');
			}

			if(!file14.exists() && !file14.mkdirs()) {
				throw new RuntimeException("The working directory could not be created: " + file14);
			}

			File file7 = file14;
			this.options = new Options(this, file14);
			this.textures = new Textures(this.options);
			this.textures.addDynamicTexture(new LavaTexture());
			this.textures.addDynamicTexture(new WaterTexture());
			this.font = new Font(this.options, "/default.png", this.textures);
			IntBuffer intBuffer8;
			(intBuffer8 = BufferUtils.createIntBuffer(256)).clear().limit(256);
			this.levelRenderer = new LevelRenderer(this, this.textures);
			Mob.modelCache = new ModelCache();
			GL11.glViewport(0, 0, this.width, this.height);
			if(this.server != null && this.user != null) {
				System.out.println(user.name);
				Level level62;
				(level62 = new Level()).setData(8, 8, 8, new byte[512]);
				this.loadLegacy(level62);
				loadToolbar();
			} else {
				this.setScreen(new ErrorScreen("No server found", "Join a server to play SMP Mod"));
				
			}

			this.particleEngine = new ParticleEngine(this.level, this.textures);
			if(this.appletMode) {
				try {
					minecraft4.emptyCursor = new Cursor(16, 16, 0, 0, 1, intBuffer8, (IntBuffer)null);
				} catch (LWJGLException lWJGLException42) {
					lWJGLException42.printStackTrace();
				}
			}

			try {
				minecraft4.soundPlayer = new SoundPlayer(minecraft4.options);
				SoundPlayer soundPlayer11 = minecraft4.soundPlayer;

				try {
					AudioFormat audioFormat75 = new AudioFormat(44100.0F, 16, 2, true, true);
					soundPlayer11.dataLine = AudioSystem.getSourceDataLine(audioFormat75);
					soundPlayer11.dataLine.open(audioFormat75, 4410);
					soundPlayer11.dataLine.start();
					soundPlayer11.running = true;
					Thread thread79;
					(thread79 = new Thread(soundPlayer11)).setDaemon(true);
					thread79.setPriority(10);
					thread79.start();
				} catch (Exception exception40) {
					exception40.printStackTrace();
					soundPlayer11.running = false;
				}

				minecraft4.resourceDownloader = new BackgroundDownloader(file7, minecraft4);
				minecraft4.resourceDownloader.start();
			} catch (Exception exception41) {
			}

			checkGlError("Post startup");
			this.gui = new Gui(this, this.width, this.height);
			(new PlayerTextureLoader(this)).start();
			if(this.server != null && this.user != null) {
				this.networkClient = new Client(this, this.server, this.port, this.user.name, this.user.mpPass);
			}
		} catch (Exception exception51) {
			exception51.printStackTrace();
			JOptionPane.showMessageDialog((Component)null, exception51.toString(), "Failed to start Minecraft", 0);
			return;
		}

		long j1 = System.currentTimeMillis();
		int i3 = 0;

		try {
			while(this.running) {
				if(this.pause) {
					Thread.sleep(100L);
				} else {
					if(this.parent == null && Display.isCloseRequested()) {
						this.running = false;
					}

					try {
						Timer timer52 = this.timer;
						long j59;
						long j63 = (j59 = System.currentTimeMillis()) - timer52.msPerTick;
						long j70 = System.nanoTime() / 1000000L;
						double d86;
						if(j63 > 1000L) {
							long j81 = j70 - timer52.passedTime;
							d86 = (double)j63 / (double)j81;
							timer52.averageFrameTime += (d86 - timer52.averageFrameTime) * (double)0.2F;
							timer52.msPerTick = j59;
							timer52.passedTime = j70;
						}

						if(j63 < 0L) {
							timer52.msPerTick = j59;
							timer52.passedTime = j70;
						}

						double d82;
						d86 = ((d82 = (double)j70 / 1000.0D) - timer52.lastTime) * timer52.averageFrameTime;
						timer52.lastTime = d82;
						if(d86 < 0.0D) {
							d86 = 0.0D;
						}

						if(d86 > 1.0D) {
							d86 = 1.0D;
						}

						timer52.ticks = (float)((double)timer52.ticks + d86 * (double)timer52.fps * (double)timer52.ticksPerSecond);
						timer52.frames = (int)timer52.ticks;
						if(timer52.frames > 100) {
							timer52.frames = 100;
						}

						timer52.ticks -= (float)timer52.frames;
						timer52.alpha = timer52.ticks;

						for(int i53 = 0; i53 < this.timer.frames; ++i53) {
							++this.frames;
							this.tick();
						}

						checkGlError("Pre render");
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						if(!this.hideScreen) {
							this.gamemode.render(this.timer.alpha);
							float f60 = this.timer.alpha;
							GameRenderer gameRenderer54 = this.gameRenderer;
							if(this.gameRenderer.displayActive && !Display.isActive()) {
								gameRenderer54.minecraft.pauseScreen();
							}

							gameRenderer54.displayActive = Display.isActive();
							int i61;
							int i64;
							int i71;
							int i77;
							if(gameRenderer54.minecraft.mouseGrabbed) {
								i61 = 0;
								i64 = 0;
								if(gameRenderer54.minecraft.appletMode) {
									if(gameRenderer54.minecraft.parent != null) {
										Point point65;
										i71 = (point65 = gameRenderer54.minecraft.parent.getLocationOnScreen()).x + gameRenderer54.minecraft.width / 2;
										i77 = point65.y + gameRenderer54.minecraft.height / 2;
										Point point84;
										i61 = (point84 = MouseInfo.getPointerInfo().getLocation()).x - i71;
										i64 = -(point84.y - i77);
										gameRenderer54.minecraft.robot.mouseMove(i71, i77);
									} else {
										Mouse.setCursorPosition(gameRenderer54.minecraft.width / 2, gameRenderer54.minecraft.height / 2);
									}
								} else {
									i61 = Mouse.getDX();
									i64 = Mouse.getDY();
								}

								byte b68 = 1;
								if(gameRenderer54.minecraft.options.invertYMouse) {
									b68 = -1;
								}

								gameRenderer54.minecraft.player.turn((float)i61, (float)(i64 * b68));
							}

							if(!gameRenderer54.minecraft.hideScreen) {
								i61 = gameRenderer54.minecraft.width * 240 / gameRenderer54.minecraft.height;
								i64 = gameRenderer54.minecraft.height * 240 / gameRenderer54.minecraft.height;
								int i73 = Mouse.getX() * i61 / gameRenderer54.minecraft.width;
								i71 = i64 - Mouse.getY() * i64 / gameRenderer54.minecraft.height - 1;
								if(gameRenderer54.minecraft.level != null) {
									float f92 = f60;
									GameRenderer gameRenderer88 = gameRenderer54;
									GameRenderer gameRenderer21 = gameRenderer54;
									Player player23;
									float f24 = (player23 = gameRenderer54.minecraft.player).xRotO + (player23.xRot - player23.xRotO) * f60;
									float f25 = player23.yRotO + (player23.yRot - player23.yRotO) * f60;
									Vec3 vec326 = gameRenderer54.getPlayerRotVec(f60);
									float f27 = Mth.cos(-f25 * 0.017453292F - (float)Math.PI);
									float f78 = Mth.sin(-f25 * 0.017453292F - (float)Math.PI);
									float f85 = Mth.cos(-f24 * 0.017453292F);
									float f17 = Mth.sin(-f24 * 0.017453292F);
									float f18 = f78 * f85;
									float f72 = f27 * f85;
									float f19 = gameRenderer54.minecraft.gamemode.getPickRange();
									Vec3 vec380 = vec326.add(f18 * f19, f17 * f19, f72 * f19);
									gameRenderer54.minecraft.hitResult = gameRenderer54.minecraft.level.clip(vec326, vec380);
									f85 = f19;
									if(gameRenderer54.minecraft.hitResult != null) {
										f85 = gameRenderer54.minecraft.hitResult.vec.distanceTo(gameRenderer54.getPlayerRotVec(f60));
									}

									vec326 = gameRenderer54.getPlayerRotVec(f60);
									if(gameRenderer54.minecraft.gamemode instanceof CreativeGameMode) {
										f19 = 32.0F;
									} else {
										f19 = f85;
									}

									vec380 = vec326.add(f18 * f19, f17 * f19, f72 * f19);
									gameRenderer54.entity = null;
									List list5 = gameRenderer54.minecraft.level.blockMap.getEntities(player23, player23.bb.expand(f18 * f19, f17 * f19, f72 * f19));
									float f6 = 0.0F;

									for(i61 = 0; i61 < list5.size(); ++i61) {
										Entity entity74;
										if((entity74 = (Entity)list5.get(i61)).isPickable()) {
											f85 = 0.1F;
											HitResult hitResult90;
											if((hitResult90 = entity74.bb.grow(f85, f85, f85).clip(vec326, vec380)) != null && ((f85 = vec326.distanceTo(hitResult90.vec)) < f6 || f6 == 0.0F)) {
												gameRenderer21.entity = entity74;
												f6 = f85;
											}
										}
									}

									if(gameRenderer21.entity != null && !(gameRenderer21.minecraft.gamemode instanceof CreativeGameMode)) {
										gameRenderer21.minecraft.hitResult = new HitResult(gameRenderer21.entity);
									}

									int i89 = 0;

									while(true) {
										if(i89 >= 2) {
											GL11.glColorMask(true, true, true, false);
											break;
										}

										if(gameRenderer88.minecraft.options.anaglyph3d) {
											if(i89 == 0) {
												GL11.glColorMask(false, true, true, false);
											} else {
												GL11.glColorMask(true, false, false, false);
											}
										}

										Player player55 = gameRenderer88.minecraft.player;
										Level level57 = gameRenderer88.minecraft.level;
										LevelRenderer levelRenderer66 = gameRenderer88.minecraft.levelRenderer;
										ParticleEngine particleEngine76 = gameRenderer88.minecraft.particleEngine;
										GL11.glViewport(0, 0, gameRenderer88.minecraft.width, gameRenderer88.minecraft.height);
										Level level22 = gameRenderer88.minecraft.level;
										player23 = gameRenderer88.minecraft.player;
										f24 = 1.0F / (float)(4 - gameRenderer88.minecraft.options.viewDistance);
										f24 = 1.0F - (float)Math.pow((double)f24, 0.25D);
										f25 = (float)(level22.skyColor >> 16 & 255) / 255.0F;
										float f112 = (float)(level22.skyColor >> 8 & 255) / 255.0F;
										f27 = (float)(level22.skyColor & 255) / 255.0F;
										gameRenderer88.fogRed = (float)(level22.fogColor >> 16 & 255) / 255.0F;
										gameRenderer88.fogGreen = (float)(level22.fogColor >> 8 & 255) / 255.0F;
										gameRenderer88.fogBlue = (float)(level22.fogColor & 255) / 255.0F;
										gameRenderer88.fogRed += (f25 - gameRenderer88.fogRed) * f24;
										gameRenderer88.fogGreen += (f112 - gameRenderer88.fogGreen) * f24;
										gameRenderer88.fogBlue += (f27 - gameRenderer88.fogBlue) * f24;
										gameRenderer88.fogRed *= gameRenderer88.fogColorMultiplier;
										gameRenderer88.fogGreen *= gameRenderer88.fogColorMultiplier;
										gameRenderer88.fogBlue *= gameRenderer88.fogColorMultiplier;
										Tile tile83;
										if((tile83 = Tile.tiles[level22.getTile((int)player23.x, (int)(player23.y + 0.12F), (int)player23.z)]) != null && tile83.getLiquidType() != Liquid.none) {
											Liquid liquid91;
											if((liquid91 = tile83.getLiquidType()) == Liquid.water) {
												gameRenderer88.fogRed = 0.02F;
												gameRenderer88.fogGreen = 0.02F;
												gameRenderer88.fogBlue = 0.2F;
											} else if(liquid91 == Liquid.lava) {
												gameRenderer88.fogRed = 0.6F;
												gameRenderer88.fogGreen = 0.1F;
												gameRenderer88.fogBlue = 0.0F;
											}
										}

										if(gameRenderer88.minecraft.options.anaglyph3d) {
											f85 = (gameRenderer88.fogRed * 30.0F + gameRenderer88.fogGreen * 59.0F + gameRenderer88.fogBlue * 11.0F) / 100.0F;
											f17 = (gameRenderer88.fogRed * 30.0F + gameRenderer88.fogGreen * 70.0F) / 100.0F;
											f18 = (gameRenderer88.fogRed * 30.0F + gameRenderer88.fogBlue * 70.0F) / 100.0F;
											gameRenderer88.fogRed = f85;
											gameRenderer88.fogGreen = f17;
											gameRenderer88.fogBlue = f18;
										}

										GL11.glClearColor(gameRenderer88.fogRed, gameRenderer88.fogGreen, gameRenderer88.fogBlue, 0.0F);
										GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
										gameRenderer88.fogColorMultiplier = 1.0F;
										GL11.glEnable(GL11.GL_CULL_FACE);
										gameRenderer88.renderDistance = (float)(512 >> (gameRenderer88.minecraft.options.viewDistance << 1));
										GL11.glMatrixMode(GL11.GL_PROJECTION);
										GL11.glLoadIdentity();
										f24 = 0.07F;
										if(gameRenderer88.minecraft.options.anaglyph3d) {
											GL11.glTranslatef((float)(-((i89 << 1) - 1)) * f24, 0.0F, 0.0F);
										}

										Player player115 = gameRenderer88.minecraft.player;
										f78 = 70.0F;
										if(player115.health <= 0) {
											f85 = (float)player115.deathTime + f92;
											f78 /= (1.0F - 500.0F / (f85 + 500.0F)) * 2.0F + 1.0F;
										}

										GLU.gluPerspective(f78, (float)gameRenderer88.minecraft.width / (float)gameRenderer88.minecraft.height, 0.05F, gameRenderer88.renderDistance);
										GL11.glMatrixMode(GL11.GL_MODELVIEW);
										GL11.glLoadIdentity();
										if(gameRenderer88.minecraft.options.anaglyph3d) {
											GL11.glTranslatef((float)((i89 << 1) - 1) * 0.1F, 0.0F, 0.0F);
										}

										gameRenderer88.renderHurtFrames(f92);
										if(gameRenderer88.minecraft.options.bobView) {
											gameRenderer88.cameraBob(f92);
										}

										player115 = gameRenderer88.minecraft.player;
										GL11.glTranslatef(0.0F, 0.0F, -0.1F);
										GL11.glRotatef(player115.xRotO + (player115.xRot - player115.xRotO) * f92, 1.0F, 0.0F, 0.0F);
										GL11.glRotatef(player115.yRotO + (player115.yRot - player115.yRotO) * f92, 0.0F, 1.0F, 0.0F);
										f78 = player115.xo + (player115.x - player115.xo) * f92;
										f85 = player115.yo + (player115.y - player115.yo) * f92;
										f17 = player115.zo + (player115.z - player115.zo) * f92;
										GL11.glTranslatef(-f78, -f85, -f17);
										Frustum frustum87 = FrustumCuller.calculateFrustum();
										Frustum frustum102 = frustum87;
										LevelRenderer levelRenderer99 = gameRenderer88.minecraft.levelRenderer;

										int i106;
										for(i106 = 0; i106 < levelRenderer99.chunks.length; ++i106) {
											levelRenderer99.chunks[i106].isInFrustum(frustum102);
										}

										levelRenderer99 = gameRenderer88.minecraft.levelRenderer;
										Collections.sort(gameRenderer88.minecraft.levelRenderer.allDirtyChunks, new DirtyChunkSorter(player55));
										i106 = levelRenderer99.allDirtyChunks.size() - 1;
										int i109;
										if((i109 = levelRenderer99.allDirtyChunks.size()) > 3) {
											i109 = 3;
										}

										int i110;
										for(i110 = 0; i110 < i109; ++i110) {
											Chunk chunk113;
											(chunk113 = (Chunk)levelRenderer99.allDirtyChunks.remove(i106 - i110)).rebuild();
											chunk113.dirty = false;
										}

										gameRenderer88.setupFog();
										GL11.glEnable(GL11.GL_FOG);
										levelRenderer66.render(player55, 0);
										int i58;
										int i93;
										int i94;
										int i95;
										int i98;
										Tesselator tesselator114;
										int i117;
										if(level57.isSolid(player55.x, player55.y, player55.z, 0.1F)) {
											i58 = (int)player55.x;
											i94 = (int)player55.y;
											i93 = (int)player55.z;

											for(i95 = i58 - 1; i95 <= i58 + 1; ++i95) {
												for(i98 = i94 - 1; i98 <= i94 + 1; ++i98) {
													for(int i20 = i93 - 1; i20 <= i93 + 1; ++i20) {
														i109 = i20;
														i106 = i98;
														int i103 = i95;
														if((i110 = levelRenderer66.level.getTile(i95, i98, i20)) != 0 && Tile.tiles[i110].isSolid()) {
															GL11.glColor4f(0.2F, 0.2F, 0.2F, 1.0F);
															GL11.glDepthFunc(GL11.GL_LESS);
															tesselator114 = Tesselator.instance;
															Tesselator.instance.begin();

															for(i117 = 0; i117 < 6; ++i117) {
																Tile.tiles[i110].renderFace(tesselator114, i103, i106, i109, i117);
															}

															tesselator114.end();
															GL11.glCullFace(GL11.GL_FRONT);
															tesselator114.begin();

															for(i117 = 0; i117 < 6; ++i117) {
																Tile.tiles[i110].renderFace(tesselator114, i103, i106, i109, i117);
															}

															tesselator114.end();
															GL11.glCullFace(GL11.GL_BACK);
															GL11.glDepthFunc(GL11.GL_LEQUAL);
														}
													}
												}
											}
										}

										gameRenderer88.toggleLight(true);
										Vec3 vec3104 = gameRenderer88.getPlayerRotVec(f92);
										levelRenderer66.level.blockMap.render(vec3104, frustum87, levelRenderer66.textures, f92);
										gameRenderer88.toggleLight(false);
										gameRenderer88.setupFog();
										float f108 = f92;
										ParticleEngine particleEngine101 = particleEngine76;
										f24 = -Mth.cos(player55.yRot * (float)Math.PI / 180.0F);
										f112 = -(f25 = -Mth.sin(player55.yRot * (float)Math.PI / 180.0F)) * Mth.sin(player55.xRot * (float)Math.PI / 180.0F);
										f27 = f24 * Mth.sin(player55.xRot * (float)Math.PI / 180.0F);
										f78 = Mth.cos(player55.xRot * (float)Math.PI / 180.0F);

										for(i94 = 0; i94 < 2; ++i94) {
											if(particleEngine101.particles[i94].size() != 0) {
												i93 = 0;
												if(i94 == 0) {
													i93 = particleEngine101.textures.loadTexture("/particles.png");
												}

												if(i94 == 1) {
													i93 = particleEngine101.textures.loadTexture("/terrain.png");
												}

												GL11.glBindTexture(GL11.GL_TEXTURE_2D, i93);
												Tesselator tesselator97 = Tesselator.instance;
												Tesselator.instance.begin();

												for(i58 = 0; i58 < particleEngine101.particles[i94].size(); ++i58) {
													((Particle)particleEngine101.particles[i94].get(i58)).render(tesselator97, f108, f24, f78, f25, f112, f27);
												}

												tesselator97.end();
											}
										}

										GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer66.textures.loadTexture("/rock.png"));
										GL11.glEnable(GL11.GL_TEXTURE_2D);
										GL11.glCallList(levelRenderer66.surroundLists);
										gameRenderer88.setupFog();
										levelRenderer99 = levelRenderer66;
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer66.textures.loadTexture("/clouds.png"));
										GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
										f108 = (float)(levelRenderer66.level.cloudColor >> 16 & 255) / 255.0F;
										f24 = (float)(levelRenderer66.level.cloudColor >> 8 & 255) / 255.0F;
										f25 = (float)(levelRenderer66.level.cloudColor & 255) / 255.0F;
										if(levelRenderer66.minecraft.options.anaglyph3d) {
											f112 = (f108 * 30.0F + f24 * 59.0F + f25 * 11.0F) / 100.0F;
											f27 = (f108 * 30.0F + f24 * 70.0F) / 100.0F;
											f78 = (f108 * 30.0F + f25 * 70.0F) / 100.0F;
											f108 = f112;
											f24 = f27;
											f25 = f78;
										}

										tesselator114 = Tesselator.instance;
										f85 = 0.0F;
										f17 = 4.8828125E-4F;
										f85 = (float)(levelRenderer66.level.depth + 2);
										f18 = ((float)levelRenderer66.cloudTickCounter + f92) * f17 * 0.03F;
										f6 = 0.0F;
										tesselator114.begin();
										tesselator114.color(f108, f24, f25);

										for(i64 = -2048; i64 < levelRenderer99.level.width + 2048; i64 += 512) {
											for(i98 = -2048; i98 < levelRenderer99.level.height + 2048; i98 += 512) {
												tesselator114.vertexUV((float)i64, f85, (float)(i98 + 512), (float)i64 * f17 + f18, (float)(i98 + 512) * f17);
												tesselator114.vertexUV((float)(i64 + 512), f85, (float)(i98 + 512), (float)(i64 + 512) * f17 + f18, (float)(i98 + 512) * f17);
												tesselator114.vertexUV((float)(i64 + 512), f85, (float)i98, (float)(i64 + 512) * f17 + f18, (float)i98 * f17);
												tesselator114.vertexUV((float)i64, f85, (float)i98, (float)i64 * f17 + f18, (float)i98 * f17);
												tesselator114.vertexUV((float)i64, f85, (float)i98, (float)i64 * f17 + f18, (float)i98 * f17);
												tesselator114.vertexUV((float)(i64 + 512), f85, (float)i98, (float)(i64 + 512) * f17 + f18, (float)i98 * f17);
												tesselator114.vertexUV((float)(i64 + 512), f85, (float)(i98 + 512), (float)(i64 + 512) * f17 + f18, (float)(i98 + 512) * f17);
												tesselator114.vertexUV((float)i64, f85, (float)(i98 + 512), (float)i64 * f17 + f18, (float)(i98 + 512) * f17);
											}
										}

										tesselator114.end();
										GL11.glDisable(GL11.GL_TEXTURE_2D);
										tesselator114.begin();
										f18 = (float)(levelRenderer99.level.skyColor >> 16 & 255) / 255.0F;
										f6 = (float)(levelRenderer99.level.skyColor >> 8 & 255) / 255.0F;
										f72 = (float)(levelRenderer99.level.skyColor & 255) / 255.0F;
										if(levelRenderer99.minecraft.options.anaglyph3d) {
											f19 = (f18 * 30.0F + f6 * 59.0F + f72 * 11.0F) / 100.0F;
											f78 = (f18 * 30.0F + f6 * 70.0F) / 100.0F;
											f85 = (f18 * 30.0F + f72 * 70.0F) / 100.0F;
											f18 = f19;
											f6 = f78;
											f72 = f85;
										}

										tesselator114.color(f18, f6, f72);
										f85 = (float)(levelRenderer99.level.depth + 10);

										for(i98 = -2048; i98 < levelRenderer99.level.width + 2048; i98 += 512) {
											for(i77 = -2048; i77 < levelRenderer99.level.height + 2048; i77 += 512) {
												tesselator114.vertex((float)i98, f85, (float)i77);
												tesselator114.vertex((float)(i98 + 512), f85, (float)i77);
												tesselator114.vertex((float)(i98 + 512), f85, (float)(i77 + 512));
												tesselator114.vertex((float)i98, f85, (float)(i77 + 512));
											}
										}

										tesselator114.end();
										GL11.glEnable(GL11.GL_TEXTURE_2D);
										gameRenderer88.setupFog();
										int i118;
										if(gameRenderer88.minecraft.hitResult != null) {
											GL11.glDisable(GL11.GL_ALPHA_TEST);
											HitResult hitResult10001 = gameRenderer88.minecraft.hitResult;
											i109 = player55.inventory.getSelected();
											boolean z111 = false;
											HitResult hitResult105 = hitResult10001;
											levelRenderer99 = levelRenderer66;
											Tesselator tesselator116 = Tesselator.instance;
											GL11.glEnable(GL11.GL_BLEND);
											GL11.glEnable(GL11.GL_ALPHA_TEST);
											GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
											GL11.glColor4f(1.0F, 1.0F, 1.0F, (Mth.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
											if(levelRenderer66.hurtTime > 0.0F) {
												GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
												i118 = levelRenderer66.textures.loadTexture("/terrain.png");
												GL11.glBindTexture(GL11.GL_TEXTURE_2D, i118);
												GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
												GL11.glPushMatrix();
												Tile tile10000 = (i117 = levelRenderer66.level.getTile(hitResult105.x, hitResult105.y, hitResult105.z)) > 0 ? Tile.tiles[i117] : null;
												tile83 = tile10000;
												f85 = (tile10000.xx0 + tile83.xx1) / 2.0F;
												f17 = (tile83.yy0 + tile83.yy1) / 2.0F;
												f18 = (tile83.zz0 + tile83.zz1) / 2.0F;
												GL11.glTranslatef((float)hitResult105.x + f85, (float)hitResult105.y + f17, (float)hitResult105.z + f18);
												f6 = 1.01F;
												GL11.glScalef(1.01F, f6, f6);
												GL11.glTranslatef(-((float)hitResult105.x + f85), -((float)hitResult105.y + f17), -((float)hitResult105.z + f18));
												tesselator116.begin();
												tesselator116.noColor();
												GL11.glDepthMask(false);
												if(tile83 == null) {
													tile83 = Tile.rock;
												}

												for(i64 = 0; i64 < 6; ++i64) {
													tile83.renderFaceNoTexture(tesselator116, hitResult105.x, hitResult105.y, hitResult105.z, i64, 240 + (int)(levelRenderer99.hurtTime * 10.0F));
												}

												tesselator116.end();
												GL11.glDepthMask(true);
												GL11.glPopMatrix();
											}

											GL11.glDisable(GL11.GL_BLEND);
											GL11.glDisable(GL11.GL_ALPHA_TEST);
											hitResult10001 = gameRenderer88.minecraft.hitResult;
											player55.inventory.getSelected();
											z111 = false;
											hitResult105 = hitResult10001;
											GL11.glEnable(GL11.GL_BLEND);
											GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
											GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
											GL11.glLineWidth(2.0F);
											GL11.glDisable(GL11.GL_TEXTURE_2D);
											GL11.glDepthMask(false);
											f24 = 0.002F;
											if((i110 = levelRenderer66.level.getTile(hitResult105.x, hitResult105.y, hitResult105.z)) > 0) {
												AABB aABB120 = Tile.tiles[i110].getAABB(hitResult105.x, hitResult105.y, hitResult105.z).grow(f24, f24, f24);
												GL11.glBegin(GL11.GL_LINE_STRIP);
												GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
												GL11.glEnd();
												GL11.glBegin(GL11.GL_LINE_STRIP);
												GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
												GL11.glEnd();
												GL11.glBegin(GL11.GL_LINES);
												GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z0);
												GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z0);
												GL11.glVertex3f(aABB120.x1, aABB120.y0, aABB120.z1);
												GL11.glVertex3f(aABB120.x1, aABB120.y1, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y0, aABB120.z1);
												GL11.glVertex3f(aABB120.x0, aABB120.y1, aABB120.z1);
												GL11.glEnd();
											}

											GL11.glDepthMask(true);
											GL11.glEnable(GL11.GL_TEXTURE_2D);
											GL11.glDisable(GL11.GL_BLEND);
											GL11.glEnable(GL11.GL_ALPHA_TEST);
										}

										GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
										gameRenderer88.setupFog();
										GL11.glEnable(GL11.GL_TEXTURE_2D);
										GL11.glEnable(GL11.GL_BLEND);
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer66.textures.loadTexture("/water.png"));
										GL11.glCallList(levelRenderer66.surroundLists + 1);
										GL11.glDisable(GL11.GL_BLEND);
										GL11.glEnable(GL11.GL_BLEND);
										GL11.glColorMask(false, false, false, false);
										i58 = levelRenderer66.render(player55, 1);
										GL11.glColorMask(true, true, true, true);
										if(gameRenderer88.minecraft.options.anaglyph3d) {
											if(i89 == 0) {
												GL11.glColorMask(false, true, true, false);
											} else {
												GL11.glColorMask(true, false, false, false);
											}
										}

										if(i58 > 0) {
											GL11.glBindTexture(GL11.GL_TEXTURE_2D, levelRenderer66.textures.loadTexture("/terrain.png"));
											GL11.glCallLists(levelRenderer66.ib);
										}

										GL11.glDepthMask(true);
										GL11.glDisable(GL11.GL_BLEND);
										GL11.glDisable(GL11.GL_FOG);
										if(gameRenderer88.minecraft.raining) {
											float f107 = f92;
											gameRenderer21 = gameRenderer88;
											player23 = gameRenderer88.minecraft.player;
											Level level119 = gameRenderer88.minecraft.level;
											i110 = (int)player23.x;
											i118 = (int)player23.y;
											i117 = (int)player23.z;
											Tesselator tesselator96 = Tesselator.instance;
											GL11.glDisable(GL11.GL_CULL_FACE);
											GL11.glNormal3f(0.0F, 1.0F, 0.0F);
											GL11.glEnable(GL11.GL_BLEND);
											GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
											GL11.glBindTexture(GL11.GL_TEXTURE_2D, gameRenderer88.minecraft.textures.loadTexture("/rain.png"));
											i93 = i110 - 5;

											while(true) {
												if(i93 > i110 + 5) {
													GL11.glEnable(GL11.GL_CULL_FACE);
													GL11.glDisable(GL11.GL_BLEND);
													break;
												}

												for(i95 = i117 - 5; i95 <= i117 + 5; ++i95) {
													i58 = level119.getHighestTile(i93, i95);
													i64 = i118 - 5;
													i98 = i118 + 5;
													if(i64 < i58) {
														i64 = i58;
													}

													if(i98 < i58) {
														i98 = i58;
													}

													if(i64 != i98) {
														f85 = ((float)((gameRenderer21.rainTicks + i93 * 3121 + i95 * 418711) % 32) + f107) / 32.0F;
														float f56 = (float)i93 + 0.5F - player23.x;
														f6 = (float)i95 + 0.5F - player23.z;
														float f69 = Mth.sqrt(f56 * f56 + f6 * f6) / (float)5;
														GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f69 * f69) * 0.7F);
														tesselator96.begin();
														tesselator96.vertexUV((float)i93, (float)i64, (float)i95, 0.0F, (float)i64 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)(i93 + 1), (float)i64, (float)(i95 + 1), 2.0F, (float)i64 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)(i93 + 1), (float)i98, (float)(i95 + 1), 2.0F, (float)i98 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)i93, (float)i98, (float)i95, 0.0F, (float)i98 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)i93, (float)i64, (float)(i95 + 1), 0.0F, (float)i64 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)(i93 + 1), (float)i64, (float)i95, 2.0F, (float)i64 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)(i93 + 1), (float)i98, (float)i95, 2.0F, (float)i98 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.vertexUV((float)i93, (float)i98, (float)(i95 + 1), 0.0F, (float)i98 * 2.0F / 8.0F + f85 * 2.0F);
														tesselator96.end();
													}
												}

												++i93;
											}
										}

										if(gameRenderer88.entity != null) {
											gameRenderer88.entity.renderHover(gameRenderer88.minecraft.textures, f92);
										}

										GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
										GL11.glLoadIdentity();
										if(gameRenderer88.minecraft.options.anaglyph3d) {
											GL11.glTranslatef((float)((i89 << 1) - 1) * 0.1F, 0.0F, 0.0F);
										}

										gameRenderer88.renderHurtFrames(f92);
										if(gameRenderer88.minecraft.options.bobView) {
											gameRenderer88.cameraBob(f92);
										}

										TileRenderer tileRenderer121 = gameRenderer88.tileRenderer;
										f112 = gameRenderer88.tileRenderer.oProgress + (tileRenderer121.progress - tileRenderer121.oProgress) * f92;
										player115 = tileRenderer121.minecraft.player;
										GL11.glPushMatrix();
										GL11.glRotatef(player115.xRotO + (player115.xRot - player115.xRotO) * f92, 1.0F, 0.0F, 0.0F);
										GL11.glRotatef(player115.yRotO + (player115.yRot - player115.yRotO) * f92, 0.0F, 1.0F, 0.0F);
										tileRenderer121.minecraft.gameRenderer.toggleLight(true);
										GL11.glPopMatrix();
										GL11.glPushMatrix();
										f78 = 0.8F;
										if(tileRenderer121.move) {
											f17 = Mth.sin((f85 = ((float)tileRenderer121.rot + f92) / 7.0F) * (float)Math.PI);
											GL11.glTranslatef(-Mth.sin(Mth.sqrt(f85) * (float)Math.PI) * 0.4F, Mth.sin(Mth.sqrt(f85) * (float)Math.PI * 2.0F) * 0.2F, -f17 * 0.2F);
										}

										GL11.glTranslatef(0.7F * f78, -0.65F * f78 - (1.0F - f112) * 0.6F, -0.9F * f78);
										GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
										GL11.glEnable(GL11.GL_NORMALIZE);
										if(tileRenderer121.move) {
											f17 = Mth.sin((f85 = ((float)tileRenderer121.rot + f92) / 7.0F) * f85 * (float)Math.PI);
											GL11.glRotatef(Mth.sin(Mth.sqrt(f85) * (float)Math.PI) * 80.0F, 0.0F, 1.0F, 0.0F);
											GL11.glRotatef(-f17 * 20.0F, 1.0F, 0.0F, 0.0F);
										}

										GL11.glColor4f(f85 = tileRenderer121.minecraft.level.getBrightness((int)player115.x, (int)player115.y, (int)player115.z), f85, f85, 1.0F);
										Tesselator tesselator100 = Tesselator.instance;
										if(tileRenderer121.tile != null) {
											if (tileRenderer121.tile.getId() < 101) {
												f18 = 0.4F;
												GL11.glScalef(0.4F, f18, f18);
												GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
												GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileRenderer121.minecraft.textures.loadTexture("/terrain.png"));
												tileRenderer121.tile.renderGuiTile(tesselator100);
											}
											else {
												f18 = 0.6F;
												GL11.glScalef(0.6F, 0.6F, f18);
												GL11.glBindTexture(GL11.GL_TEXTURE_2D, tileRenderer121.minecraft.textures.loadTexture("/terrain.png"));
												GL11.glDisable(GL11.GL_LIGHTING);
												Tesselator tessellator6 = Tesselator.instance;
												float f1 = (float)(tileRenderer121.tile.tex % 16 << 4) / 256.0F;
												float f2 = (float)((tileRenderer121.tile.tex % 16 << 4) + 16) / 256.0F;
												float f9 = (float)(tileRenderer121.tile.tex / 16 << 4) / 256.0F;
												float f4 = (float)((tileRenderer121.tile.tex / 16 << 4) + 16) / 256.0F;
												tessellator6.begin();
												tessellator6.vertexUV(-0.4F, -0.2F, -0.4F, f1, f4);
												tessellator6.vertexUV(0.29999998F, -0.2F, 0.29999998F, f2, f4);
												tessellator6.vertexUV(0.29999998F, 0.8F, 0.29999998F, f2, f9);
												tessellator6.vertexUV(-0.4F, 0.8F, -0.4F, f1, f9);
												tessellator6.end();
												GL11.glEnable(GL11.GL_LIGHTING);
											}
										} else {
											player115.bindTexture(tileRenderer121.minecraft.textures);
											GL11.glScalef(1.0F, -1.0F, -1.0F);
											GL11.glTranslatef(0.0F, 0.2F, 0.0F);
											GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
											GL11.glScalef(1.0F, 1.0F, 1.0F);
											f18 = 0.0625F;
											Cube cube67;
											if(!(cube67 = tileRenderer121.minecraft.player.getModel().leftArm).compiled) {
												cube67.translateTo(f18);
											}

											GL11.glCallList(cube67.list);
										}

										GL11.glDisable(GL11.GL_NORMALIZE);
										GL11.glPopMatrix();
										tileRenderer121.minecraft.gameRenderer.toggleLight(false);
										if(!gameRenderer88.minecraft.options.anaglyph3d) {
											break;
										}

										++i89;
									}

									gameRenderer54.minecraft.gui.render(f60, gameRenderer54.minecraft.screen != null, i73, i71);
								} else {
									GL11.glViewport(0, 0, gameRenderer54.minecraft.width, gameRenderer54.minecraft.height);
									GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
									GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
									GL11.glMatrixMode(GL11.GL_PROJECTION);
									GL11.glLoadIdentity();
									GL11.glMatrixMode(GL11.GL_MODELVIEW);
									GL11.glLoadIdentity();
									gameRenderer54.render();
								}

								if(gameRenderer54.minecraft.screen != null) {
									gameRenderer54.minecraft.screen.render(i73, i71);
								}

								Thread.yield();
								Display.update();
							}
						}

						if(this.options.limitFramerate) {
							Thread.sleep(5L);
						}

						checkGlError("Post render");
						++i3;
					} catch (Exception exception47) {
						this.setScreen(new ErrorScreen("Client error", "The game broke! [" + exception47 + "]"));
						exception47.printStackTrace();
					}

					while(System.currentTimeMillis() >= j1 + 1000L) {
						this.fpsString = i3 + " fps, " + Chunk.updates + " chunk updates";
						Chunk.updates = 0;
						j1 += 1000L;
						i3 = 0;
					}
				}
			}

			return;
		} catch (StopGameException stopGameException48) {
			return;
		} catch (Exception exception49) {
			exception49.printStackTrace();
		} finally {
			this.destroy();
		}

	}

	public final void grabMouse() {
		if(!this.mouseGrabbed) {
			this.mouseGrabbed = true;
			if(this.appletMode) {
				try {
					Mouse.setNativeCursor(this.emptyCursor);
					Mouse.setCursorPosition(this.width / 2, this.height / 2);
				} catch (LWJGLException lWJGLException2) {
					lWJGLException2.printStackTrace();
				}

				if(this.parent == null) {
					this.parent.requestFocus();
				}
			} else {
				Mouse.setGrabbed(true);
			}

			this.setScreen((Screen)null);
			this.oFrames = this.frames + 10000;
		}
	}

	public final void pauseScreen() {
		if(this.screen == null) {
			this.setScreen(new PauseScreen());
		}
	}

	private void clickMouse(int id) {
		if(id != 0 || this.clickCounter <= 0) {
			TileRenderer tileRenderer6;
			if(id == 0) {
				tileRenderer6 = this.gameRenderer.tileRenderer;
				this.gameRenderer.tileRenderer.rot = -1;
				tileRenderer6.move = true;
			}
			
			if (id == 1) {
				if (this.player.inventory.getSelected() == Tile.apple.getId()) {
					int health = this.player.health;
					health += 4;
					this.player.health = Math.min(health, 20);
					this.player.inventory.count[this.player.inventory.selected]--;
					if (this.player.inventory.count[this.player.inventory.selected] <= 0)
					this.player.inventory.slots[this.player.inventory.selected] = -1;
				}
			}

			int i2;
			if(id == 1 && (i2 = this.player.inventory.getSelected()) > 0 && this.gamemode.removeResource(this.player, i2)) {
				tileRenderer6 = this.gameRenderer.tileRenderer;
				this.gameRenderer.tileRenderer.progress = 0.0F;
			} else if(this.hitResult == null) {
				if(id == 0 && !(this.gamemode instanceof CreativeGameMode)) {
					this.clickCounter = 10;
				}

			} else {
				if(this.hitResult.type == 1) {
					if(id == 0) {
						this.hitResult.entity.hurt(this.player, 4);
						return;
					}
				} else if(this.hitResult.type == 0) {
					i2 = this.hitResult.x;
					int i3 = this.hitResult.y;
					int i4 = this.hitResult.z;
					if(id != 0) {
						if(this.hitResult.f == 0) {
							--i3;
						}

						if(this.hitResult.f == 1) {
							++i3;
						}

						if(this.hitResult.f == 2) {
							--i4;
						}

						if(this.hitResult.f == 3) {
							++i4;
						}

						if(this.hitResult.f == 4) {
							--i2;
						}

						if(this.hitResult.f == 5) {
							++i2;
						}
					}

					Tile tile5 = Tile.tiles[this.level.getTile(i2, i3, i4)];
					if(id == 0) {
						if(tile5 != Tile.unbreakable || this.player.userType >= 100) {
							this.gamemode.startDestroyBlock(i2, i3, i4);
							return;
						}
					} else {
						int i8;
						if((i8 = this.player.inventory.getSelected()) <= 0) {
							return;
						}

						Tile tile9;
						AABB aABB10;
						if(((tile9 = Tile.tiles[this.level.getTile(i2, i3, i4)]) == null || tile9 == Tile.water || tile9 == Tile.calmWater || tile9 == Tile.lava || tile9 == Tile.calmLava) && ((aABB10 = Tile.tiles[i8].getTileAABB(i2, i3, i4)) == null || (this.player.bb.intersects(aABB10) ? false : this.level.isFree(aABB10)))) {
							if (Tile.tiles[i8].getId() > 100) return;
							
							if(!this.gamemode.removeResource(i8)) {
								return;
							}
							
							/*if (Tile.tiles[i8] == Tile.bush && this.level.getTile(i2, i3 - 1, i4) == 2) {
								ComboTile(i2, i3, i4, id, 17);
								ComboTile(i2, i3 + 1, i4, id, 17);
								ComboTile(i2, i3 + 2, i4, id, 17);
								ComboTile(i2, i3 + 3, i4, id, 17);
								
								ComboTile(i2 + 0, i3 + 2, i4 + 0, id, 18);
								ComboTile(i2 - 1, i3 + 2, i4 + 0, id, 18);
								ComboTile(i2 + 0, i3 + 2, i4 - 1, id, 18);
								ComboTile(i2 - 1, i3 + 2, i4 - 1, id, 18);
								ComboTile(i2 + 0, i3 + 2, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 2, i4 + 0, id, 18);
								ComboTile(i2 + 1, i3 + 2, i4 + 1, id, 18);

								ComboTile(i2 - 1, i3 + 3, i4 + 0, id, 18);
								ComboTile(i2 + 0, i3 + 3, i4 - 1, id, 18);
								ComboTile(i2 - 1, i3 + 3, i4 - 1, id, 18);
								ComboTile(i2 + 0, i3 + 3, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 3, i4 + 0, id, 18);
								ComboTile(i2 + 1, i3 + 3, i4 + 1, id, 18);
								
								ComboTile(i2 + 0, i3 + 4, i4 + 0, id, 18);
								ComboTile(i2 - 1, i3 + 4, i4 + 0, id, 18);
								ComboTile(i2 + 0, i3 + 4, i4 - 1, id, 18);
								ComboTile(i2 - 1, i3 + 4, i4 - 1, id, 18);
								ComboTile(i2 + 0, i3 + 4, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 4, i4 + 0, id, 18);
								ComboTile(i2 + 1, i3 + 4, i4 + 1, id, 18);
								
								ComboTile(i2 - 1, i3 + 2, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 2, i4 - 1, id, 18);
								
								ComboTile(i2 - 1, i3 + 3, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 3, i4 - 1, id, 18);
								
								ComboTile(i2 - 1, i3 + 4, i4 + 1, id, 18);
								ComboTile(i2 + 1, i3 + 4, i4 - 1, id, 18);
								this.gameRenderer.tileRenderer.progress = 0.0F;
								return;
							}*/

							if(this.isOnlineClient()) {
								this.networkClient.sendTileUpdated(i2, i3, i4, id, i8);
							}

							this.level.netSetTile(i2, i3, i4, i8);
							tileRenderer6 = this.gameRenderer.tileRenderer;
							this.gameRenderer.tileRenderer.progress = 0.0F;
							Tile.tiles[i8].onPlace(this.level, i2, i3, i4);
						}
					}
				}

			}
		}
	}
	
	/*private void ComboTile(int x, int y, int z, int mouseID, int blockID) {
		if(this.isOnlineClient()) {
			this.networkClient.sendTileUpdated(x, y, z, mouseID, blockID);
		}
		
		this.level.netSetTile(x, y, z, blockID);
		Tile.tiles[blockID].onPlace(this.level, x, y, z);
	}*/

	private void tick() {
		if(this.soundPlayer != null) {
			SoundPlayer soundPlayer2 = this.soundPlayer;
			SoundEngine soundEngine1 = this.soundEngine;
			if(System.currentTimeMillis() > soundEngine1.lastMusic && soundEngine1.playMusic(soundPlayer2, "calm")) {
				soundEngine1.lastMusic = System.currentTimeMillis() + (long)soundEngine1.random.nextInt(900000) + 300000L;
			}
		}

		this.gamemode.tick();
		Gui gui16 = this.gui;
		++this.gui.tickCounter;

		int i19;
		for(i19 = 0; i19 < gui16.messages.size(); ++i19) {
			++((GuiMessage)gui16.messages.get(i19)).counter;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
		Textures textures17 = this.textures;

		for(i19 = 0; i19 < textures17.textureList.size(); ++i19) {
			DynamicTexture dynamicTexture3;
			(dynamicTexture3 = (DynamicTexture)textures17.textureList.get(i19)).anaglyph = textures17.options.anaglyph3d;
			dynamicTexture3.tick();
			textures17.pixels.clear();
			textures17.pixels.put(dynamicTexture3.pixels);
			textures17.pixels.position(0).limit(dynamicTexture3.pixels.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, dynamicTexture3.tex % 16 << 4, dynamicTexture3.tex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textures17.pixels);
		}

		int i4;
		int i8;
		int i38;
		int i46;
		int i47;
		if(this.networkClient != null && !(this.screen instanceof ErrorScreen)) {
			if(!this.networkClient.isConnected()) {
				this.loadingScreen.beginLevelLoading("Connecting..");
				this.loadingScreen.setLoadingProgress(0);
			} else {
				Client client18 = this.networkClient;
				if(this.networkClient.processData) {
					SocketConnection socketConnection22 = client18.serverConnection;
					if(client18.serverConnection.connected) {
						try {
							SocketConnection socketConnection21 = client18.serverConnection;
							client18.serverConnection.socketChannel.read(socketConnection21.readBuffer);
							i4 = 0;

							while(socketConnection21.readBuffer.position() > 0 && i4++ != 100) {
								socketConnection21.readBuffer.flip();
								byte b5 = socketConnection21.readBuffer.get(0);
								Packet packet6;
								if((packet6 = Packet.PACKETS[b5]) == null) {
									throw new IOException("Bad command: " + b5);
								}

								if(socketConnection21.readBuffer.remaining() < packet6.size + 1) {
									socketConnection21.readBuffer.compact();
									break;
								}

								socketConnection21.readBuffer.get();
								Object[] object7 = new Object[packet6.fields.length];

								for(i8 = 0; i8 < object7.length; ++i8) {
									object7[i8] = socketConnection21.read(packet6.fields[i8]);
								}

								Client client43 = socketConnection21.client;
								if(socketConnection21.client.processData) {
									if(packet6 == Packet.LOGIN) {
										client43.minecraft.loadingScreen.beginLevelLoading(object7[1].toString());
										client43.minecraft.loadingScreen.levelLoadUpdate(object7[2].toString());
										client43.minecraft.player.userType = ((Byte)object7[3]).byteValue();
									} else if(packet6 == Packet.LEVEL_INITIALIZE) {
										client43.minecraft.loadLegacy((Level)null);
										client43.levelBuffer = new ByteArrayOutputStream();
									} else if(packet6 == Packet.LEVEL_DATA_CHUNK) {
										short s11 = ((Short)object7[0]).shortValue();
										byte[] b12 = (byte[])((byte[])object7[1]);
										byte b13 = ((Byte)object7[2]).byteValue();
										client43.minecraft.loadingScreen.setLoadingProgress(b13);
										client43.levelBuffer.write(b12, 0, s11);
									} else if(packet6 == Packet.LEVEL_FINALIZE) {
										try {
											client43.levelBuffer.close();
										} catch (IOException iOException14) {
											iOException14.printStackTrace();
										}

										byte[] b56 = LevelIO.loadBlocks(new ByteArrayInputStream(client43.levelBuffer.toByteArray()));
										client43.levelBuffer = null;
										short s59 = ((Short)object7[0]).shortValue();
										short s62 = ((Short)object7[1]).shortValue();
										short s27 = ((Short)object7[2]).shortValue();
										Level level30;
										(level30 = new Level()).setNetworkMode(true);
										level30.setData(s59, s62, s27, b56);
										client43.minecraft.loadLegacy(level30);
										client43.minecraft.hideScreen = false;
										client43.connected = true;
									} else if(packet6 == Packet.SET_TILE) {
										if(client43.minecraft.level != null) {
											client43.minecraft.level.netSetTile(((Short)object7[0]).shortValue(), ((Short)object7[1]).shortValue(), ((Short)object7[2]).shortValue(), ((Byte)object7[3]).byteValue());
										}
									} else {
										byte b10;
										byte b10001;
										short s10003;
										short s10004;
										String string33;
										NetworkPlayer networkPlayer34;
										short s36;
										short s45;
										if(packet6 == Packet.PLAYER_JOIN) {
											b10001 = ((Byte)object7[0]).byteValue();
											String string10002 = (String)object7[1];
											s10003 = ((Short)object7[2]).shortValue();
											s10004 = ((Short)object7[3]).shortValue();
											short s10005 = ((Short)object7[4]).shortValue();
											byte b10006 = ((Byte)object7[5]).byteValue();
											byte b57 = ((Byte)object7[6]).byteValue();
											b10 = b10006;
											short s9 = s10005;
											s45 = s10004;
											s36 = s10003;
											string33 = string10002;
											b5 = b10001;
											if(b5 >= 0) {
												b10 = (byte)(b10 + 128);
												s45 = (short)(s45 - 22);
												networkPlayer34 = new NetworkPlayer(client43.minecraft, b5, string33, s36, s45, s9, (float)(b10 * 360) / 256.0F, (float)(b57 * 360) / 256.0F);
												client43.players.put(b5, networkPlayer34);
												client43.minecraft.level.addEntity(networkPlayer34);
											} else {
												client43.minecraft.level.setSpawnPos(s36 / 32, s45 / 32, s9 / 32, (float)(b10 * 320 / 256));
												client43.minecraft.player.moveTo((float)s36 / 32.0F, (float)s45 / 32.0F, (float)s9 / 32.0F, (float)(b10 * 360) / 256.0F, (float)(b57 * 360) / 256.0F);
											}
										} else {
											byte b48;
											NetworkPlayer networkPlayer58;
											byte b69;
											if(packet6 == Packet.PLAYER_TELEPORT) {
												b10001 = ((Byte)object7[0]).byteValue();
												short s65 = ((Short)object7[1]).shortValue();
												s10003 = ((Short)object7[2]).shortValue();
												s10004 = ((Short)object7[3]).shortValue();
												b69 = ((Byte)object7[4]).byteValue();
												b10 = ((Byte)object7[5]).byteValue();
												b48 = b69;
												s45 = s10004;
												s36 = s10003;
												short s35 = s65;
												b5 = b10001;
												if(b5 < 0) {
													client43.minecraft.player.moveTo((float)s35 / 32.0F, (float)s36 / 32.0F, (float)s45 / 32.0F, (float)(b48 * 360) / 256.0F, (float)(b10 * 360) / 256.0F);
												} else {
													b48 = (byte)(b48 + 128);
													s36 = (short)(s36 - 22);
													if((networkPlayer58 = (NetworkPlayer)client43.players.get(b5)) != null) {
														networkPlayer58.teleport(s35, s36, s45, (float)(b48 * 360) / 256.0F, (float)(b10 * 360) / 256.0F);
													}
												}
											} else {
												byte b37;
												byte b40;
												byte b50;
												byte b66;
												byte b67;
												if(packet6 == Packet.PLAYER_MOVE_AND_ROTATE) {
													b10001 = ((Byte)object7[0]).byteValue();
													b66 = ((Byte)object7[1]).byteValue();
													b67 = ((Byte)object7[2]).byteValue();
													byte b68 = ((Byte)object7[3]).byteValue();
													b69 = ((Byte)object7[4]).byteValue();
													b10 = ((Byte)object7[5]).byteValue();
													b48 = b69;
													b50 = b68;
													b40 = b67;
													b37 = b66;
													b5 = b10001;
													if(b5 >= 0) {
														b48 = (byte)(b48 + 128);
														if((networkPlayer58 = (NetworkPlayer)client43.players.get(b5)) != null) {
															networkPlayer58.queue(b37, b40, b50, (float)(b48 * 360) / 256.0F, (float)(b10 * 360) / 256.0F);
														}
													}
												} else if(packet6 == Packet.PLAYER_ROTATE) {
													b10001 = ((Byte)object7[0]).byteValue();
													b66 = ((Byte)object7[1]).byteValue();
													b40 = ((Byte)object7[2]).byteValue();
													b37 = b66;
													b5 = b10001;
													if(b5 >= 0) {
														b37 = (byte)(b37 + 128);
														NetworkPlayer networkPlayer51;
														if((networkPlayer51 = (NetworkPlayer)client43.players.get(b5)) != null) {
															networkPlayer51.queue((float)(b37 * 360) / 256.0F, (float)(b40 * 360) / 256.0F);
														}
													}
												} else if(packet6 == Packet.PLAYER_MOVE) {
													b10001 = ((Byte)object7[0]).byteValue();
													b66 = ((Byte)object7[1]).byteValue();
													b67 = ((Byte)object7[2]).byteValue();
													b50 = ((Byte)object7[3]).byteValue();
													b40 = b67;
													b37 = b66;
													b5 = b10001;
													NetworkPlayer networkPlayer52;
													if(b5 >= 0 && (networkPlayer52 = (NetworkPlayer)client43.players.get(b5)) != null) {
														networkPlayer52.queue(b37, b40, b50);
													}
												} else if(packet6 == Packet.PLAYER_DISCONNECT) {
													b5 = ((Byte)object7[0]).byteValue();
													if(b5 >= 0 && (networkPlayer34 = (NetworkPlayer)client43.players.remove(b5)) != null) {
														networkPlayer34.clear();
														client43.minecraft.level.removeEntity(networkPlayer34);
													}
												} else if(packet6 == Packet.CHAT_MESSAGE) {
													b10001 = ((Byte)object7[0]).byteValue();
													string33 = (String)object7[1];
													b5 = b10001;
													if(b5 < 0) {
														client43.minecraft.gui.addMessage("&e" + string33);
													} else {
														client43.players.get(b5);
														client43.minecraft.gui.addMessage(string33);
													}
												} else if(packet6 == Packet.KICK_PLAYER) {
													client43.serverConnection.disconnect();
													client43.minecraft.setScreen(new ErrorScreen("Connection lost", (String)object7[0]));
												} else if(packet6 == Packet.USER_TYPE) {
													client43.minecraft.player.userType = ((Byte)object7[0]).byteValue();
												}
											}
										}
									}
								}

								if(!socketConnection21.connected) {
									break;
								}

								socketConnection21.readBuffer.compact();
							}

							if(socketConnection21.writeBuffer.position() > 0) {
								socketConnection21.writeBuffer.flip();
								socketConnection21.socketChannel.write(socketConnection21.writeBuffer);
								socketConnection21.writeBuffer.compact();
							}
						} catch (Exception exception15) {
							client18.minecraft.setScreen(new ErrorScreen("Disconnected!", "You\'ve lost connection to the server"));
							client18.minecraft.hideScreen = false;
							exception15.printStackTrace();
							client18.serverConnection.disconnect();
							client18.minecraft.networkClient = null;
						}
					}
				}

				Player player31 = this.player;
				client18 = this.networkClient;
				if(this.networkClient.connected) {
					int i23 = (int)(player31.x * 32.0F);
					i4 = (int)(player31.y * 32.0F);
					i38 = (int)(player31.z * 32.0F);
					i46 = (int)(player31.yRot * 256.0F / 360.0F) & 255;
					i47 = (int)(player31.xRot * 256.0F / 360.0F) & 255;
					client18.serverConnection.sendPacket(Packet.PLAYER_TELEPORT, new Object[]{-1, i23, i4, i38, i46, i47});
				}
			}
		}

		if(this.screen == null && this.player != null && this.player.health <= 0) {
			this.setScreen((Screen)null);
		}
		
		ItemTile.mode = ItemTile.getItemMode(this.player.inventory.getSelected());
		ItemTile.itemPower = ItemTile.getItemPower(this.player.inventory.getSelected());
		
		//System.out.println(ItemTile.mode + " " + ItemTile.itemPower);

		if(this.screen == null || this.screen.allowUserInput) {
			int i20;
			while(Mouse.next()) {
				if((i20 = Mouse.getEventDWheel()) != 0) {
					this.player.inventory.swapPaint(i20);
				}

				if(this.screen == null) {
					if(!this.mouseGrabbed && Mouse.getEventButtonState()) {
						this.grabMouse();
					} else {
						if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
							this.clickMouse(0);
							this.oFrames = this.frames;
						}

						if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
							this.clickMouse(1);
							this.oFrames = this.frames;
						}

						if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.hitResult != null) {
							if((i19 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z)) == Tile.grass.id) {
								i19 = Tile.dirt.id;
							}

							if(i19 == Tile.slabFull.id) {
								i19 = Tile.slabHalf.id;
							}

							if(i19 == Tile.unbreakable.id) {
								i19 = Tile.rock.id;
							}

							this.player.inventory.grabTexture(i19, this.gamemode instanceof CreativeGameMode);
						}
					}
				}

				if(this.screen != null) {
					this.screen.mouseEvent();
				}
			}

			if(this.clickCounter > 0) {
				--this.clickCounter;
			}

			label380:
			while(true) {
				do {
					do {
						if(!Keyboard.next()) {
							if(this.screen == null) {
								if(Mouse.isButtonDown(0) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
									this.clickMouse(0);
									this.oFrames = this.frames;
								}

								if(Mouse.isButtonDown(1) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
									this.clickMouse(1);
									this.oFrames = this.frames;
								}
							}

							boolean z25 = this.screen == null && Mouse.isButtonDown(0) && this.mouseGrabbed;
							boolean z39 = false;
							if(!this.gamemode.mode && this.clickCounter <= 0) {
								if(z25 && this.hitResult != null && this.hitResult.type == 0) {
									i4 = this.hitResult.x;
									i38 = this.hitResult.y;
									i46 = this.hitResult.z;
									this.gamemode.continueDestroyBlock(i4, i38, i46, this.hitResult.f);
								} else {
									this.gamemode.stopDestroyBlock();
								}
							}
							break label380;
						}

						this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
					} while(!Keyboard.getEventKeyState());

					if(this.screen != null) {
						this.screen.keyboardEvent();
					}

					if(this.screen == null) {
						if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
							this.pauseScreen();
						}

						if(this.gamemode instanceof CreativeGameMode) {
							if(Keyboard.getEventKey() == this.options.load.key) {
								this.player.resetPos();
							}

							if(Keyboard.getEventKey() == this.options.save.key) {
								this.level.setSpawnPos((int)this.player.x, (int)this.player.y, (int)this.player.z, this.player.yRot);
								this.player.resetPos();
							}
						}

						Keyboard.getEventKey();
						if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
							this.raining = !this.raining;
						}
						
						if(Keyboard.getEventKey() == Keyboard.KEY_Q) {
							int count = this.player.inventory.count[this.player.inventory.selected];
							count--;
							
							if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) )
								count = 0;
							
							this.player.inventory.count[this.player.inventory.selected] = count;
							if (count == 0)this.player.inventory.slots[this.player.inventory.selected] = -1;
						}

						if(Keyboard.getEventKey() == this.options.build.key) {
							this.gamemode.handleOpenInventory();
						}
						
						if (Keyboard.getEventKey() == Keyboard.KEY_C) {
							this.setScreen(new CraftMenu());
						}

						if(Keyboard.getEventKey() == this.options.chat.key && this.networkClient != null && this.networkClient.isConnected()) {
							this.player.releaseAllKeys();
							this.setScreen(new ChatScreen());
						}
					}

					for(i20 = 0; i20 < 9; ++i20) {
						if(Keyboard.getEventKey() == i20 + Keyboard.KEY_1) {
							this.player.inventory.selected = i20;
						}
					}
					
					
					
				} while(Keyboard.getEventKey() != this.options.toggleFog.key);

				this.options.setOption(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
			}
		}

		if(this.screen != null) {
			this.oFrames = this.frames + 10000;
		}

		if(this.screen != null) {
			this.screen.updateEvents();
			if(this.screen != null) {
				this.screen.tick();
			}
		}

		if(this.level != null) {
			GameRenderer gameRenderer24 = this.gameRenderer;
			++this.gameRenderer.rainTicks;
			TileRenderer tileRenderer41 = gameRenderer24.tileRenderer;
			gameRenderer24.tileRenderer.oProgress = tileRenderer41.progress;
			if(tileRenderer41.move) {
				++tileRenderer41.rot;
				if(tileRenderer41.rot == 7) {
					tileRenderer41.rot = 0;
					tileRenderer41.move = false;
				}
			}

			Player player28 = tileRenderer41.minecraft.player;
			i4 = tileRenderer41.minecraft.player.inventory.getSelected();
			Tile tile42 = null;
			if(i4 > 0) {
				tile42 = Tile.tiles[i4];
			}

			float f49 = 0.4F;
			float f53;
			if((f53 = (tile42 == tileRenderer41.tile ? 1.0F : 0.0F) - tileRenderer41.progress) < -f49) {
				f53 = -f49;
			}

			if(f53 > f49) {
				f53 = f49;
			}

			tileRenderer41.progress += f53;
			if(tileRenderer41.progress < 0.1F) {
				tileRenderer41.tile = tile42;
			}

			if(gameRenderer24.minecraft.raining) {
				GameRenderer gameRenderer44 = gameRenderer24;
				player28 = gameRenderer24.minecraft.player;
				Level level32 = gameRenderer24.minecraft.level;
				i38 = (int)player28.x;
				i46 = (int)player28.y;
				i47 = (int)player28.z;

				for(i8 = 0; i8 < 50; ++i8) {
					int i54 = i38 + gameRenderer44.random.nextInt(9) - 4;
					int i55 = i47 + gameRenderer44.random.nextInt(9) - 4;
					int i60;
					if((i60 = level32.getHighestTile(i54, i55)) <= i46 + 4 && i60 >= i46 - 4) {
						float f61 = gameRenderer44.random.nextFloat();
						float f63 = gameRenderer44.random.nextFloat();
						gameRenderer44.minecraft.particleEngine.addParticle(new WaterDropParticle(level32, (float)i54 + f61, (float)i60 + 0.1F, (float)i55 + f63));
					}
				}
			}

			LevelRenderer levelRenderer26 = this.levelRenderer;
			++this.levelRenderer.cloudTickCounter;
			this.level.tickEntities();
			if(!this.isOnlineClient()) {
				this.level.tick();
			}

			this.particleEngine.tick();
		}

	}

	public final boolean isOnlineClient() {
		return this.networkClient != null;
	}

	public final void generateLevel(int size) {
		String string2 = this.user != null ? this.user.name : "anonymous";
		Level size1 = (new LevelGen(this.loadingScreen)).generateLevel(string2, 128 << size, 128 << size, 64);
		this.gamemode.createPlayer(size1);
		this.loadLegacy(size1);
	}

	public final boolean loadLevel(String name, int id) {
		Level name1;
		if((name1 = this.levelIo.load(this.host, name, id)) == null) {
			return false;
		} else {
			this.loadLegacy(name1);
			return true;
		}
	}

	public final void loadLegacy(Level level) {
		if(this.applet == null || !this.applet.getDocumentBase().getHost().equalsIgnoreCase("minecraft.net") && !this.applet.getDocumentBase().getHost().equalsIgnoreCase("www.minecraft.net") || !this.applet.getCodeBase().getHost().equalsIgnoreCase("minecraft.net") && !this.applet.getCodeBase().getHost().equalsIgnoreCase("www.minecraft.net")) {
			level = null;
		}

		this.level = level;
		if(level != null) {
			level.initTransient();
			this.gamemode.initLevel(level);
			level.font = this.font;
			level.rendererContext = this;
			if(!this.isOnlineClient()) {
				this.player = (Player)level.findSubclassOf(Player.class);
			} else if(this.player != null) {
				this.player.resetPos();
				this.gamemode.initPlayer(this.player);
				if(level != null) {
					level.player = this.player;
					level.addEntity(this.player);
				}
			}
		}

		if(this.player == null) {
			this.player = new Player(level);
			this.player.resetPos();
			this.gamemode.initPlayer(this.player);
			if(level != null) {
				level.player = this.player;
			}
		}

		if(this.player != null) {
			this.player.input = new KeyboardInput(this.options);
			this.gamemode.adjustPlayer(this.player);
		}

		if(this.levelRenderer != null) {
			LevelRenderer levelRenderer2 = this.levelRenderer;
			if(this.levelRenderer.level != null) {
				levelRenderer2.level.removeListener(levelRenderer2);
			}

			levelRenderer2.level = level;
			if(level != null) {
				level.addListener(levelRenderer2);
				levelRenderer2.compileSurroundingGround();
			}
		}

		if(this.particleEngine != null) {
			ParticleEngine particleEngine5 = this.particleEngine;
			if(level != null) {
				level.particleEngine = particleEngine5;
			}

			for(int i4 = 0; i4 < 2; ++i4) {
				particleEngine5.particles[i4].clear();
			}
		}

		System.gc();
	}
}