package com.sam.yh.service.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sam.yh.req.bean.BatteryInfoReq;
import com.sam.yh.service.BatteryService;

@Service
@Sharable
public class SamBtyDataHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(SamBtyDataHandler.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
    
    // private static ConcurrentHashMap<String, Channel> channelMap = new
    // ConcurrentHashMap<String, Channel>();

    @Autowired
    BatteryService batteryService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        channels.add(ctx.channel());
        ctx.write("Welcome, It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

   
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String response;
        boolean close = false;
        logger.info("TEST REQUEST:" + request);
        if (request.isEmpty()) {
            response = "Please type something.\r\n";
        } else if ("bye".equals(request.toLowerCase())) {
            response = "Have a good day!\n";
            close = true;
        } else if ("heartbeat".equals(request.toLowerCase())) {
            response = "got it\n";
        } else {
            BatteryInfoReq infoReq = BtyDataConverter.convert(request);

            if (infoReq != null && infoReq.getImei() != null) {
                ctx.attr(AttributeKey.valueOf("IMEI")).set(infoReq.getImei());
                channelMap.put(infoReq.getImei(), ctx.channel());
            }

            /*for (Channel c : channels) {
                if (c == ctx.channel()) {
                    c.attr(AttributeKey.valueOf("IMEI")).set(infoReq.getImei());
                }
            }*/
            batteryService.uploadBatteryInfo(infoReq);
            response = "got it\n";
        }

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the
        // conversion.
        ChannelFuture future = ctx.write(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
    
    
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    
 

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(ExceptionUtils.getStackTrace(cause));
        if(channels.contains(ctx.channel())){
        	channels.remove(ctx.channel());
        }
        ctx.close();
    }

    /*
     * public static ConcurrentHashMap<String, Channel> getChannelMap() { return
     * channelMap; }
     */

    public static ChannelGroup getChannels() {
        return channels;
    }

}
