<?php
    /**
     * Created by JetBrains PhpStorm.
     * User: taoqili
     * Date: 11-12-28
     * Time: 上午9:54
     * To change this template use File | Settings | File Templates.
     */
    header("Content-Type: text/html; charset=utf-8");
    error_reporting(E_ERROR|E_WARNING);
    include "../upyun.class.php";
    include "../upyun.config.php";
    //远程抓取图片配置
    $config = array(
        "savePath" => "upload/" ,            //保存路径
        "allowFiles" => array( ".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp", ".tif" ) , //文件允许格式
        "maxSize" => 20480                    //文件大小限制，单位KB
    );
    $uri = htmlspecialchars( $_POST[ 'upfile' ] );
    $uri = str_replace( "&amp;" , "&" , $uri );
    $upyun = new UpYun($img_bucket, $img_username, $img_passwd);
    getRemoteImage( $uri,$config,$upyun );

    /**
     * 远程抓取
     * @param $uri
     * @param $config
     */
    function getRemoteImage( $uri,$config, $upyun)
    {
        //忽略抓取时间限制
        set_time_limit( 0 );
        //ue_separate_ue  ue用于传递数据分割符号
        $imgUrls = explode( "ue_separate_ue" , $uri );
        $tmpNames = array();
        foreach ( $imgUrls as $imgUrl ) {
            //http开头验证
            if(strpos($imgUrl,"http")!==0){
                array_push( $tmpNames , "error" );
                continue;
            }
            //获取请求头
            $heads = get_headers( $imgUrl );
            //死链检测
            if ( !( stristr( $heads[ 0 ] , "200" ) && stristr( $heads[ 0 ] , "OK" ) ) ) {
                array_push( $tmpNames , "error" );
                continue;
            }

            //格式验证(扩展名验证和Content-Type验证)
            $fileType = strtolower( strrchr( $imgUrl , '.' ) );
            if ( !in_array( $fileType , $config[ 'allowFiles' ] ) || stristr( $heads[ 'Content-Type' ] , "image" ) ) {
                array_push( $tmpNames , "error" );
                continue;
            }

            //打开输出缓冲区并获取远程图片
            ob_start();
            $context = stream_context_create(
                array (
                    'http' => array (
                        'follow_location' => false // don't follow redirects
                    )
                )
            );
            //请确保php.ini中的fopen wrappers已经激活
            readfile( $imgUrl,false,$context);
            $img = ob_get_contents();
            ob_end_clean();

            //大小验证
            $uriSize = strlen( $img ); //得到图片大小
            $allowSize = 1024 * $config[ 'maxSize' ];
            if ( $uriSize > $allowSize ) {
                array_push( $tmpNames , "error" );
                continue;
            }
            //创建保存位置
            $savePath = $config[ 'savePath' ];
            if ( !file_exists( $savePath ) ) {
                mkdir( "$savePath" , 0777 );
            }
            //写入文件
            $tmpName = $savePath . rand( 1 , 10000 ) . time() . strrchr( $imgUrl , '.' );
            try {
                $fp2 = @fopen( $tmpName , "a" );
                fwrite( $fp2 , $img );
                fclose( $fp2 );
                array_push( $tmpNames ,  $tmpName );
            } catch ( Exception $e ) {
                array_push( $tmpNames , "error" );
            }

            try {
                $con = implode( "ue_separate_ue" , $tmpNames );
                $opts = array(
                    UpYun::CONTENT_MD5 => md5(file_get_contents($con))
                );
                $fh = fopen($con, "rb");
                $rsp = $upyun->writeFile("/" . $con, $fh, True, $opts);	// 上传文件，自动创建目录
                fclose($fh);
            } catch(Exception $e) {
                $log_file = "./error_log.txt";
                $err = "RemoteImage " . date("Y-m-d H:m:s") . " " . $e->getCode() . " " . $e->getMessage() . " " . $con . "\r\n";
                $handle = fopen($log_file, "a");
                fwrite($handle, $err);
                fclose($handle);
                exit;
            }
        }
        
        /**
         * 返回数据格式
         * {
         *   'url'   : '新地址一ue_separate_ue新地址二ue_separate_ue新地址三',
         *   'srcUrl': '原始地址一ue_separate_ue原始地址二ue_separate_ue原始地址三'，
         *   'tip'   : '状态提示'
         * }
         */
        echo "{'url':'" . implode( "ue_separate_ue" , $tmpNames ) . "','tip':'远程图片抓取成功！','srcUrl':'" . $uri . "'}";
    }
