package nia.chapter2.handlerdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author aguicode
 * @since 2020-3-8
 */
public class EchoOutHandler2 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
            throws Exception {
        System.out.println("out2");
        // 执行下一个OutboundHandler
        System.out.println("at out2 msg: " + msg);
        //msg = "hi newed in out2";
        // 通知执行下一个OutboundHandler
        ctx.writeAndFlush(msg);
        //super.write(ctx, msg, promise);
        //super.flush(ctx);
        System.out.println("out2 done");
    }
}