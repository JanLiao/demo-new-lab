
//------------------设置鼠标右键的菜单， 采用smartMenu插件--------------------------
var data = [[ {text: "设为该行首张",
             func: function() {
                    set_image_status("first_picture", $(this).attr("id"));
                }
             },

            {text: "设为该行尾张",
             func: function() {
                    set_image_status("end_picture", $(this).attr("id"));
                }
            },
           ],

            [{text: "删除",
              func: function() {
                    set_image_status("delete", $(this).attr("id"));
                }
            }]
           ];


var hello = "hello,lz";

function showhello(){
   alert(hello);
}
