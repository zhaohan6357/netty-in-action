package nia.chapter2.handlerdemo;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author aguicode
 * @since 2020-3-8
 */
public class EchoOutHandler1 extends ChannelOutboundHandlerAdapter {

    @Override
    // 向client发送消息
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
            throws Exception {
        System.out.println("out1");
        Thread.sleep(10000);
        System.out.println("at out1 msg: " + msg);
        String response = "\nI am ok!\n";
        ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
        encoded.writeBytes(response.getBytes());
        String currentTime = new Date(System.currentTimeMillis()).toString();
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
        ctx.writeAndFlush(encoded);
        ctx.flush();
        System.out.println("out1 done");
    }
}