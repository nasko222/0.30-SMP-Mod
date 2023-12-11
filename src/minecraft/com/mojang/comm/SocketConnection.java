package com.mojang.comm;

import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public final class SocketConnection {
	public volatile boolean connected;
	public SocketChannel socketChannel = SocketChannel.open();
	public ByteBuffer readBuffer = ByteBuffer.allocate(1048576);
	public ByteBuffer writeBuffer = ByteBuffer.allocate(1048576);
	public Client client;
	private Socket socket;
	private boolean initialized = false;
	private byte[] stringPacket = new byte[64];

	public SocketConnection(String ip, int port) throws IOException {
		this.socketChannel.connect(new InetSocketAddress(ip, port));
		this.socketChannel.configureBlocking(false);
		System.currentTimeMillis();
		this.socket = this.socketChannel.socket();
		this.connected = true;
		this.readBuffer.clear();
		this.writeBuffer.clear();
		this.socket.setTcpNoDelay(true);
		this.socket.setTrafficClass(24);
		this.socket.setKeepAlive(false);
		this.socket.setReuseAddress(false);
		this.socket.setSoTimeout(100);
		this.socket.getInetAddress().toString();
	}

	public final void disconnect() {
		try {
			if(this.writeBuffer.position() > 0) {
				this.writeBuffer.flip();
				this.socketChannel.write(this.writeBuffer);
				this.writeBuffer.compact();
			}
		} catch (Exception exception2) {
		}

		this.connected = false;

		try {
			this.socketChannel.close();
		} catch (Exception exception1) {
		}

		this.socket = null;
		this.socketChannel = null;
	}

	public final void sendPacket(Packet packet, Object... data) {
		if(this.connected) {
			this.writeBuffer.put(packet.id);

			for(int i3 = 0; i3 < data.length; ++i3) {
				Class class10001 = packet.fields[i3];
				Object object6 = data[i3];
				Class class5 = class10001;
				SocketConnection socketConnection4 = this;
				if(this.connected) {
					try {
						if(class5 == Long.TYPE) {
							socketConnection4.writeBuffer.putLong(((Long)object6).longValue());
						} else if(class5 == Integer.TYPE) {
							socketConnection4.writeBuffer.putInt(((Number)object6).intValue());
						} else if(class5 == Short.TYPE) {
							socketConnection4.writeBuffer.putShort(((Number)object6).shortValue());
						} else if(class5 == Byte.TYPE) {
							socketConnection4.writeBuffer.put(((Number)object6).byteValue());
						} else if(class5 == Double.TYPE) {
							socketConnection4.writeBuffer.putDouble(((Double)object6).doubleValue());
						} else if(class5 == Float.TYPE) {
							socketConnection4.writeBuffer.putFloat(((Float)object6).floatValue());
						} else {
							byte[] b8;
							if(class5 != String.class) {
								if(class5 == byte[].class) {
									if((b8 = (byte[])((byte[])object6)).length < 1024) {
										b8 = Arrays.copyOf(b8, 1024);
									}

									socketConnection4.writeBuffer.put(b8);
								}
							} else {
								b8 = ((String)object6).getBytes("UTF-8");
								Arrays.fill(socketConnection4.stringPacket, (byte)32);

								int i9;
								for(i9 = 0; i9 < 64 && i9 < b8.length; ++i9) {
									socketConnection4.stringPacket[i9] = b8[i9];
								}

								for(i9 = b8.length; i9 < 64; ++i9) {
									socketConnection4.stringPacket[i9] = 32;
								}

								socketConnection4.writeBuffer.put(socketConnection4.stringPacket);
							}
						}
					} catch (Exception exception7) {
						this.client.handleException(exception7);
					}
				}
			}

		}
	}

	public Object read(Class type) {
		if(!this.connected) {
			return null;
		} else {
			try {
				if(type == Long.TYPE) {
					return this.readBuffer.getLong();
				} else if(type == Integer.TYPE) {
					return this.readBuffer.getInt();
				} else if(type == Short.TYPE) {
					return this.readBuffer.getShort();
				} else if(type == Byte.TYPE) {
					return this.readBuffer.get();
				} else if(type == Double.TYPE) {
					return this.readBuffer.getDouble();
				} else if(type == Float.TYPE) {
					return this.readBuffer.getFloat();
				} else if(type == String.class) {
					this.readBuffer.get(this.stringPacket);
					return (new String(this.stringPacket, "UTF-8")).trim();
				} else if(type == byte[].class) {
					byte[] type1 = new byte[1024];
					this.readBuffer.get(type1);
					return type1;
				} else {
					return null;
				}
			} catch (Exception exception2) {
				this.client.handleException(exception2);
				return null;
			}
		}
	}
}