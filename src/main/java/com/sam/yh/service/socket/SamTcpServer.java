package com.sam.yh.service.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SamTcpServer {

    private static final Logger logger = LoggerFactory.getLogger(SamTcpServer.class);

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    
    
  //test 
    /**用于分配处理业务线程的线程组个数 */  
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2; //默认  
    /** 业务出现线程大小*/  
    protected static final int BIZTHREADSIZE = 8;  
    

    private ChannelFuture channelFuture;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    @Resource
    SamBtyDataHandler SamBtyDataHandler;
    @Resource
    Integer samPort;

    @PostConstruct
    public void start() throws Exception {
        logger.info("Starting tcp server ");
        
//        bossGroup = new NioEventLoopGroup();
//        workerGroup = new NioEventLoopGroup();
        
        //test
        bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
        workerGroup = new NioEventLoopGroup(BIZGROUPSIZE);
        
        
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));

        // bootstrap.option(option, value);
        // bootstrap.option(ChannelOption.SO_BACKLOG, 3000);
        // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 保持长连接状态
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .childOption(ChannelOption.SO_SNDBUF, 64*1024)
        .childOption(ChannelOption.SO_RCVBUF, 64*1024);

       
              
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline(); // todo: add handler

                pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                // the encoder and decoder are static as these are sharable
                pipeline.addLast(DECODER);
                pipeline.addLast(ENCODER);
                     
                // and then business logic.
                pipeline.addLast(SamBtyDataHandler);   
                
            }
        });
        channelFuture = bootstrap.bind(samPort).sync();
    }

    @PreDestroy
    public void stop() throws Exception {
        logger.info("stop sam tcp server");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }

    /*
     * public static void main(String[] args) throws Exception { SamTcpServer
     * server = new SamTcpServer(); server.start(); }
     */

}
