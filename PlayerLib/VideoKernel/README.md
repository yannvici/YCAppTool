# 视频播放器内核切换封装
#### 目录介绍
- 01.多视频内核适配背景
    - 1.1 播放器内核难以切换
    - 1.2 播放器内核和业务联动
    - 1.3 播放器内核如何选择
- 02.多视频内核设计目标
    - 2.1 建立一套视频API
    - 2.2 定义统一API要点
    - 2.3 无缝修改内核
- 03.播放器内核整体架构
    - 3.1 内核整体架构
    - 3.2 内核UML类图架构
    - 3.3 关于依赖关系
- 04.视频统一接口设计
    - 4.1 定义视频状态接口
    - 4.2 定义抽象视频播放器
    - 4.3 具体ijk实现案例
- 05.轻松创建不同内核播放器
    - 5.1 如何创建不同内核
    - 5.2 使用一点设计模式
    - 5.3 设计模式代码展示
- 06.其他一些设计说明
    - 6.1 稳定性设计说明
    - 6.2 测试case设计
    - 6.3 拓展性相关设计




### 00.视频播放器通用框架
- 基础封装视频播放器player，可以在ExoPlayer、MediaPlayer，声网RTC视频播放器内核，原生MediaPlayer可以自由切换
- 对于视图状态切换和后期维护拓展，避免功能和业务出现耦合。比如需要支持播放器UI高度定制，而不是该lib库中UI代码
- 针对视频播放，音频播放，播放回放，以及视频直播的功能。使用简单，代码拓展性强，封装性好，主要是和业务彻底解耦，暴露接口监听给开发者处理业务具体逻辑
- 该播放器整体架构：播放器内核(自由切换) +  视频播放器 + 边播边缓存 + 高度定制播放器UI视图层
- 项目地址：https://github.com/yangchong211/YCVideoPlayer
- 关于视频播放器整体功能介绍文档：https://juejin.im/post/6883457444752654343



### 01.多适配内核适配背景
#### 1.1 播放器内核难以切换
- 由于历史原因，公司多款app，使用视频播放器都不一样，有的用饺子播放器，有的用原生播放器，还有的用谷歌播放器。为了打造适用多条业务线的播放器，固需要封装一个自己的视频播放器接口。


#### 1.2 播放器内核和业务联动
- 和业务联动难抽离
    - 播放器，是一个比较核心的功能，之前有项目中，直接在Activity页面创建播放器，然后播放视频。其中还有很多视频的业务……然后产品说，添加一个列表视频，然后一顿痛改，改了后还要回归播放功能。
- 添加新业务改动大
    - 播放器内核与播放器解耦，还要跟具体的业务解耦: 支持更多的播放场景、以及新的播放业务快速接入，并且不影响其他播放业务，比如后期添加阿里云播放器内核，或者腾讯播放器内核。


#### 1.3 播放器内核如何选择
- 目前流行的视频框架
    - 1.Android原生VideoView
    - 2.Google 开源视频播放框架 ExoPlayer
    - 3.Vitamio 视频播放框架
    - 4.Bilibili 开源视频播放框架ijkplayer
- 这里选择的视频播放器内核
    - Google 开源视频播放框架 ExoPlayer
    - Bilibili 开源视频播放框架ijkplayer




### 02.多视频内核设计目标
#### 2.1 建立一套视频API
- 不同的视频播放器内核，由于api不一样，所以难以切换操作。要是想兼容内核切换，就必须自己制定一个视频接口+实现类的播放器


### 2.2 定义统一API要点
- 提问：针对不同内核播放器，比如谷歌的ExoPlayer，B站的IjkPlayer，还有原生的MediaPlayer，有些api不一样，那使用的时候如何统一api呢？
    - 比如说，ijk和exo的视频播放listener监听api就完全不同，这个时候需要做兼容处理
    - 定义接口，然后各个不同内核播放器实现接口，重写抽象方法。调用的时候，获取接口对象调用api，这样就可以统一Api
- 定义一个接口，这个接口有什么呢？这个接口定义通用视频播放器方法，比如常见的有：视频初始化，设置url，加载，以及播放状态，简单来说可以分为三个部分。
    - 第一部分：视频初始化实例对象方法，主要包括：initPlayer初始化视频，setDataSource设置视频播放器地址，setSurface设置视频播放器渲染view，prepareAsync开始准备播放操作
    - 第二部分：视频播放器状态方法，主要包括：播放，暂停，恢复，重制，设置进度，释放资源，获取进度，设置速度，设置音量
    - 第三部分：player绑定view后，需要监听播放状态，比如播放异常，播放完成，播放准备，播放size变化，还有播放准备


#### 2.2 无缝修改内核
- 传入不同类型方便创建不同内核
    - 隐藏内核播放器创建具体细节，开发者只需要关心所需产品对应的工厂，无须关心创建细节，甚至无须知道具体播放器类的类名。需要符合开闭原则



### 03.播放器内核整体架构
#### 3.1 内核整体架构
- 播放器内核架构图
    - ![image](https://img-blog.csdnimg.cn/2020101321464162.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)



#### 3.2 内核UML类图架构
- 内核UML类图架构如下所示
    - ![image](https://img-blog.csdnimg.cn/057b630ff4f4437b92ad71c52c70b575.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5p2o5YWF,size_20,color_FFFFFF,t_70,g_se,x_16)


#### 3.3 关于依赖关系
- 谷歌播放器，需要依赖一些谷歌的服务。ijk视频播放器，需要依赖ijk相关的库。如果你要拓展其他视频播放器，则需要添加依赖。需要注意，添加的库使用compileOnly。


### 04.视频统一接口设计
#### 4.1 定义视频状态接口
- 主要是视频播放过程中，监听视频的播放状态，比如说异常，播放完成，准备阶段，视频size变化等等。
    ``` java
    public interface VideoPlayerListener {
        /**
         * 异常
         * 1          表示错误的链接
         * 2          表示解析异常
         * 3          表示其他的异常
         * @param type                          错误类型
         */
        void onError(@PlayerConstant.ErrorType int type , String error);
    
        /**
         * 完成
         */
        void onCompletion();
    
        /**
         * 视频信息
         * @param what                          what
         * @param extra                         extra
         */
        void onInfo(int what, int extra);
    
        /**
         * 准备
         */
        void onPrepared();
    
        /**
         * 视频size变化监听
         * @param width                         宽
         * @param height                        高
         */
        void onVideoSizeChanged(int width, int height);
    }
    ```


#### 4.2 定义抽象视频播放器
- 这个是统一视频播放器接口，主要分成三个部分，第一部分：视频初始化实例对象方法；第二部分：视频播放器状态方法；第三部分：player绑定view后，需要监听播放状态
- 第一部分：视频初始化实例对象方法
    ``` java
    //视频播放器第一步：创建视频播放器
    public abstract void initPlayer();
    //设置播放地址
    public abstract void setDataSource(String path, Map<String, String> headers);
    //用于播放raw和asset里面的视频文件
    public abstract void setDataSource(AssetFileDescriptor fd);
    //设置渲染视频的View,主要用于TextureView
    public abstract void setSurface(Surface surface);
    //准备开始播放（异步）
    public abstract void prepareAsync();
    ```
- 第二部分：视频播放器状态方法
    ``` java
    //播放
    public abstract void start();
    //暂停
    public abstract void pause();
    //停止
    public abstract void stop();
    //重置播放器
    public abstract void reset();
    //是否正在播放
    public abstract boolean isPlaying();
    //调整进度
    public abstract void seekTo(long time);
    //释放播放器
    public abstract void release();
    //其他省略
    ```
- 第三部分：player绑定view后，需要监听播放状态
    ``` java
    /**
     * 绑定VideoView，监听播放异常，完成，开始准备，视频size变化，视频信息等操作
     */
    public void setPlayerEventListener(VideoPlayerListener playerEventListener) {
        this.mPlayerEventListener = playerEventListener;
    }
    ```


#### 4.3 具体ijk实现案例
- ijk的内核实现类代码如下所示
    ``` java
    public class IjkVideoPlayer extends AbstractVideoPlayer {
    
        protected IjkMediaPlayer mMediaPlayer;
        private int mBufferedPercent;
        private Context mAppContext;
    
        public IjkVideoPlayer(Context context) {
            if (context instanceof Application){
                mAppContext = context;
            } else {
                mAppContext = context.getApplicationContext();
            }
        }
    
        @Override
        public void initPlayer() {
            mMediaPlayer = new IjkMediaPlayer();
            //native日志
            IjkMediaPlayer.native_setLogLevel(VideoLogUtils.isIsLog()
                    ? IjkMediaPlayer.IJK_LOG_INFO : IjkMediaPlayer.IJK_LOG_SILENT);
            setOptions();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            initListener();
        }
    
        @Override
        public void setOptions() {
        }
    
        /**
         * ijk视频播放器监听listener
         */
        private void initListener() {
            // 设置监听，可以查看ijk中的IMediaPlayer源码监听事件
            // 设置视频错误监听器
            mMediaPlayer.setOnErrorListener(onErrorListener);
            // 设置视频播放完成监听事件
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            // 设置视频信息监听器
            mMediaPlayer.setOnInfoListener(onInfoListener);
            // 设置视频缓冲更新监听事件
            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            // 设置准备视频播放监听事件
            mMediaPlayer.setOnPreparedListener(onPreparedListener);
            // 设置视频大小更改监听器
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            // 设置视频seek完成监听事件
            mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
            // 设置时间文本监听器
            mMediaPlayer.setOnTimedTextListener(onTimedTextListener);
            mMediaPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
                @Override
                public boolean onNativeInvoke(int i, Bundle bundle) {
                    return true;
                }
            });
        }
    
        /**
         * 设置播放地址
         *
         * @param path    播放地址
         * @param headers 播放地址请求头
         */
        @Override
        public void setDataSource(String path, Map<String, String> headers) {
            // 设置dataSource
            if(path==null || path.length()==0){
                if (mPlayerEventListener!=null){
                    mPlayerEventListener.onInfo(PlayerConstant.MEDIA_INFO_URL_NULL, 0);
                }
                return;
            }
            try {
                //解析path
                Uri uri = Uri.parse(path);
                if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
                    RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(mAppContext, uri);
                    mMediaPlayer.setDataSource(rawDataSourceProvider);
                } else {
                    //处理UA问题
                    if (headers != null) {
                        String userAgent = headers.get("User-Agent");
                        if (!TextUtils.isEmpty(userAgent)) {
                            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", userAgent);
                        }
                    }
                    mMediaPlayer.setDataSource(mAppContext, uri, headers);
                }
            } catch (Exception e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 用于播放raw和asset里面的视频文件
         */
        @Override
        public void setDataSource(AssetFileDescriptor fd) {
            try {
                mMediaPlayer.setDataSource(new RawDataSourceProvider(fd));
            } catch (Exception e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 设置渲染视频的View,主要用于TextureView
         * @param surface                           surface
         */
        @Override
        public void setSurface(Surface surface) {
            mMediaPlayer.setSurface(surface);
        }
    
        /**
         * 准备开始播放（异步）
         */
        @Override
        public void prepareAsync() {
            try {
                mMediaPlayer.prepareAsync();
            } catch (IllegalStateException e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 暂停
         */
        @Override
        public void pause() {
            try {
                mMediaPlayer.pause();
            } catch (IllegalStateException e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 播放
         */
        @Override
        public void start() {
            try {
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 停止
         */
        @Override
        public void stop() {
            try {
                mMediaPlayer.stop();
            } catch (IllegalStateException e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 重置播放器
         */
        @Override
        public void reset() {
            mMediaPlayer.reset();
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            setOptions();
        }
    
        /**
         * 是否正在播放
         */
        @Override
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }
    
    
        /**
         * 调整进度
         */
        @Override
        public void seekTo(long time) {
            try {
                mMediaPlayer.seekTo((int) time);
            } catch (IllegalStateException e) {
                mPlayerEventListener.onError();
            }
        }
    
        /**
         * 释放播放器
         */
        @Override
        public void release() {
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnInfoListener(null);
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            new Thread() {
                @Override
                public void run() {
                    try {
                        mMediaPlayer.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    
        /**
         * 获取当前播放的位置
         */
        @Override
        public long getCurrentPosition() {
            return mMediaPlayer.getCurrentPosition();
        }
    
        /**
         * 获取视频总时长
         */
        @Override
        public long getDuration() {
            return mMediaPlayer.getDuration();
        }
    
        /**
         * 获取缓冲百分比
         */
        @Override
        public int getBufferedPercentage() {
            return mBufferedPercent;
        }
    
        /**
         * 设置渲染视频的View,主要用于SurfaceView
         */
        @Override
        public void setDisplay(SurfaceHolder holder) {
            mMediaPlayer.setDisplay(holder);
        }
    
        /**
         * 设置音量
         */
        @Override
        public void setVolume(float v1, float v2) {
            mMediaPlayer.setVolume(v1, v2);
        }
    
        /**
         * 设置是否循环播放
         */
        @Override
        public void setLooping(boolean isLooping) {
            mMediaPlayer.setLooping(isLooping);
        }
    
        /**
         * 设置播放速度
         */
        @Override
        public void setSpeed(float speed) {
            mMediaPlayer.setSpeed(speed);
        }
    
        /**
         * 获取播放速度
         */
        @Override
        public float getSpeed() {
            return mMediaPlayer.getSpeed(0);
        }
    
        /**
         * 获取当前缓冲的网速
         */
        @Override
        public long getTcpSpeed() {
            return mMediaPlayer.getTcpSpeed();
        }
    
        /**
         * 设置视频错误监听器
         * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
         * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
         * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
         * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
         * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断，一般是视频源异常或者不支持的视频类型。
         * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
         * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
         */
        private IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
                mPlayerEventListener.onError();
                VideoLogUtils.d("IjkVideoPlayer----listener---------onError ——> STATE_ERROR ———— what：" + framework_err + ", extra: " + impl_err);
                return true;
            }
        };
    
        /**
         * 设置视频播放完成监听事件
         */
        private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mPlayerEventListener.onCompletion();
                VideoLogUtils.d("IjkVideoPlayer----listener---------onCompletion ——> STATE_COMPLETED");
            }
        };
    
    
        /**
         * 设置视频信息监听器
         */
        private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                mPlayerEventListener.onInfo(what, extra);
                VideoLogUtils.d("IjkVideoPlayer----listener---------onInfo ——> ———— what：" + what + ", extra: " + extra);
                return true;
            }
        };
    
        /**
         * 设置视频缓冲更新监听事件
         */
        private IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
                mBufferedPercent = percent;
            }
        };
    
    
        /**
         * 设置准备视频播放监听事件
         */
        private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mPlayerEventListener.onPrepared();
                VideoLogUtils.d("IjkVideoPlayer----listener---------onPrepared ——> STATE_PREPARED");
            }
        };
    
        /**
         * 设置视频大小更改监听器
         */
        private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height,
                                           int sar_num, int sar_den) {
                int videoWidth = iMediaPlayer.getVideoWidth();
                int videoHeight = iMediaPlayer.getVideoHeight();
                if (videoWidth != 0 && videoHeight != 0) {
                    mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
                }
                VideoLogUtils.d("IjkVideoPlayer----listener---------onVideoSizeChanged ——> WIDTH：" + width + "， HEIGHT：" + height);
            }
        };
    
        /**
         * 设置时间文本监听器
         */
        private IMediaPlayer.OnTimedTextListener onTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
            @Override
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
    
            }
        };
    
        /**
         * 设置视频seek完成监听事件
         */
        private IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
    
            }
        };
    }
    ```



### 05.轻松创建不同内核播放器
#### 5.1 如何创建不同内核
- 先来看一下创建不同内核播放器的代码，只需要开发者传入一个类型参数，即可创建不同类的实例对象。代码如下所示
    ``` java
    /**
     * 获取PlayerFactory具体实现类，获取内核
     * 创建对象的时候只需要传递类型type，而不需要对应的工厂，即可创建具体的产品对象
     * TYPE_IJK                 IjkPlayer，基于IjkPlayer封装播放器
     * TYPE_NATIVE              MediaPlayer，基于原生自带的播放器控件
     * TYPE_EXO                 基于谷歌视频播放器
     * TYPE_RTC                 基于RTC视频播放器
     * @param type                              类型
     * @return
     */
    public static AbstractVideoPlayer getVideoPlayer(Context context,@PlayerConstant.PlayerType int type){
        if (type == PlayerConstant.PlayerType.TYPE_EXO){
            return ExoPlayerFactory.create().createPlayer(context);
        } else if (type == PlayerConstant.PlayerType.TYPE_IJK){
            return IjkPlayerFactory.create().createPlayer(context);
        } else if (type == PlayerConstant.PlayerType.TYPE_NATIVE){
            return MediaPlayerFactory.create().createPlayer(context);
        } else if (type == PlayerConstant.PlayerType.TYPE_RTC){
            return IjkPlayerFactory.create().createPlayer(context);
        } else {
            return IjkPlayerFactory.create().createPlayer(context);
        }
    }
    ```


#### 5.2 使用一点设计模式
- 使用工厂模式创建不同对象的动机是什么，为何要这样使用？
    - 一个视频播放器可以提供多个内核Player（如ijk、exo、media，rtc等等）， 这些player都源自同一个基类，不过在继承基类后不同的子类修改了部分属性从而使得它们可以呈现不同的外观。
    - 如果希望在使用这些内核player时，不需要知道这些具体内核的名字，只需要知道表示该内核类的一个参数，并提供一个调用方便的方法，把该参数传入方法即可返回一个相应的内核对象，此时，就可以使用工厂模式。
- 首先定义一个工厂抽象类，然后不同的内核播放器分别创建其具体的工厂实现具体类
    - PlayerFactory：抽象工厂，担任这个角色的是工厂方法模式的核心，任何在模式中创建对象的工厂类必须实现这个接口
    - ExoPlayerFactory：具体工厂，具体工厂角色含有与业务密切相关的逻辑，并且受到使用者的调用以创建具体产品对象。
- 如何使用，分为三步，具体操作如下所示
    - 1.先调用具体工厂对象中的方法createPlayer方法；2.根据传入产品类型参数获得具体的产品对象；3.返回产品对象并使用。
    - 简而言之，创建对象的时候只需要传递类型type，而不需要对应的工厂，即可创建具体的产品对象


#### 5.3 设计模式代码展示
- 抽象工厂类，代码如下所示
    ```java
    public abstract class PlayerFactory<T extends AbstractVideoPlayer> {
        public abstract T createPlayer(Context context);
    }
    ```
- 具体实现工厂类，代码如下所示
    ```java
    public class ExoPlayerFactory extends PlayerFactory<ExoMediaPlayer> {
    
        public static ExoPlayerFactory create() {
            return new ExoPlayerFactory();
        }
    
        @Override
        public ExoMediaPlayer createPlayer(Context context) {
            return new ExoMediaPlayer(context);
        }
    }
    ```
- 这种创建对象最大优点
    - 工厂方法用来创建所需要的产品，同时隐藏了哪种具体产品类将被实例化这一细节，用户只需要关心所需产品对应的工厂，无须关心创建细节，甚至无须知道具体产品类的类名。
    - 加入新的产品时，比如后期新加一个阿里播放器内核，这个时候就只需要添加一个具体工厂和具体产品就可以。系统的可扩展性也就变得非常好，完全符合“开闭原则”




### 06.其他一些设计说明
#### 6.1 稳定性设计说明
- 暂无，这里没有什么要说。


#### 6.2 测试case设计
- 目前给视频内核播放器，设置视频链接的时候，可能会播放异常。那么关于异常的类型，可能有很多种，这里我简单把它分成三类。大概如下所示：
    ``` java
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorType {
        //错误的链接
        int TYPE_SOURCE = 1;
        //解析异常
        int TYPE_PARSE = 2;
        //其他异常
        int TYPE_UNEXPECTED = 3;
    }
    ```
- 什么时候，出现错误是链接异常，谷歌播放器给出
    ```
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mPlayerEventListener != null) {
            int type = error.type;
            if (type == TYPE_SOURCE){
                //错误的链接
                mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_SOURCE,error.getMessage());
            } 
        }
    }
    ```
- 什么时候，出现错误是解析异常，ijk播放器举例说明，代码如下所示
    ```
    try {
        //解析path
        Uri uri = Uri.parse(path);
        if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
            RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(mAppContext, uri);
            mMediaPlayer.setDataSource(rawDataSourceProvider);
        } else {
            //处理UA问题
            if (headers != null) {
                String userAgent = headers.get("User-Agent");
                if (!TextUtils.isEmpty(userAgent)) {
                    mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", userAgent);
                }
            }
            mMediaPlayer.setDataSource(mAppContext, uri, headers);
        }
    } catch (Exception e) {
        mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_PARSE,e.getMessage());
    }
    ```
- 什么时候，出现错误是其他异常，ijk播放器举例说明，代码如下所示，主要是播放器给出的error监听，具体还要看视频库源码。
    ```
    /**
     * 设置视频错误监听器
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
     * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断，一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     */
    private IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
            mPlayerEventListener.onError(PlayerConstant.ErrorType.TYPE_UNEXPECTED,"监听异常"+ framework_err + ", extra: " + impl_err);
            VideoLogUtils.d("IjkVideoPlayer----listener---------onError ——> STATE_ERROR ———— what：" + framework_err + ", extra: " + impl_err);
            return true;
        }
    };
    ```



#### 6.3 拓展性相关设计
- 比如后期想要添加一个腾讯视频内核的播放器。代码如下所示，这个是简化的
    ``` java
    public class TxPlayerFactory extends PlayerFactory<TxMediaPlayer> {
    
        public static TxPlayerFactory create() {
            return new TxPlayerFactory();
        }
    
        @Override
        public TxMediaPlayer createPlayer(Context context) {
            return new TxMediaPlayer(context);
        }
    }
    
    public class TxMediaPlayer extends AbstractVideoPlayer {
        //省略接口的实现方法代码
    }
    ```

















