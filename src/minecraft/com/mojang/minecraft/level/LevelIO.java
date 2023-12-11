package com.mojang.minecraft.level;

import com.mojang.minecraft.LevelLoaderListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class LevelIO {
	private LevelLoaderListener levelLoaderListener;

	public LevelIO(LevelLoaderListener levelLoaderListener) {
		this.levelLoaderListener = levelLoaderListener;
	}

	public final boolean save(Level level, File levelFile) {
		try {
			FileOutputStream levelFile1 = new FileOutputStream(levelFile);
			save(level, (OutputStream)levelFile1);
			levelFile1.close();
			return true;
		} catch (Exception exception4) {
			exception4.printStackTrace();
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Failed!");
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException3) {
			}

			return false;
		}
	}

	public final Level load(File levelFile) {
		try {
			FileInputStream levelFile1 = new FileInputStream(levelFile);
			Level level2 = this.load((InputStream)levelFile1);
			levelFile1.close();
			return level2;
		} catch (Exception exception4) {
			exception4.printStackTrace();
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Failed!");
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException3) {
			}

			return null;
		}
	}

	public final boolean save(Level level, String host, String userName, String sessionid, String name, int id) {
		if(sessionid == null) {
			sessionid = "";
		}

		if(this.levelLoaderListener != null && this.levelLoaderListener != null) {
			this.levelLoaderListener.beginLevelLoading("Saving level");
		}

		try {
			if(this.levelLoaderListener != null && this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Compressing..");
			}

			ByteArrayOutputStream byteArrayOutputStream7 = new ByteArrayOutputStream();
			save(level, (OutputStream)byteArrayOutputStream7);
			byteArrayOutputStream7.close();
			byte[] level1 = byteArrayOutputStream7.toByteArray();
			if(this.levelLoaderListener != null && this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Connecting..");
			}

			HttpURLConnection host1;
			(host1 = (HttpURLConnection)(new URL("http://" + host + "/level/save.html")).openConnection()).setDoInput(true);
			host1.setDoOutput(true);
			host1.setRequestMethod("POST");
			DataOutputStream dataOutputStream13;
			(dataOutputStream13 = new DataOutputStream(host1.getOutputStream())).writeUTF(userName);
			dataOutputStream13.writeUTF(sessionid);
			dataOutputStream13.writeUTF(name);
			dataOutputStream13.writeByte(id);
			dataOutputStream13.writeInt(level1.length);
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Saving..");
			}

			dataOutputStream13.write(level1);
			dataOutputStream13.close();
			BufferedReader level2;
			if(!(level2 = new BufferedReader(new InputStreamReader(host1.getInputStream()))).readLine().equalsIgnoreCase("ok")) {
				if(this.levelLoaderListener != null) {
					this.levelLoaderListener.levelLoadUpdate("Failed: " + level2.readLine());
				}

				level2.close();
				Thread.sleep(1000L);
				return false;
			} else {
				level2.close();
				return true;
			}
		} catch (Exception exception9) {
			exception9.printStackTrace();
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Failed!");
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException8) {
			}

			return false;
		}
	}

	public final Level load(String host, String owner, int id) {
		if(this.levelLoaderListener != null) {
			this.levelLoaderListener.beginLevelLoading("Loading level");
		}

		try {
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Connecting..");
			}

			HttpURLConnection host1;
			(host1 = (HttpURLConnection)(new URL("http://" + host + "/level/load.html?id=" + id + "&user=" + owner)).openConnection()).setDoInput(true);
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Loading..");
			}

			DataInputStream host2;
			if((host2 = new DataInputStream(host1.getInputStream())).readUTF().equalsIgnoreCase("ok")) {
				return this.load((InputStream)host2);
			} else {
				if(this.levelLoaderListener != null) {
					this.levelLoaderListener.levelLoadUpdate("Failed: " + host2.readUTF());
				}

				host2.close();
				Thread.sleep(1000L);
				return null;
			}
		} catch (Exception exception5) {
			exception5.printStackTrace();
			if(this.levelLoaderListener != null) {
				this.levelLoaderListener.levelLoadUpdate("Failed!");
			}

			try {
				Thread.sleep(3000L);
			} catch (InterruptedException interruptedException4) {
			}

			return null;
		}
	}

	public final Level load(InputStream in) {
		if(this.levelLoaderListener != null) {
			this.levelLoaderListener.beginLevelLoading("Loading level");
		}

		if(this.levelLoaderListener != null) {
			this.levelLoaderListener.levelLoadUpdate("Reading..");
		}

		try {
			DataInputStream dataInputStream10;
			if((dataInputStream10 = new DataInputStream(new GZIPInputStream(in))).readInt() != 656127880) {
				return null;
			} else {
				byte in1;
				if((in1 = dataInputStream10.readByte()) > 2) {
					return null;
				} else if(in1 <= 1) {
					String in3 = dataInputStream10.readUTF();
					String string15 = dataInputStream10.readUTF();
					long j7 = dataInputStream10.readLong();
					short s3 = dataInputStream10.readShort();
					short s4 = dataInputStream10.readShort();
					short s5 = dataInputStream10.readShort();
					byte[] b6 = new byte[s3 * s4 * s5];
					dataInputStream10.readFully(b6);
					dataInputStream10.close();
					Level level11;
					(level11 = new Level()).setData(s3, s5, s4, b6);
					level11.name = in3;
					level11.creator = string15;
					level11.createTime = j7;
					return level11;
				} else {
					Level level2;
					LevelInputStream in2;
					(level2 = (Level)(in2 = new LevelInputStream(dataInputStream10)).readObject()).initTransient();
					in2.close();
					return level2;
				}
			}
		} catch (Exception exception9) {
			exception9.printStackTrace();
			(new StringBuilder()).append("Failed to load level: ").append(exception9.toString()).toString();
			return null;
		}
	}

	public static void save(Level level, OutputStream out) {
		try {
			DataOutputStream out1;
			(out1 = new DataOutputStream(new GZIPOutputStream(out))).writeInt(656127880);
			out1.writeByte(2);
			ObjectOutputStream out2;
			(out2 = new ObjectOutputStream(out1)).writeObject(level);
			out2.close();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}

	public static byte[] loadBlocks(InputStream in) {
		try {
			DataInputStream in1;
			byte[] b1 = new byte[(in1 = new DataInputStream(new GZIPInputStream(in))).readInt()];
			in1.readFully(b1);
			in1.close();
			return b1;
		} catch (Exception exception2) {
			throw new RuntimeException(exception2);
		}
	}
}