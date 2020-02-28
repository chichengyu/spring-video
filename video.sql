/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : video

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-02-28 15:28:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for video_bgm
-- ----------------------------
DROP TABLE IF EXISTS `video_bgm`;
CREATE TABLE `video_bgm` (
  `id` varchar(64) NOT NULL,
  `author` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='背景音乐表';

-- ----------------------------
-- Records of video_bgm
-- ----------------------------
INSERT INTO `video_bgm` VALUES ('1232989868798902256', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902272', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902273', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902274', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902275', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902276', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902277', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902278', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902279', '测试歌手', '测试歌曲', '/bgm/9420.mp3');
INSERT INTO `video_bgm` VALUES ('1232989868798902286', '测试歌手', '测试歌曲', '/bgm/9420.mp3');

-- ----------------------------
-- Table structure for video_comments
-- ----------------------------
DROP TABLE IF EXISTS `video_comments`;
CREATE TABLE `video_comments` (
  `id` varchar(20) NOT NULL,
  `father_comment_id` varchar(20) DEFAULT NULL COMMENT '评论表父级id',
  `to_user_id` varchar(20) DEFAULT NULL COMMENT '被评论者id，该字段是为了便于取被评论者的信息',
  `video_id` varchar(20) NOT NULL COMMENT '视频id',
  `from_user_id` varchar(20) NOT NULL COMMENT '留言者，评论的用户id',
  `comment` text NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评论表';

-- ----------------------------
-- Records of video_comments
-- ----------------------------
INSERT INTO `video_comments` VALUES ('1232172278133952512', '1232174516059377664', '1230681055802642432', '1231949943510536192', '1231845590741159936', '测试评论', '2020-02-25 13:15:38');
INSERT INTO `video_comments` VALUES ('1232174516059377664', null, null, '1231949943510536192', '1230681055802642432', '我来评论试试', '2020-02-25 13:24:32');
INSERT INTO `video_comments` VALUES ('1232193551656423424', null, null, '1231949943510536192', '1230681055802642432', '再来测试试试', '2020-02-25 14:40:10');
INSERT INTO `video_comments` VALUES ('1232203955749130240', '1232193551656423424', '1230681055802642432', '1231949943510536192', '1231845590741159936', '121132', '2020-02-25 15:21:31');
INSERT INTO `video_comments` VALUES ('1232227099977322496', null, null, '1231949943510536192', '1231845590741159936', '，all 健健康康', '2020-02-25 16:53:29');
INSERT INTO `video_comments` VALUES ('1232231908063186944', '1232193551656423424', '1230681055802642432', '1231949943510536192', '1230681055802642432', '我实打实大苏打实打实', '2020-02-25 17:12:35');

-- ----------------------------
-- Table structure for video_search_records
-- ----------------------------
DROP TABLE IF EXISTS `video_search_records`;
CREATE TABLE `video_search_records` (
  `id` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL COMMENT '搜索的内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频搜索的记录表';

-- ----------------------------
-- Records of video_search_records
-- ----------------------------
INSERT INTO `video_search_records` VALUES ('1', '手机测试');
INSERT INTO `video_search_records` VALUES ('1231524691215257600', '测试播放');
INSERT INTO `video_search_records` VALUES ('1231525454255624192', '手机测试');
INSERT INTO `video_search_records` VALUES ('1231526010915262464', '测试视频上传与视频封面截图');
INSERT INTO `video_search_records` VALUES ('1231918810387320832', '红过敏明');
INSERT INTO `video_search_records` VALUES ('18051309YBCMHYRP', '测试视频上传与视频封面截图');

-- ----------------------------
-- Table structure for video_users
-- ----------------------------
DROP TABLE IF EXISTS `video_users`;
CREATE TABLE `video_users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) DEFAULT NULL COMMENT '我的头像，如果没有默认给一张',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `fans_counts` int(11) DEFAULT '0' COMMENT '我的粉丝数量',
  `follow_counts` int(11) DEFAULT '0' COMMENT '我关注的人总数',
  `receive_like_counts` int(11) DEFAULT '0' COMMENT '我接受到的赞美/收藏 的数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of video_users
-- ----------------------------
INSERT INTO `video_users` VALUES ('1230681055802642432', 'admin', '$2a$10$hRv5ncM6x8DXkaAEnZpfCe/EXodndj0f29FqA/iu88ouHeNH1d5DO', '/1230681055802642432/face/tmp_c6a0ad37ba58ca25b556669c336ee7fb.jpg', 'admin', '2', '0', '2');
INSERT INTO `video_users` VALUES ('1231845590741159936', 'admin123', '$2a$10$OSxW8cpsXmt3/bQmpFroaeig0l6mL/ulyPbNLKsKc8XfHwiMoy.PS', '/1231845590741159936/face/wx993172f7da9acf1f.o6zAJsyKb0xOaMfAu-DA3-l1u6Bc.zxQSnEv9jyow62c48776e31a6fd2d93accccd600321c.jpg', 'admin123', '0', '2', '1');

-- ----------------------------
-- Table structure for video_users_fans
-- ----------------------------
DROP TABLE IF EXISTS `video_users_fans`;
CREATE TABLE `video_users_fans` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `fan_id` varchar(64) NOT NULL COMMENT '粉丝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户粉丝关联关系表';

-- ----------------------------
-- Records of video_users_fans
-- ----------------------------
INSERT INTO `video_users_fans` VALUES ('1231905181420097536', '1230681055802642432', '1231845590741159936');

-- ----------------------------
-- Table structure for video_users_like_videos
-- ----------------------------
DROP TABLE IF EXISTS `video_users_like_videos`;
CREATE TABLE `video_users_like_videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `video_id` varchar(64) NOT NULL COMMENT '视频',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_video_rel` (`user_id`,`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户喜欢的/赞过的视频';

-- ----------------------------
-- Records of video_users_like_videos
-- ----------------------------
INSERT INTO `video_users_like_videos` VALUES ('1231906215492194304', '1230681055802642432', '1231805988366061568');
INSERT INTO `video_users_like_videos` VALUES ('1231910599244517376', '1231845590741159936', '1231805988366061568');
INSERT INTO `video_users_like_videos` VALUES ('1231914293298073600', '1231845590741159936', '1231911451606781952');

-- ----------------------------
-- Table structure for video_users_report
-- ----------------------------
DROP TABLE IF EXISTS `video_users_report`;
CREATE TABLE `video_users_report` (
  `id` varchar(64) NOT NULL,
  `deal_user_id` varchar(64) NOT NULL COMMENT '被举报用户id',
  `deal_video_id` varchar(64) NOT NULL,
  `title` varchar(128) NOT NULL COMMENT '类型标题，让用户选择，详情见 枚举',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `userid` varchar(64) NOT NULL COMMENT '举报人的id',
  `create_date` datetime NOT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报用户表';

-- ----------------------------
-- Records of video_users_report
-- ----------------------------
INSERT INTO `video_users_report` VALUES ('1232158919523176448', '1231845590741159936', '1231949421160304640', '色情低俗', '测试举报功能', '1230681055802642432', '2020-02-25 12:22:33');

-- ----------------------------
-- Table structure for video_videos
-- ----------------------------
DROP TABLE IF EXISTS `video_videos`;
CREATE TABLE `video_videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '发布者id',
  `audio_id` varchar(64) DEFAULT NULL COMMENT '用户使用音频的信息',
  `video_desc` varchar(128) DEFAULT NULL COMMENT '视频描述',
  `video_path` varchar(255) NOT NULL COMMENT '视频存放的路径',
  `video_seconds` float(6,2) DEFAULT NULL COMMENT '视频秒数',
  `video_width` int(6) DEFAULT NULL COMMENT '视频宽度',
  `video_height` int(6) DEFAULT NULL COMMENT '视频高度',
  `cover_path` varchar(255) DEFAULT NULL COMMENT '视频封面图',
  `like_counts` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢/赞美的数量',
  `status` int(1) NOT NULL COMMENT '视频状态：\r\n1、发布成功\r\n2、禁止播放，管理员操作',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

-- ----------------------------
-- Records of video_videos
-- ----------------------------
INSERT INTO `video_videos` VALUES ('1231805988366061568', '1230681055802642432', '18052674D26HH3X3', '测试视频收藏功能', '/1230681055802642432/video/wx993172f7da9acf1f.o6zAJsyKb0xOaMfAu-DA3-l1u6Bc.vVWBvNCJ7cz000ae1ebecb9a25e10de99afaef8381d9.mp4_ffmpeg.mp4', '41.07', '540', '960', '/1230681055802642432/cover/wx993172f7da9acf1f.jpg', '2', '1', '2020-02-24 13:00:08');
INSERT INTO `video_videos` VALUES ('1231911451606781952', '1231845590741159936', '18052674D26HH3X2', '真机测试', '/1231845590741159936/video/tmp_a5e4d9490573d25afd9e45304d96f9cc.mp4_ffmpeg.mp4', '21.00', '448', '960', '/1231845590741159936/cover/tmp_a5e4d9490573d25afd9e45304d96f9cc.jpg', '1', '1', '2020-02-24 19:59:13');
INSERT INTO `video_videos` VALUES ('1231914897437233152', '1231845590741159936', '18052674D26HH3X1', '测试', '/1231845590741159936/video/wx993172f7da9acf1f.o6zAJsyKb0xOaMfAu-DA3-l1u6Bc.MknFULg995Pp00ae1ebecb9a25e10de99afaef8381d9.mp4_ffmpeg.mp4', '41.07', '540', '960', '/1231845590741159936/cover/wx993172f7da9acf1f.jpg', '0', '1', '2020-02-24 20:12:54');
INSERT INTO `video_videos` VALUES ('1231916098253885440', '1231845590741159936', '18052674D26HH3X2', '手机测试功能跳转', '/1231845590741159936/video/tmp_78036c240ec8fb2633f0ed609444f92d.mp4_ffmpeg.mp4', '15.00', '944', '540', '/1231845590741159936/cover/tmp_78036c240ec8fb2633f0ed609444f92d.jpg', '0', '1', '2020-02-24 20:17:40');
INSERT INTO `video_videos` VALUES ('1231918429393522688', '1231845590741159936', '18052674D26HH3X2', '红过敏明', '/1231845590741159936/video/tmp_20e00f0008e6da32c8643466e09fc884.mp4_ffmpeg.mp4', '17.00', '544', '960', '/1231845590741159936/cover/tmp_20e00f0008e6da32c8643466e09fc884.jpg', '0', '1', '2020-02-24 20:26:56');
INSERT INTO `video_videos` VALUES ('1231919173655990272', '1231845590741159936', '', '测试不配置背景音乐', '/1231845590741159936/video/tmp_e991354b7601eade9acf6cea17df55e6.mp4', '41.00', '544', '960', '/1231845590741159936/cover/tmp_e991354b7601eade9acf6cea17df55e6.jpg', '0', '1', '2020-02-24 20:29:54');
INSERT INTO `video_videos` VALUES ('1231949421160304640', '1231845590741159936', '18052674D26HH32P', '测试删除视频功能', '/1231845590741159936/video/tmp_ae178d34ffd52ca2b52263b7d37b6f0d.mp4_ffmpeg.mp4', '10.00', '368', '640', '/1231845590741159936/cover/tmp_ae178d34ffd52ca2b52263b7d37b6f0d.jpg', '0', '1', '2020-02-24 22:30:05');
INSERT INTO `video_videos` VALUES ('1231949943510536192', '1231845590741159936', '', '测试删除', '/1231845590741159936/video/tmp_7736357b3f0dd457fa04f3e8acead1f1.mp4', '16.00', '368', '640', '/1231845590741159936/cover/tmp_7736357b3f0dd457fa04f3e8acead1f1.jpg', '0', '1', '2020-02-24 22:32:10');
