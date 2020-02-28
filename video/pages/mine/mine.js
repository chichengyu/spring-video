var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
    isMe: true,
    isFollow: false,


    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedLike: "",
    isSelectedFollow: "",

    myVideoList: [],
    myVideoPage: 1,
    myVideoTotal: 1,

    likeVideoList: [],
    likeVideoPage: 1,
    likeVideoTotal: 1,

    followVideoList: [],
    followVideoPage: 1,
    followVideoTotal: 1,

    myWorkFalg: false,
    myLikesFalg: true,
    myFollowFalg: true

  },

  onLoad: function (params) {
    var me = this;

    // var user = app.userInfo;
    // fixme 修改原有的全局对象为本地缓存
    var user = app.getGlobalUserInfo();
    var userId = user.id;

    var publisherId = params.publisherId;
    if (publisherId != null && publisherId != '' && publisherId != undefined) {
      userId = publisherId;
      me.setData({
        isMe: user.id == userId,
        publisherId: publisherId,
        serverUrl: app.serverUrl
      })
    }
    me.setData({
      userId: userId
    })

    wx.showLoading({
      title: '请等待...',
    });
    var serverUrl = app.serverUrl;

    // 调用后端
    wx.request({
      url: serverUrl + '/user/query?userId=' + userId + "&fanId=" + user.id,
      method: "GET",
      header: {
        'content-type': 'application/json', // 默认值
        'token': user.token
      },
      success: function (res) {
        wx.hideLoading();
        if (res.data.code == 1) {
          var userInfo = res.data.data;
          var faceUrl = "../resource/images/noneface.png";
          if (userInfo.faceImage != null && userInfo.faceImage != '' && userInfo.faceImage != undefined) {
            faceUrl = serverUrl + userInfo.faceImage;
          }
          me.setData({
            faceUrl: faceUrl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname,
            isFollow: userInfo.follow
          });
        } else if (res.data.code == 0) {
          wx.showToast({
            title: res.data.msg,
            duration: 3000,
            icon: "none",
            success: function () {
              wx.redirectTo({
                url: '../userLogin/login',
              })
            }
          })
        }
      }
    })

    me.getMyVideoList(1);
  },
  // 关注 与 取消关注
  followMe: function (e) {
    var me = this;

    var user = app.getGlobalUserInfo();
    var userId = user.id;
    var publisherId = me.data.publisherId;

    var followType = e.currentTarget.dataset.followtype;

    // 1：关注 0：取消关注
    var url = '';
    if (followType == '1') {
      url = '/user/beyourfans?userId=' + publisherId + '&fanId=' + userId;
    } else {
      url = '/user/dontbeyourfans?userId=' + publisherId + '&fanId=' + userId;
    }
    wx.showLoading();
    wx.request({
      url: app.serverUrl + url,
      method: 'GET',
      header: {
        'content-type': 'application/json', // 默认值
        'token': user.token
      },
      success: function (res) {
        wx.hideLoading();
        if(res.data.code == 1){
          if (followType == '1') {
            me.setData({
              isFollow: true,
              fansCounts: ++me.data.fansCounts
            })
          } else {
            me.setData({
              isFollow: false,
              fansCounts: --me.data.fansCounts
            })
          }
          wx.showToast({
            title: res.data.msg,
            icon:'success'
          })
        }else{
          wx.showToast({
            title: '请先登陆',
            icon: "none",
            duration: 3000,
            complete: function () {
              // 页面跳转
              wx.redirectTo({
                url: '../userLogin/login',
              });
            }
          })
        }
      }
    })
  },
  // 注销登陆
  logout: function () {
    // var user = app.userInfo;
    var user = app.getGlobalUserInfo();

    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    if(!user){
      wx.showToast({
        title: '请先登陆',
        icon: "none",
        duration: 3000,
        success:function(){
          // 页面跳转
          wx.redirectTo({
            url: '../userLogin/login',
          });
        }
      })
      return;
    }
    // 调用后端
    wx.request({
      url: serverUrl + '/logout?userId=' + user.id,
      method: "GET",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        wx.hideLoading();
        if (res.data.code == 1) {
          // 登录成功跳转 
          wx.showToast({
            title: '注销成功',
            icon: 'success',
            duration: 2000
          });
          // app.userInfo = null;
          // 注销以后，清空缓存
          wx.removeStorageSync("userInfo")
          // 页面跳转
          wx.redirectTo({
            url: '../userLogin/login',
          })
        }else{
          wx.showToast({
            title: res.data.msg,
            icon:'none',
            duration:3000
          })
        }
      }
    })
  },
  // 上传头像
  changeFace: function () {
    var me = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths;

        wx.showLoading({
          title: '上传中...',
        })
        var serverUrl = app.serverUrl;
        // fixme 修改原有的全局对象为本地缓存
        var userInfo = app.getGlobalUserInfo();

        wx.uploadFile({
          url: serverUrl + '/user/uploadFace?userId=' + userInfo.id,  //app.userInfo.id,
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type': 'application/json', // 默认值
            'token': userInfo.token
          },
          success: function (res) {
            var data = JSON.parse(res.data);
            console.log(data);
            wx.hideLoading();
            if (data.code == 1) {
              wx.showToast({
                title: '上传成功!~~',
                icon: "success"
              });
              var imageUrl = data.data.url;
              me.setData({
                faceUrl: serverUrl + imageUrl
              });
            } else if (data.code == -1) {
              wx.showToast({
                title: data.msg,
                duration:20000,
                icon:'none',
                success: function () {
                  wx.redirectTo({
                    url: '../userLogin/login',
                  })
                }
              });
            } else{
              wx.showToast({
                title: res.data.msg,
                duration: 2000,
                icon: "none",
                success: function () {
                  wx.redirectTo({
                    url: '../userLogin/login',
                  })
                }
              });
            }
          }
        })
      }
    })
  },
  // 上传视频
  uploadVideo: function () {
    // fixme 视频上传复用
    // videoUtil.uploadVideo();
    // 以下是原来的代码，不删除，便于参照
    var me = this;

    wx.chooseVideo({
      sourceType: ['album'],
      success: function (res) {
        var duration = res.duration;
        var tmpHeight = res.height;
        var tmpWidth = res.width;
        var tmpVideoUrl = res.tempFilePath;
        var tmpCoverUrl = res.thumbTempFilePath;
        // 视频时长限制
        if (duration > 60) {
          wx.showToast({
            title: '视频长度不能超过60秒！',
            icon: "none",
            duration: 2500
          })
        } else if (duration < 1) {
          wx.showToast({
            title: '视频长度太短，请上传超过1秒的视频！',
            icon: "none",
            duration: 2500
          })
        } else {
          // 打开选择bgm的页面
          wx.navigateTo({
            url: '../chooseBgm/chooseBgm?duration=' + duration
            + "&tmpHeight=" + tmpHeight
            + "&tmpWidth=" + tmpWidth
            + "&tmpVideoUrl=" + tmpVideoUrl
            + "&tmpCoverUrl=" + tmpCoverUrl
            ,
          })
        }
      }
    })

  },
  // 作品
  doSelectWork: function () {
    this.setData({
      isSelectedWork: "video-info-selected",
      isSelectedLike: "",
      isSelectedFollow: "",

      myWorkFalg: false,
      myLikesFalg: true,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });
    // 获取作品
    this.getMyVideoList(1);
  },
  // 收藏作品
  doSelectLike: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedLike: "video-info-selected",
      isSelectedFollow: "",

      myWorkFalg: true,
      myLikesFalg: false,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });
    // 获取收藏作品列表
    this.getMyLikesList(1);
  },
  
  doSelectFollow: function () {
    this.setData({
      isSelectedWork: "",
      isSelectedLike: "",
      isSelectedFollow: "video-info-selected",

      myWorkFalg: true,
      myLikesFalg: true,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });

    this.getMyFollowList(1)
  },
  // 获取作品列表
  getMyVideoList: function (page) {
    var me = this;

    // 查询视频信息
    wx.showLoading();
    // 调用后端
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/showAll/?page=' + page + '&pageSize=6',
      method: "POST",
      data: {
        userId: me.data.userId
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        if(res.data.code == 1){
          var myVideoList = res.data.data.rows;

          var newVideoList = me.data.myVideoList;
          me.setData({
            myVideoPage: page,
            myVideoList: newVideoList.concat(myVideoList),
            myVideoTotal: res.data.data.total,
            serverUrl: app.serverUrl
          });
        }else{
          wx.showToast({
            title: '获取失败！',
            icon: 'none'
          })
        }
      }
    })
  },
  // 获取收藏列表
  getMyLikesList: function (page) {
    var me = this;
    var userId = me.data.userId;

    // 查询视频信息
    wx.showLoading();
    // 调用后端
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/showMyLike/?userId=' + userId + '&page=' + page + '&pageSize=6',
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        wx.hideLoading();
        if(res.data.code == 1){
          var likeVideoList = res.data.data.rows;
          var newVideoList = me.data.likeVideoList;
          me.setData({
            likeVideoPage: page,
            likeVideoList: newVideoList.concat(likeVideoList),
            likeVideoTotal: res.data.data.total,
            serverUrl: app.serverUrl
          });
        }else{
          wx.showToast({
            title: '获取失败！',
            icon: 'none'
          })
        }
      }
    })
  },
  // 关注列表
  getMyFollowList: function (page) {
    var me = this;
    var userId = me.data.userId;

    // 查询视频信息
    wx.showLoading();
    // 调用后端
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/showMyFollow/?userId=' + userId + '&page=' + page + '&pageSize=6',
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        //console.log(res.data);
        wx.hideLoading();
        if(res.data.code == 1){
          var followVideoList = res.data.data.rows;
          var newVideoList = me.data.followVideoList;
          me.setData({
            followVideoPage: page,
            followVideoList: newVideoList.concat(followVideoList),
            followVideoTotal: res.data.data.total,
            serverUrl: app.serverUrl
          });
        }else{
          wx.showToast({
            title: '获取失败！',
            icon:'none'
          })
        }
      }
    })
  },
  // 点击跳转到视频详情页面
  showVideo: function (e) {
    //console.log(e);
    var myWorkFalg = this.data.myWorkFalg;
    var myLikesFalg = this.data.myLikesFalg;
    var myFollowFalg = this.data.myFollowFalg;
    
    if (!myWorkFalg) {
      var videoList = this.data.myVideoList;
    } else if (!myLikesFalg) {
      var videoList = this.data.likeVideoList;
    } else if (!myFollowFalg) {
      var videoList = this.data.followVideoList;
    }

    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoList[arrindex]);

    wx.redirectTo({
      url: '../videoinfo/videoinfo?videoInfo=' + videoInfo
    })

  },
  // 到底部后触发加载
  onReachBottom: function () {
    var myWorkFalg = this.data.myWorkFalg;
    var myLikesFalg = this.data.myLikesFalg;
    var myFollowFalg = this.data.myFollowFalg;

    if (!myWorkFalg) {
      var currentPage = this.data.myVideoPage;
      var totalPage = this.data.myVideoTotal;
      // 获取总页数进行判断，如果当前页数和总页数相等，则不分页
      if (currentPage === totalPage) {
        wx.showToast({
          title: '已经没有视频啦...',
          icon: "none"
        });
        return;
      }
      var page = currentPage + 1;
      this.getMyVideoList(page);
    } else if (!myLikesFalg) {
      var currentPage = this.data.likeVideoPage;
      var totalPage = this.data.myLikesTotal;
      // 获取总页数进行判断，如果当前页数和总页数相等，则不分页
      if (currentPage === totalPage) {
        wx.showToast({
          title: '已经没有视频啦...',
          icon: "none"
        });
        return;
      }
      var page = currentPage + 1;
      this.getMyLikesList(page);
    } else if (!myFollowFalg) {
      var currentPage = this.data.followVideoPage;
      var totalPage = this.data.followVideoTotal;
      // 获取总页数进行判断，如果当前页数和总页数相等，则不分页
      if (currentPage === totalPage) {
        wx.showToast({
          title: '已经没有视频啦...',
          icon: "none"
        });
        return;
      }
      var page = currentPage + 1;
      this.getMyFollowList(page);
    }
  }
})
