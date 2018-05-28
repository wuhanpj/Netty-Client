package netty.time.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
	
	public void startChannel() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new TimeClientHandler());
				}
            	
            });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8080).sync(); // (5)
            
            f.channel().writeAndFlush(Unpooled.copiedBuffer("ht1234567890".getBytes()));

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            
        } catch(InterruptedException e) {
        	
        } finally {
            workerGroup.shutdownGracefully();
        }
	}
	public static void main(String[] args) {
		// 打开com接口
		ContinueRead cRead = new ContinueRead();
        int i = cRead.startComPort();
        if(i == 1) {
        	// 连接服务端
        	TimeClient tc = new TimeClient();
        	tc.startChannel();
        } else {
        	return;
        }
        
    }
}
