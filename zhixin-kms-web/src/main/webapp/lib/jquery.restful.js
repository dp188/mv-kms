/**
 * 对jQuery扩展出RestFul插件
 */

(function($) {
	$.REST = {
		settings : {
			headers : {},
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			async : true
		},
		/**
		 * 新增
		 */
		POST : function(options) {
			options.type="POST";
			this.innerAjax(options);
		},
		/**
		 * 修改(修改所有)
		 */
		PUT : function(options) {
			options.type="PUT";
			this.innerAjax(options);
		},
		/**
		 * 部分更新
		 */
		PATCH : function(options) {
			options.type="PATCH";
			this.innerAjax(options);
		},
		/**
		 * 查询
		 */
		GET : function(options) {
			options.type="GET";
			this.innerAjax(options);
		},
		/**
		 * 删除
		 */
		DELETE : function(options) { 
			BootstrapDialog.show({
				title:'警告',
				type:BootstrapDialog.TYPE_WARNING,
				closable: false,
	            message: '确认删除选中的记录？!',
	            buttons: [
	              {
	                label: '取消',
	                action: function(dialogItself){
	                    dialogItself.close();
	                }
	            }, {
	                label: '确认',
	                action:function(dialogItself){
	                	dialogItself.close();
	                	options.type="DELETE";
	        			$.REST.innerAjax(options);
	                }
	              }
	            ]
	        });
		},
		innerAjax : function(options) {
			var temp = $.extend({},this.settings);
			var opts = $.extend(temp, options);
			$.ajax({
				url : opts.url,
 				contentType : opts.contentType,
				dataType : opts.dataType,
				type : opts.type,
				data : opts.data,
				async : opts.async,
				success : function(data) {
					if(options.success){
						options.success(data.data);
					}else{
						alert(JSON.stringify(data.data));
					}
				},
				error : function(data) {
					if(data.readyState==0){ 
						result = {"success":false,"error":{"code":500,"message":"服务器连接失败！"},"message":"服务器连接失败！"};
					} 
					var result = null;
					try{
						result = JSON.parse(data.responseText);
					}catch(e){
						result = {"success":false,"error":{"code":data.status,"message":responseText},"message":responseText};
					}
					if(options.error){
						options.error(result);
					}
				}
			});
		}

	};
})(jQuery);