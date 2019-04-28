//播放器状态集
var PlayerStatus = {
    //html元素
    $needle: null,
    $curBar: null,
    $curTime: null,
    $totTime: null,
    $processBar: null,
    $processBtn: null,
    $rdyBar: null,
    $playBtn: null,
    $pauseBtn: null,
    $playList: null,
    $listContent: null,
    //
    player: null,
    isPlaying: false,
    currentIndex: 0,
    interval: 0,
    processBtnState: 0,
    originX: 0,
    diskCovers: [],
    songUpdated: true,
    singleLoop: false
};
/**
 * 旋转状态设置
 * @param  $ele: 元素(封面)
 * @param  state: 状态running/paused
 */
PlayerStatus.changeAnimationState = function($ele, state) {
    $ele.css({
        'animation-play-state': state,
        '-webkit-animation-play-state': state
    });
};

/**
 * 针状态设置，放下(true)或放下(false)
 * @param  (Boolean) play
 * 
 */
PlayerStatus.moveNeedle = function(play) {
    if (play) {
        PlayerStatus.$needle.removeClass("pause-needle").addClass("resume-needle");
    } else {
        PlayerStatus.$needle.removeClass("resume-needle").addClass("pause-needle");
    }
};

PlayerStatus.play = function() {
    PlayerStatus.player.play();
    PlayerStatus.isPlaying = true;
    //封面旋转，指针放下
    PlayerStatus.changeAnimationState(PlayerStatus.diskCovers[1], 'running');
    PlayerStatus.moveNeedle(true);
    //按钮改变
    PlayerStatus.$playBtn.hide();
    PlayerStatus.$pauseBtn.show();
};

PlayerStatus.pause = function() {
    PlayerStatus.player.pause();
    PlayerStatus.isPlaying = false;
    PlayerStatus.moveNeedle(false);
    PlayerStatus.changeAnimationState(PlayerStatus.diskCovers[1], 'paused');
    PlayerStatus.$playBtn.show();
    PlayerStatus.$pauseBtn.hide();
};

/**
 * 修改PlayList状态
 */
PlayerStatus.validatePlayList = function() {
    PlayerStatus.$listContent.children('li.active').removeClass('active').children("div.song-play").remove();
    PlayerStatus.$listContent.children('li').eq(PlayerStatus.currentIndex).addClass('active')
        .prepend($('<div>').addClass('song-play'));
    PlayerStatus.$listContent.animate({
        scrollTop: (PlayerStatus.currentIndex + 1) * 41 - PlayerStatus.$listContent.height() / 2
    });
};

PlayerStatus.preSwitchSong = function() {
    PlayerStatus.songUpdated = false;
    PlayerStatus.player.pause();
    PlayerStatus.moveNeedle(false);
    PlayerStatus.validatePlayList();
};

/**
 * 更新模糊背景
 */
PlayerStatus.updatePic = function() {
    $(".bg").css('background-image', 'url(' + playlist[PlayerStatus.currentIndex].img + ')');
};

PlayerStatus.updateMusicInfo = function() {
    $('#songName').html(playlist[PlayerStatus.currentIndex].songName);
    if (playlist[PlayerStatus.currentIndex].artist == "") {
        $('#artist').html(playlist[PlayerStatus.currentIndex].album);
    } else if (playlist[PlayerStatus.currentIndex].album == "") {
        $('#artist').html(playlist[PlayerStatus.currentIndex].artist);
    } else {
        $('#artist').html(playlist[PlayerStatus.currentIndex].artist + ' - ' + playlist[PlayerStatus.currentIndex].album);
    }
};

PlayerStatus.updateSong = function() {
    //更新音乐源
    PlayerStatus.player.src = playlist[PlayerStatus.currentIndex].songurl;
    setTimeout(PlayerStatus.updatePic, 10);
    PlayerStatus.updateMusicInfo();
    if (PlayerStatus.isPlaying) {
        setTimeout(PlayerStatus.play, 500);
    }
};

PlayerStatus.updateCoverState = function(derection, preLoad) {
    var temp, speed = 800,
        defualtUrl = "../resource/images/placeholder_disk_play_song.png",
        preIndex = PlayerStatus.currentIndex - 1 < 0 ? playlist.length - 1 : PlayerStatus.currentIndex - 1,
        nextIndex = PlayerStatus.currentIndex + 2 > playlist.length ? 0 : PlayerStatus.currentIndex + 1,
        posLeft = -PlayerStatus.diskCovers[0].width() / 2,
        posCenter = '50%',
        posRight = PlayerStatus.diskCovers[0].parent().width() + PlayerStatus.diskCovers[0].width() / 2,
        updateAlbumImgs = function() {
            PlayerStatus.diskCovers[0].children('.album').attr('src', playlist[preIndex].img);
            PlayerStatus.diskCovers[1].children('.album').attr('src', playlist[PlayerStatus.currentIndex].img);
            PlayerStatus.diskCovers[2].children('.album').attr('src', playlist[nextIndex].img);
        },
        animationEnd = function() {
            if (!PlayerStatus.songUpdated) {
                updateAlbumImgs();
                PlayerStatus.updateSong();
                PlayerStatus.songUpdated = true;
            }
        },
        albumStopRotate = function() {
            PlayerStatus.changeAnimationState(PlayerStatus.diskCovers[0], 'paused');
            PlayerStatus.changeAnimationState(PlayerStatus.diskCovers[2], 'paused');
        };

    if (derection === 1) {
        PlayerStatus.songUpdated = false;
        temp = PlayerStatus.diskCovers[0];
        PlayerStatus.diskCovers[0] = PlayerStatus.diskCovers[1];
        PlayerStatus.diskCovers[1] = PlayerStatus.diskCovers[2];
        PlayerStatus.diskCovers[2] = temp;

        albumStopRotate();

        if (preLoad) {
            PlayerStatus.diskCovers[1].children('.album').attr('src', defualtUrl);
        }

        PlayerStatus.diskCovers[2].css('left', posRight);
        PlayerStatus.diskCovers[1].animate({ left: posCenter }, speed, animationEnd);
        PlayerStatus.diskCovers[0].animate({ left: posLeft }, speed, animationEnd);
    } else if (derection === -1) {
        PlayerStatus.songUpdated = false;
        temp = PlayerStatus.diskCovers[2];
        PlayerStatus.diskCovers[2] = PlayerStatus.diskCovers[1];
        PlayerStatus.diskCovers[1] = PlayerStatus.diskCovers[0];
        PlayerStatus.diskCovers[0] = temp;

        albumStopRotate();
        PlayerStatus.diskCovers[0].css('left', posLeft);
        PlayerStatus.diskCovers[1].animate({ left: posCenter }, speed, animationEnd);
        PlayerStatus.diskCovers[2].animate({ left: posRight }, speed, animationEnd);
    } else {
        PlayerStatus.songUpdated = true;
        PlayerStatus.diskCovers[0].css('left', posLeft).show();
        PlayerStatus.diskCovers[1].css('left', posCenter).show();
        PlayerStatus.diskCovers[2].css('left', posRight).show();
        updateAlbumImgs();
    }

};

PlayerStatus.next = function() {
    if (PlayerStatus.songUpdated) {
        PlayerStatus.currentIndex = PlayerStatus.currentIndex < playlist.length - 1 ? PlayerStatus.currentIndex + 1 : 0;
        PlayerStatus.preSwitchSong();
        setTimeout('PlayerStatus.updateCoverState(1)', PlayerStatus.isPlaying ? 400 : 0);
    }
};

PlayerStatus.prev = function() {
    if (PlayerStatus.songUpdated) {
        PlayerStatus.currentIndex = PlayerStatus.currentIndex > 0 ? PlayerStatus.currentIndex - 1 : playlist.length - 1;
        PlayerStatus.preSwitchSong();
        setTimeout('PlayerStatus.updateCoverState(-1)', PlayerStatus.isPlaying ? 400 : 0);
    }
};

PlayerStatus.loop = function() {
    PlayerStatus.singleLoop = !PlayerStatus.singleLoop;
    $('#controls .loop-btn').toggleClass('active');
};

PlayerStatus.showPlayList = function() {
    PlayerStatus.$playList.animate({ bottom: '0px' }, 200);
};

PlayerStatus.hidePlayList = function() {
    PlayerStatus.$playList.animate({ bottom: -PlayerStatus.$playList.height() + 'px' }, 200);
};

PlayerStatus.init = function() {
    PlayerStatus.initData();
    PlayerStatus.initState();
    PlayerStatus.initPlayList();
    PlayerStatus.updateSong();
    PlayerStatus.setInterval();
    PlayerStatus.initProcessBtn(PlayerStatus.$processBtn);
    PlayerStatus.updateCoverState(0);
};

PlayerStatus.initData = function() {
    PlayerStatus.player = $('#player').get(0);
    PlayerStatus.$needle = $('#needle');
    PlayerStatus.$curTime = $('#currentTime');
    PlayerStatus.$totTime = $('#totalTime');
    PlayerStatus.$processBtn = $('#processBtn');
    PlayerStatus.$processBar = $('#process .process-bar');
    PlayerStatus.$rdyBar = $('#process .rdy');
    PlayerStatus.$curBar = $('#process .cur');
    PlayerStatus.$playBtn = $('#controls .play');
    PlayerStatus.$pauseBtn = $('#controls .pause');
    PlayerStatus.$playList = $('#playList');
    PlayerStatus.$listContent = $('#listContent');
    PlayerStatus.diskCovers = [$('.disk-cover:eq(0)'), $('.disk-cover:eq(1)'), $('.disk-cover:eq(2)')];
};
/**
 * 转到歌曲index
 * @param  {number} index 歌曲标号
 */
PlayerStatus.moveTo = function(index) {
    if (PlayerStatus.songUpdated) {
        PlayerStatus.currentIndex = index;
        PlayerStatus.preSwitchSong();
        setTimeout('PlayerStatus.updateCoverState(1,true)', PlayerStatus.isPlaying ? 400 : 0);
    }
};

PlayerStatus.initState = function() {
    $('img').attr('draggable', false);
    PlayerStatus.player.addEventListener('ended', function() {
        if (PlayerStatus.singleLoop) {
            PlayerStatus.moveTo(PlayerStatus.currentIndex);
        } else {
            PlayerStatus.next();
        }
    });
    PlayerStatus.player.addEventListener('canplay', PlayerStatus.readyToPlay); /**/
    window.addEventListener('resize', PlayerStatus.updateCoverState);
    $("body").on('click touch', function(e) {
        if ($(e.target).parents('#playList').length === 0 && !$(e.target).hasClass('list-btn')) {
            PlayerStatus.hidePlayList();
        }
    });
};

PlayerStatus.initPlayList = function() {
    var $li;
    PlayerStatus.$listContent.html('');
    $('#playListCount').html(playlist.length);
    $.each(playlist, function(i, item) {
        $li = $('<li>').html(item.songName).append($('<span>').html('   -' + item.artist));
        $li.on('click touch', function() {
            if (PlayerStatus.currentIndex !== i) {
                PlayerStatus.isPlaying = true;
                PlayerStatus.moveTo(i);
            }
        });
        PlayerStatus.$listContent.append($li);
    });
    PlayerStatus.validatePlayList();
    PlayerStatus.$playList.css('bottom', -PlayerStatus.$playList.height() + 'px');
};

/**
 * 生成时钟类型数字01，02..
 */
function validateTime(number) {
    var value = (number > 10 ? number + '' : '0' + number).substring(0, 2);
    return isNaN(value) ? '00' : value;
}

/**
 * 更新进度条
 */
PlayerStatus.updateProcess = function() {
    var buffer = PlayerStatus.player.buffered,
        bufferTime = buffer.length > 0 ? buffer.end(buffer.length - 1) : 0,
        duration = PlayerStatus.player.duration,
        currentTime = PlayerStatus.player.currentTime;
    PlayerStatus.$totTime.text(validateTime(duration / 60) + ":" + validateTime(duration % 60));
    PlayerStatus.$rdyBar.width(bufferTime / duration * 100 + '%');
    if (!PlayerStatus.processBtnState) {
        PlayerStatus.$curBar.width(currentTime / duration * 100 + '%');
        PlayerStatus.$curTime.text(validateTime(currentTime / 60) + ":" + validateTime(currentTime % 60));
    }
};

/**
 * 间隔性更新进度条
 */
PlayerStatus.setInterval = function() {
    if (!PlayerStatus.interval) {
        PlayerStatus.updateProcess();
        PlayerStatus.interval = setInterval(PlayerStatus.updateProcess, 1000);
    }
};
/**
 * 设置进度条圆点动作
 * @param  {button} $btn
 */
PlayerStatus.initProcessBtn = function($btn) {
    var moveFun = function(e) {
            var duration = PlayerStatus.player.duration,
                e = e.originalEvent,
                totalWidth = PlayerStatus.$processBar.width(),
                percent, moveX, newWidth;
            e.preventDefault();
            if (PlayerStatus.processBtnState) {
                moveX = (e.clientX || e.touches[0].clientX) - PlayerStatus.originX;
                newWidth = PlayerStatus.$curBar.width() + moveX;

                if (newWidth > totalWidth || newWidth < 0) {
                    PlayerStatus.processBtnState = 0;
                } else {
                    percent = newWidth / totalWidth;
                    PlayerStatus.$curBar.width(newWidth);
                    PlayerStatus.$curTime.text(validateTime(percent * duration / 60) + ":" + validateTime(percent * duration % 60));
                }
                PlayerStatus.originX = (e.clientX || e.touches[0].clientX);
            }
        },
        startFun = function(e) {
            e = e.originalEvent;
            PlayerStatus.processBtnState = 1;
            PlayerStatus.originX = (e.clientX || e.touches[0].clientX);
        },
        endFun = function() {
            if (PlayerStatus.processBtnState) {
                PlayerStatus.player.currentTime = PlayerStatus.$curBar.width() / PlayerStatus.$processBar.width() * PlayerStatus.player.duration;
                PlayerStatus.processBtnState = 0;
                PlayerStatus.updateProcess();
            }
        };
    $btn.on('mousedown touchstart', startFun);
    $("body").on('mouseup touchend', endFun);
    $("#process").on('mousemove touchmove', moveFun);
}
PlayerStatus.QuerySong = function() {
    var jud = 0;
    var inputValue = document.getElementById("qureySong").value;
    var result = document.getElementById("result");
    for (var i = 0; i < playlist.length; ++i) {
        if (playlist[i].songName == inputValue) {
            PlayerStatus.currentIndex = i;
            PlayerStatus.preSwitchSong();
            setTimeout('PlayerStatus.updateCoverState(1)', PlayerStatus.isPlaying ? 400 : 0);
            count = i;
            jud = 1;
            break;
        }
    }
    if (jud == 1) result.innerHTML = 'find';
    else result.innerHTML = 'not find';
}

$().ready(function() {
    PlayerStatus.init();
    $('.query .search').click(PlayerStatus.QuerySong);
    $('#controls .loop-btn').click(PlayerStatus.loop);
    $('#controls .pre').click(PlayerStatus.prev);
    PlayerStatus.$playBtn.click(PlayerStatus.play);
    PlayerStatus.$pauseBtn.click(PlayerStatus.pause);
    $('#controls .next').click(PlayerStatus.next);
    $('#controls .list-btn').click(PlayerStatus.showPlayList);
});