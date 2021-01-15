package me.onlyeli.ice.utils;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;
import net.minecraft.client.Minecraft;

public class PingUtils
{
    public static class ServerListPing
    {
        private String host1;
        private int port;
        private int timeout = 100;
        private static final Gson gson = new Gson();

        public void setHost1(String host1)
        {
            this.host1 = host1;
        }

        public void setPort(int port)
        {
            this.port = port;
        }

        public void setTimeout(int timeout)
        {
            this.timeout = timeout;
        }

        public PingUtils.StatusResponse fetchData() throws Exception
        {
            Socket socket = null;
            OutputStream outputstream = null;
            InputStream inputstream = null;
            PingUtils.StatusResponse pingutils$statusresponse = null;
            PingUtils.StatusResponse dIn;

            try
            {
                socket = new Socket(this.host1, this.port);
                socket.setSoTimeout(this.timeout);
                outputstream = socket.getOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
                inputstream = socket.getInputStream();
                DataInputStream datainputstream = new DataInputStream(inputstream);
                long i = Minecraft.getSystemTime();
                this.sendPacket(dataoutputstream, this.prepareHandshake());
                this.sendPacket(dataoutputstream, this.preparePing());
                pingutils$statusresponse = this.receiveResponse(datainputstream);
                pingutils$statusresponse.ms = (int)(Minecraft.getSystemTime() - i);
                datainputstream.close();
                dataoutputstream.close();
                PingUtils.StatusResponse pingutils$statusresponse2 = pingutils$statusresponse;
                return pingutils$statusresponse2;
            }
            catch (Exception var13)
            {
                dIn = pingutils$statusresponse;
            }
            finally
            {
                if (outputstream != null)
                {
                    outputstream.close();
                }

                if (inputstream != null)
                {
                    inputstream.close();
                }

                if (socket != null)
                {
                    socket.close();
                }
            }

            return dIn;
        }

        private PingUtils.StatusResponse receiveResponse(DataInputStream dIn) throws IOException
        {
            this.readVarInt(dIn);
            int i = this.readVarInt(dIn);

            if (i != 0)
            {
                throw new IOException("Invalid packetId");
            }
            else
            {
                int j = this.readVarInt(dIn);

                if (j < 1)
                {
                    throw new IOException("Invalid string length.");
                }
                else
                {
                    byte[] abyte = new byte[j];
                    dIn.readFully(abyte);
                    String s = new String(abyte, Charset.forName("utf-8"));

                    if (s.contains("\"text\":"))
                    {
                        s = s.replace("\"description\"", "\"descriptions\"");
                    }

                    PingUtils.StatusResponse pingutils$statusresponse = (PingUtils.StatusResponse)gson.fromJson(s, PingUtils.StatusResponse.class);
                    return pingutils$statusresponse;
                }
            }
        }

        private void sendPacket(DataOutputStream out, byte[] data) throws IOException
        {
            this.writeVarInt(out, data.length);
            out.write(data);
        }

        private byte[] preparePing() throws IOException
        {
            return new byte[] {(byte)0};
        }

        private byte[] prepareHandshake() throws IOException
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
            bytearrayoutputstream.write(0);
            this.writeVarInt(dataoutputstream, 4);
            this.writeString(dataoutputstream, this.host1);
            dataoutputstream.writeShort(this.port);
            this.writeVarInt(dataoutputstream, 1);
            return bytearrayoutputstream.toByteArray();
        }

        public void writeString(DataOutputStream out, String string) throws IOException
        {
            this.writeVarInt(out, string.length());
            out.write(string.getBytes(Charset.forName("utf-8")));
        }

        public int readVarInt(DataInputStream in) throws IOException
        {
            int i = 0;
            int j = 0;

            while (true)
            {
                int k = in.readByte();
                i |= (k & 127) << j++ * 7;

                if (j > 5)
                {
                    throw new RuntimeException("VarInt too big");
                }

                if ((k & 128) != 128)
                {
                    break;
                }
            }

            return i;
        }

        public void writeVarInt(DataOutputStream out, int paramInt) throws IOException
        {
            while ((paramInt & -128) != 0)
            {
                out.write(paramInt & 127 | 128);
                paramInt >>>= 7;
            }

            out.write(paramInt);
        }
    }

    public static class StatusResponse
    {
        private String description;
        private PingUtils.StatusResponse.Descriptions descriptions;
        private PingUtils.StatusResponse.Players players;
        private PingUtils.StatusResponse.Version version;
        private String favicon;
        private int time;
        private int ms;

        public int hashCode()
        {
            int i = 1;
            Object object = this.getDescription();
            i = i * 59 + (object == null ? 0 : object.hashCode());
            Object object1 = this.getPlayers();
            i = i * 59 + (object1 == null ? 0 : object1.hashCode());
            Object object2 = this.getVersion();
            i = i * 59 + (object2 == null ? 0 : object2.hashCode());
            Object object3 = this.getFavicon();
            i = i * 59 + (object3 == null ? 0 : object3.hashCode());
            i = i * 59 + this.getTime();
            return i;
        }

        public boolean canEqual(Object other)
        {
            return other instanceof PingUtils.StatusResponse;
        }

        public boolean equals(Object o)
        {
            if (o == this)
            {
                return true;
            }
            else if (!(o instanceof PingUtils.StatusResponse))
            {
                return false;
            }
            else
            {
                PingUtils.StatusResponse pingutils$statusresponse = (PingUtils.StatusResponse)o;

                if (!pingutils$statusresponse.canEqual(this))
                {
                    return false;
                }
                else
                {
                    Object object = this.getDescription();
                    Object object1 = pingutils$statusresponse.getDescription();

                    if (object == null)
                    {
                        if (object1 != null)
                        {
                            return false;
                        }
                    }
                    else if (!object.equals(object1))
                    {
                        return false;
                    }

                    Object object2 = this.getPlayers();
                    Object object3 = pingutils$statusresponse.getPlayers();

                    if (object2 == null)
                    {
                        if (object3 != null)
                        {
                            return false;
                        }
                    }
                    else if (!object2.equals(object3))
                    {
                        return false;
                    }

                    Object object4 = this.getVersion();
                    Object object5 = pingutils$statusresponse.getVersion();

                    if (object4 == null)
                    {
                        if (object5 != null)
                        {
                            return false;
                        }
                    }
                    else if (!object4.equals(object5))
                    {
                        return false;
                    }

                    Object object6 = this.getFavicon();
                    Object object7 = pingutils$statusresponse.getFavicon();

                    if (object6 == null)
                    {
                        if (object7 != null)
                        {
                            return false;
                        }
                    }
                    else if (!object6.equals(object7))
                    {
                        return false;
                    }

                    return this.getTime() == pingutils$statusresponse.getTime();
                }
            }
        }

        public String toString()
        {
            return "StatusResponse(description=" + this.getDescription() + ", players=" + this.getPlayers() + ", version=" + this.getVersion() + ", favicon=" + this.getFavicon() + ", time=" + this.getTime() + ")";
        }

        public String getDescription()
        {
            return this.description != null && !this.description.isEmpty() ? this.description : this.descriptions.getText();
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public PingUtils.StatusResponse.Players getPlayers()
        {
            return this.players;
        }

        public int getMs()
        {
            return this.ms;
        }

        public void setPlayers(PingUtils.StatusResponse.Players players)
        {
            this.players = players;
        }

        public PingUtils.StatusResponse.Version getVersion()
        {
            return this.version;
        }

        public void setVersion(PingUtils.StatusResponse.Version version)
        {
            this.version = version;
        }

        public String getFavicon()
        {
            return this.favicon;
        }

        public void setFavicon(String favicon)
        {
            this.favicon = favicon;
        }

        public void setTime(int time)
        {
            this.time = time;
        }

        public int getTime()
        {
            return this.time;
        }

        public class Descriptions
        {
            private String text;

            public String getText()
            {
                return this.text;
            }

            public void setText(String text)
            {
                this.text = text;
            }
        }

        public class Player
        {
            private String name;
            private String id;

            public boolean equals(Object o)
            {
                if (o == this)
                {
                    return true;
                }
                else if (!(o instanceof PingUtils.StatusResponse.Player))
                {
                    return false;
                }
                else
                {
                    PingUtils.StatusResponse.Player pingutils$statusresponse$player = (PingUtils.StatusResponse.Player)o;

                    if (!pingutils$statusresponse$player.canEqual(this))
                    {
                        return false;
                    }
                    else
                    {
                        Object object = this.getName();
                        Object object1 = pingutils$statusresponse$player.getName();

                        if (object == null)
                        {
                            if (object1 != null)
                            {
                                return false;
                            }
                        }
                        else if (!object.equals(object1))
                        {
                            return false;
                        }

                        Object object2 = this.getId();
                        Object object3 = pingutils$statusresponse$player.getId();
                        return object2 == null ? object3 == null : object2.equals(object3);
                    }
                }
            }

            public int hashCode()
            {
                int i = 1;
                Object object = this.getName();
                i = i * 59 + (object == null ? 0 : object.hashCode());
                Object object1 = this.getId();
                i = i * 59 + (object1 == null ? 0 : object1.hashCode());
                return i;
            }

            public boolean canEqual(Object other)
            {
                return other instanceof PingUtils.StatusResponse.Player;
            }

            public String toString()
            {
                return "StatusResponse.Player(name=" + this.getName() + ", id=" + this.getId() + ")";
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getName()
            {
                return this.name;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getId()
            {
                return this.id;
            }
        }

        public class Players
        {
            private int max;
            private int online;
            private List<PingUtils.StatusResponse.Player> sample;

            public boolean canEqual(Object other)
            {
                return other instanceof PingUtils.StatusResponse.Players;
            }

            public boolean equals(Object o)
            {
                if (o == this)
                {
                    return true;
                }
                else if (!(o instanceof PingUtils.StatusResponse.Players))
                {
                    return false;
                }
                else
                {
                    PingUtils.StatusResponse.Players pingutils$statusresponse$players = (PingUtils.StatusResponse.Players)o;

                    if (!pingutils$statusresponse$players.canEqual(this))
                    {
                        return false;
                    }
                    else if (this.getMax() != pingutils$statusresponse$players.getMax())
                    {
                        return false;
                    }
                    else if (this.getOnline() != pingutils$statusresponse$players.getOnline())
                    {
                        return false;
                    }
                    else
                    {
                        Object object = this.getSample();
                        Object object1 = pingutils$statusresponse$players.getSample();
                        return object == null ? object1 == null : object.equals(object1);
                    }
                }
            }

            public int hashCode()
            {
                int i = 1;
                i = i * 59 + this.getMax();
                i = i * 59 + this.getOnline();
                Object object = this.getSample();
                i = i * 59 + (object == null ? 0 : object.hashCode());
                return i;
            }

            public String toString()
            {
                return "StatusResponse.Players(max=" + this.getMax() + ", online=" + this.getOnline() + ", sample=" + this.getSample() + ")";
            }

            public void setMax(int max)
            {
                this.max = max;
            }

            public int getMax()
            {
                return this.max;
            }

            public void setOnline(int online)
            {
                this.online = online;
            }

            public int getOnline()
            {
                return this.online;
            }

            public List<PingUtils.StatusResponse.Player> getSample()
            {
                return this.sample;
            }

            public void setSample(List<PingUtils.StatusResponse.Player> sample)
            {
                this.sample = sample;
            }
        }

        public class Version
        {
            private String name;
            private String protocol;

            public boolean equals(Object o)
            {
                if (o == this)
                {
                    return true;
                }
                else if (!(o instanceof PingUtils.StatusResponse.Version))
                {
                    return false;
                }
                else
                {
                    PingUtils.StatusResponse.Version pingutils$statusresponse$version = (PingUtils.StatusResponse.Version)o;

                    if (!pingutils$statusresponse$version.canEqual(this))
                    {
                        return false;
                    }
                    else
                    {
                        Object object = this.getName();
                        Object object1 = pingutils$statusresponse$version.getName();

                        if (object == null)
                        {
                            if (object1 != null)
                            {
                                return false;
                            }
                        }
                        else if (!object.equals(object1))
                        {
                            return false;
                        }

                        Object object2 = this.getProtocol();
                        Object object3 = pingutils$statusresponse$version.getProtocol();
                        return object2 == null ? object3 == null : object2.equals(object3);
                    }
                }
            }

            public int hashCode()
            {
                int i = 1;
                Object object = this.getName();
                i = i * 59 + (object == null ? 0 : object.hashCode());
                Object object1 = this.getProtocol();
                i = i * 59 + (object1 == null ? 0 : object1.hashCode());
                return i;
            }

            public boolean canEqual(Object other)
            {
                return other instanceof PingUtils.StatusResponse.Version;
            }

            public String toString()
            {
                return "StatusResponse.Version(name=" + this.getName() + ", protocol=" + this.getProtocol() + ")";
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getName()
            {
                return this.name;
            }

            public void setProtocol(String protocol)
            {
                this.protocol = protocol;
            }

            public String getProtocol()
            {
                return this.protocol;
            }
        }
    }
}
