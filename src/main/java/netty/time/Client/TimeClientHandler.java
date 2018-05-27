package netty.time.Client;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        try {
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
        	// 接收服务器传来的指令打印出来 
        	byte[] request = new byte[m.readableBytes()];
        	m.readBytes(request);
        	//System.out.println(printHexString(request));
            // 反馈信息给服务器
        	byte[] response = new byte[8];
        	response[0] = (byte) 0xCC;
        	response[1] = (byte) 0x33;
        	response[2] = (byte) 0xFF;
        	response[3] = (byte) 0xFF;
        	response[4] = (byte) 0x00;
        	response[5] = (byte) 0x00;
        	response[6] = (byte) 0xC3;
        	response[7] = (byte) 0x3C;
        	ByteBuf bf = ctx.alloc().buffer(8);
        	bf.writeBytes(response);
        	ctx.writeAndFlush(bf);        	
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    //将指定byte数组以16进制的形式打印到控制台 
    public String printHexString( byte[] b) { 
    	StringBuffer bf = new StringBuffer();
    	for (int i = 0; i < b.length; i++) { 
    		String hex = Integer.toHexString(b[i] & 0xFF); 
    		if (hex.length() == 1) { 
    			hex = '0' + hex; 
    		} 
    		bf.append(hex.toUpperCase() + " "); 
    	} 
    	return bf.toString();
    }
}
