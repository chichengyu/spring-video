const app = getApp()

Page({
    data: {
      bgmList: [],
      serverUrl: "",
      videoParams: {}
    },
    onLoad: function (params) {
      var me = this;
      console.log(params);
      me.setData({
        videoParams: params
      });

      wx.showLoading({
        title: '请等待',
      });
      var serverUrl = app.serverUrl;
      var user = app.getGlobalUserInfo();
      //debugger;
      // 调用后端
      wx.request({
        url: serverUrl + '/bgm/list',
        method: "GET",
        header: {
          'content-type': 'application/json', // 默认值
          'token': user.token
        },
        success: function (res) {
          //console.log(res.data);
          wx.hideLoading();
          if (res.data.code == 1) {
            var bgmList = res.data.data;
            me.setData({
              bgmList: bgmList,
              serverUrl: serverUrl
            });
          } else if (res.data.code == -1) {
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
    },
    // 上传视频
    upload: function(e) {
      var me = this;

      var bgmId = e.detail.value.bgmId;
      var desc = e.detail.value.desc;
      var duration = me.data.videoParams.duration;
      var tmpHeight = me.data.videoParams.tmpHeight;
      var tmpWidth = me.data.videoParams.tmpWidth;
      var tmpVideoUrl = me.data.videoParams.tmpVideoUrl;
      var tmpCoverUrl = me.data.videoParams.tmpCoverUrl;// 封面

      // 上传短视频
      wx.showLoading({
        title: '上传中...',
      })
      var serverUrl = app.serverUrl;
      // fixme 修改原有的全局对象为本地缓存
      var userInfo = app.getGlobalUserInfo();

      wx.uploadFile({
        url: serverUrl + '/video/upload',
        formData: {
          userId: userInfo.id,    // fixme 原来的 app.userInfo.id
          bgmId: bgmId,
          desc: desc,
          videoSeconds: duration,
          videoHeight: tmpHeight,
          videoWidth: tmpWidth
        },
        filePath: tmpVideoUrl,
        name: 'file',
        header: {
          'content-type': 'application/json', // 默认值
          'token': userInfo.token
        },
        success: function (res) {
          var data = JSON.parse(res.data);
          // wx.hideLoading();
          if (data.code == 1) {
            wx.showToast({
              title: '上传成功！',
              icon:"success",
              success:function(){
                // 跳转回原页面
                // wx.navigateBack({
                //   delta: 1
                // })
                wx.navigateTo({
                  url: '../mine/mine?userInfo=' + JSON.stringify(userInfo),
                })
              }
            })
            
            // 上传视频封面，使用了 ffmpeg 后台截图视频封面，不用再上传封面
            /*var videoId = data.data.videoId;
            wx.showLoading({
              title: '上传中...',
            })
            wx.uploadFile({
              url: serverUrl + '/video/uploadCover',
              formData: {
                userId: userInfo.id,
                videoId: videoId
              },
              filePath: tmpCoverUrl,
              name: 'file',
              header: {
                'content-type': 'application/json' // 默认值
              },
              success: function (res) {
                var data = JSON.parse(res.data);
                console.log(data)
                wx.hideLoading();
                if (data.code == 1) {
                  wx.showToast({
                    title: '上传成功！',
                    icon: "success"
                  });
                  wx.navigateBack({
                    delta: 1,
                  })
                } else {
                  wx.showToast({
                    title: '上传失败！',
                    icon: "success"
                  });
                }
              }
            })*/
          } else if (data.code == -1) {
            wx.showToast({
              title: data.msg,
              duration: 2000,
              icon: "none",
              success:function(){
                wx.redirectTo({
                  url: '../userLogin/login',
                })
              }
            });
          } else {
            wx.showToast({
              title: '上传失败！',
              icon: "none"
            });
          }
        }
      })
    }
})

