//*********************加载配置js_config.js************************************
var new_element=document.createElement("script");
new_element.setAttribute("type","text/javascript");
new_element.setAttribute("src","../../layui/layui.js");// 在这里引入了a.js
document.body.appendChild(new_element);

//*********************ajax的公共方法*******************************************
function ajax_http_request(url, async, dataType, request_data){
  var  result = "";
  $.ajax({
    type:'POST',
    async:async, //true or false
    url: url,
    dataType: dataType, //json or string
    data: request_data,

    success: function(data) {
       result = data;
    }
  });
  return result;
}

String.prototype.replaceAll  = function(s1,s2){
    return this.replace(new RegExp(s1,"gm"),s2);
}

//===========================定义全局变量============================================
var currentImgId = 0;
var mouse_position; //鼠标当前位置

//图片缩放方法
var zoom_ratio = 1.0;
var canvas_zoom_ratio = 1.0
var winWidth = window.innerWidth * 0.6;
var my_canvas_data;
var myoImg ;

// 图片旋转角度
var current_image_angle = 0;

//方框颜色显示
var color_flag = 0;

//================================================
var contextMenuItems;

var canvas = new fabric.Canvas('bigImgCanvas');
canvas.on({
'object:moving': onChange,
'object:scaling': onChange,
'object:rotating': onChange,
'mouse:wheel':zoomImage,
});


function onChange(options) {
    options.target.setCoords();

    canvas.forEachObject(function(obj) {
      if (obj === options.target) {
          return;
      }
      obj.set('opacity' ,options.target.intersectsWithObject(obj) ? 0.5 : 1);

});}



//--------------------------------------------------------------------------
$(document).ready(function(){
   var image_id = $("#imgid").html();
    if(image_id == "0" || image_id== 0){
        alert("无数据!");
        return;
    }

    var data = $("#ldata").html();
    var objects ;
    if(data != ""){
        data = data.replaceAll("u'{", "{");
        data = data.replaceAll("}'", "}");
        data = data.replaceAll("u'", "'");
        data = data.replaceAll("'", "\"");
        var objects = JSON.parse(data);
        my_canvas_data = objects.canvas_data;
        zoom_ratio = objects.zoom_ratio;

    }
    layui.use('layer', function(){
		var layer = layui.layer;
		//layer.msg("000000", {icon:6, time:9000});
	});
    app.getCurrImg();

    layer.msg("111", {icon:6, time:9000});
    update_canvas("");

    //在canvas上层对象上添加右键事件监听
    $(".upper-canvas").contextmenu(onContextmenu);

    //初始化右键菜单
    $.contextMenu({
          selector: '#contextmenu-output',
          trigger: 'none',
          build: function($trigger, e) {
              //构建菜单项build方法在每次右键点击会执行
              return {
                  callback: contextMenuClick,
                  items: contextMenuItems
              };
          }
    });

    if (objects != undefined ) {
        draw_circle(objects);
    }


    $("#ldata").val("");

});


function draw_circle(objects){
   for (var i = objects.label_data.length - 1; i >= 0; i--) {
           var obj = objects.label_data[i];
           var circle = new fabric.Circle(obj["data"]);
           circle.name = obj["name"];
       canvas.add(circle);
   }
}


function update_canvas(url_image){
   //var url_image = "data_img/" + $("#imgurl").html();
   var url_image = $("#imgurl").html();
    if (my_canvas_data != undefined ){
    	layui.use('layer', function(){
    		var layer = layui.layer;
    		layer.msg("Canvas", {icon: 6, time: 7000});
    	});
           //zoom_ratio = zoom_ratio+0.12;
           // 设置canvas背景
           fabric.Image.fromURL(url_image, function(oImg) {
              // canvas.setHeight(my_canvas_data.h) ;
              // canvas.setWidth( my_canvas_data.w);
               oImg.scale(zoom_ratio).set('flipX', true);
              canvas.setHeight(oImg.width * zoom_ratio);
              canvas.setWidth(oImg.height * zoom_ratio);
              canvas.setBackgroundImage(oImg);
              myoImg = oImg;
            });

    }
    else {
    	layui.use('layer', function(){
    		var layer = layui.layer;
    		
    	});
        // 设置canvas背景 window.innerWidth
        fabric.Image.fromURL(url_image, function (oImg) {
            zoom_ratio = 1.0 * winWidth / oImg.width;
            zoom_ratio = zoom_ratio - 0.12;
            canvas.setHeight(oImg.width * zoom_ratio);
            canvas.setWidth(oImg.height * zoom_ratio);
            var ms = "" + zoom_ratio + " , " + oImg.width + " , " + oImg.height + " , " + winWidth;
            //layer.msg(ms, {icon: 6, time: 15000});

            oImg.scale(zoom_ratio).set('flipX', true);
            canvas.setBackgroundImage(oImg);
            myoImg = oImg;
        });
    }

    $("body").mLoading("hide");
}


// 添加圆形框
function addCircle(circle_type){

    var circle = new fabric.Circle({
        radius: 50, left: mouse_position.x-80, top: mouse_position.y-80, fill: ''
    });

    if (circle_type == 1) {
        circle.set({strokeWidth: 2, stroke: 'rgba(51, 171, 160, 1)'});
        circle.name = "shipan"; //视盘
        //fabric.Object.prototype.name = "" ;
    }
    else {
        circle.set({strokeWidth: 2, stroke: 'rgba(0, 0, 255, 1)'});
        circle.name = "shibei"; //视杯
    }
    canvas.add(circle);
}


//右键点击事件响应
var onContextmenu = function(event) {
    var pointer = canvas.getPointer(event.originalEvent);
    var objects = canvas.getObjects();

    var select_flag = 0;

    for (var i = objects.length - 1; i >= 0; i--) {
      var object = objects[i];
       //判断该对象是否在鼠标点击处

       if (canvas.containsPoint(event, object)) {
           //选中该对象
           canvas.setActiveObject(object);
           //显示菜单
           select_flag = 1;
           showContextMenu(event, object, 1);
           continue;
       }
    }
    if(select_flag == 0) {
        showContextMenu(event, object, 0);
    }

    //阻止系统右键菜单
    event.preventDefault();
    return false;
}



//右键菜单项点击
function showContextMenu(event, object, select_flag) {
    //定义右键菜单项
    if(select_flag == 0)
    {
        object = "";
    }

    contextMenuItems = {"delete": {name: "删除", icon: "delete", data: object},
                         "add1": {name: "添加视盘", icon: "add", data: object},
                         "add2": {name: "添加杯盘", icon: "add", data: object},};

    //右键菜单显示位置
    mouse_position = {
            x: event.clientX,
            y: event.clientY};

    $("#contextmenu-output").contextMenu(mouse_position);
}

//右键菜单项点击
function contextMenuClick(key, options) {

    if (key == "delete") {
        //得到对应的object并删除
        var object = contextMenuItems[key].data;
        if (object == "") {
            alert("请先选择需要删除的对象");
        }
        else {
            canvas.remove(object);
        }
    }
    else if (key == "add1") {
        if(check_label("shipan")){
            addCircle(1);
        }
    }
    else if (key == "add2") {
        if(check_label("shibei")){
            addCircle(2);
        }
    }
}


function check_label(labe_type) {
    var objects = canvas.getObjects();
    if (objects.length == 2){
        alert("无法添加!\n注意: 有且只能有1个视盘和1个杯盘标注.");
        return false;
    }

    for (var i = objects.length - 1; i >= 0; i--) {
      var obj = objects[i];

      if(obj.name == labe_type){
         alert("无法添加!\n注意: 有且只能有1个视盘和1个杯盘标注.");
         return false;
      }
    }
    return true;
}

//=====================缩放============================
function  zoomImage(options) {
    var zoomDelta = options.e.wheelDelta;

    //chrome
    if(zoomDelta == 120 || zoomDelta == -120){
        canvas_zoom_ratio += (zoomDelta/120)*0.1;

    }//firefox
    else if(zoomDelta == 300 || zoomDelta == -300){
     canvas_zoom_ratio += (zoomDelta/3000)*0.1;
    }
    canvas.setZoom(canvas_zoom_ratio);
    
}

//=========================移动===============================
//      left: 37
//      up: 38
//      right: 39
//      down: 40

function moveCanvas(direction_key) {
    var x = 0;
    var y = 0;
    var step = 10;
    if (direction_key == 37) {
        x -= step;
    } else if (direction_key == 39) {
        x += step;
    } else if (direction_key == 38) {
        y -= step;
    } else if (direction_key == 40) {
        y += step;
    }

    var delta = new fabric.Point(x, y);
    canvas.relativePan(delta);
}

//------------------响应按键事件-------------------------------------------------------------------
$(document).keydown(function (event) {
    /*
     按键的ascii值：
     delete: 46
     left: 37
     up: 38
     right: 39
     down: 40
     ctrl: 17

     tab: 9
     s:83
     r:82
     e:69
     0:48

     */
    //alert(event.keyCode);
    var kc = event.keyCode;

    var explorer = navigator.userAgent;
    //ie
    if (explorer.indexOf("MSIE") >= 0) {
    }
    //firefox
    else if (explorer.indexOf("Firefox") >= 0) {
        if (kc == 61) {
            kc = 187;
        }
        else if (kc == 173) {
            kc = 189;
        }
    }
    //Chrome
    else if (explorer.indexOf("Chrome") >= 0) {
    }


    //alert(kc);

    // 同时按下组合键的时候，这几个属性会变成true
    var ctrlKey = event.ctrlKey || event.metaKey;

    if (kc == 37 || kc ==38 || kc == 39 || kc == 40) {
        moveCanvas(kc);
    }

        // Ctrl + S 的事件响应
    if(event.ctrlKey && kc == 83) {

     if(confirm("需要保存当前数据吗?")){
           func_save_and_next();
      }
      else{

      }
    return;


            // 以下是阻止浏览器响应该事件
            event.returnValue=false;
            return false;
     }

});




//save data
function func_save_and_next(){
    func_save_label();
    //location.href = "/label";
}

function func_save_label(){
        var image_id = $("#imgid").html();
    var objects = canvas.getObjects();

    if(image_id == "0" || image_id== 0){
        alert("当前没有需要保存的数据!");
        return;
    }

    if (objects.length != 2){
        alert("数据标注错误, 请检查!\n注意: 有且只能有1个视盘和1个杯盘标注.");
        return;
    }

    var select_flag = 0;

    var li_result = new Array(2);

    for (var i = objects.length - 1; i >= 0; i--) {
      var obj = objects[i];
      var result = JSON.stringify(obj.toJSON());
      result["name"] = obj.name;
      li_result[i] = {"name:":obj.name, "data":result};
    }
    layer.msg(JSON.stringify(objects[0].toJSON()), {icon:6, time:15000});

    canvas_data = {"w":parseInt(canvas.getWidth()), "h":parseInt(canvas.getHeight())}

    var url = "/label";

    var data = {
        "label_data":li_result,
        "user_id":$("#uid").html(),
        "package_id":$("#pid").html(),
        "image_id":$("#imgid").html(),
        "task_id":$("#task_id").html(),
        "batch_id":$("#bid").html(),
        "canvas_data":canvas_data,
        "zoom_ratio":zoom_ratio
    };
    
    
    //result = ajax_http_request(url, false, "json", JSON.stringify(data))
}


function func_get_last(){
    if(confirm("需要保存当前数据吗?")){
        func_save_label();
      }
      else{

      }
    return;
    location.href = "/label?label_id=-1";
}