(function(a){a.fn.serialAll=function(c,b){if(a.trim(c)=="serial"){return a.fn.serialAll.methods.serial(this)}else{if(a.trim(c)=="object"){return a.fn.serialAll.methods.object(this,b)}else{if(a.trim(c)=="load"){if(a.isPlainObject(b)){a.fn.serialAll.methods.loadObj(this,b)}else{return a.fn.serialAll.methods.loadUrl(this,b)}}else{if(a.trim(c)=="clear"){a.fn.serialAll.methods.clear(this)}}}}};a.fn.serialAll.methods={serial:function(f){var d="{";var b=a(f).find(":text,:password,:radio,textarea,:hidden,select").not("[unserial='true']").serializeArray();a.each(b,function(j,k){d+='"'+k.name+'":"'+k.value+'",'});var e=a(f).find(":checkbox[unselectvalue]").not("[unserial='true']");a.each(e,function(){if(a(this).prop("checked")){d+='"'+a(this).attr("name")+'":"'+a(this).attr("value")+'",'}else{d+='"'+a(this).attr("name")+'":"'+a(this).attr("unselectvalue")+'",'}});var g=a(f).find(":checkbox").not("[unserial='true'],[unselectvalue]").serializeArray();var h=a(g).size();if(h>0){d+='"'+a(g).get(0).name+'":['}else{var c=d.lastIndexOf(",");d=d.substring(0,c)}a.each(g,function(j,l){var k=a(g).get(j+1);if(k&&l.name==k.name){d+='{"'+l.name+'":"'+l.value+'"},'}else{if(k&&l.name!=k.name){d+='{"'+l.name+'":"'+l.value+'"}],"'+k.name+'":['}else{if(!k){d+='{"'+l.name+'":"'+l.value+'"}]'}}}});d+="}";return d},object:function(e,g){var c=new Object();var b=a(e).find(":text,:password,:radio,textarea,:hidden,select").not("[unserial='true']").serializeArray();a.each(b,function(j,k){if(a(e).find("[name='"+k.name+"']").attr("formatType")=="number"){c[k.name]=a.string2Number(k.value)}else{c[k.name]=typeof(escapeHTML)=="function"?escapeHTML(k.value):k.value}});var f=a(e).find(":checkbox").not("[unserial='true'],[unselectvalue]").serializeArray();var h=new Array();a.each(f,function(k,m){var l=a(f).get(k+1);if(g=="noarray"){if(l&&m.name==l.name){h.push(m.value)}else{if(l&&m.name!=l.name){h.push(m.value);c[m.name]=h.join(",");h=[]}else{if(!l){h.push(m.value);c[m.name]=h.join(",")}}}}else{if(l&&m.name==l.name){var j=new Object();j[m.name]=m.value;h.push(j)}else{if(l&&m.name!=l.name){var j=new Object();j[m.name]=m.value;h.push(j);c[m.name]=h;h=[]}else{if(!l){var j=new Object();j[m.name]=m.value;h.push(j);c[m.name]=h}}}}});var d=a(e).find(":checkbox[unselectvalue]").not("[unserial='true']");a.each(d,function(){if(a(this).prop("checked")){c[a(this).attr("name")]=a(this).attr("value")}else{c[a(this).attr("name")]=a(this).attr("unselectvalue")}});return c},clear:function(b){a(b).find(":text,:password,textarea,input:hidden").val("");a(b).find("select").each(function(){a(this).find("option").first().attr("selected",true)});a(b).find(":radio").attr("checked",false);a(b).find(":checkbox").attr("checked",false)},loadObj:function(c,b){a(c).find(":input,textarea").not(":checkbox,:radio,[unfillback='true']").each(function(){var d=a(this).attr("name");if(d&&typeof(b[d])!="undefined"){a(this).val(typeof(decodeHTML)=="function"?decodeHTML(b[d]):b[d])}});a(c).find("select").not("[unfillback='true']").each(function(){var e=a(this).attr("name");if(e&&typeof(b[e])!="undefined"){a(this).children().removeAttr("selected");var d=typeof(decodeHTML)=="function"?decodeHTML(b[e]):b[e];a(this).children("[value='"+d+"']").attr("selected","selected")}});a(c).formatForm();a(c).find(":radio").not("[unfillback='true']").each(function(){var d=a(this).attr("name");if(d&&typeof(b[d])!="undefined"&&b[d]==a(this).attr("value")){a(this).attr("checked",true);if(a(this).parent().is("span.radio")){a(this).parent().addClass("radio--checked")}}else{a(this).prop("checked",false);if(a(this).parent().is("span.radio")){a(this).parent().removeClass("radio--checked")}}});a(c).find(":checkbox").not("[unfillback='true']").each(function(){for(var e in b){if(a(this).attr("name")==e){var d=b[e];if(d){if(jQuery.isArray(d)){a.each(d,function(j,h){for(var g in h){a("input[name='"+g+"'][value='"+h[g]+"']").attr("checked",true)}})}else{var f=a(this);a.each(d.split(","),function(g,h){if(f.val()==h){f.prop("checked",true);if(f.parent().is("span.checkbox")){f.parent().addClass("checkbox--checked")}}else{f.prop("checked",false);if(f.parent().is("span.checkbox")){f.parent().removeClass("checkbox--checked")}}})}}}}})},loadUrl:function(d,b){var c=new Object();a.ajax({type:"POST",url:b,dataType:"json",async:false,cache:false,success:function(e){a.fn.serialAll.methods.loadObj(d,e);c=e}});return c}};a.fn.formatForm=function(){var b=a(this).find("[formatter]");a.each(b,function(){if(a.trim(a(this).val())!=""){var d=a(this).val().split("");var j="";a.each(d,function(){var i=/[0-9]|\./;if(this!=""&&i.exec(this)){j+=this.match(i)}});var m=a(this).attr("formatter");var c=m;if(m.indexOf("H")==0){c=c.replace("HH",j.substring(0,2)==""?"00":j.substring(0,2));c=c.replace("mm",j.substring(2,4)==""?"00":j.substring(2,4));c=c.replace("ss",j.substring(4,6)==""?"00":j.substring(4,6))}else{if(m.indexOf("yy")!=-1){c=c.replace("yyyy",j.substring(0,4));c=c.replace("MM",j.substring(4,6));c=c.replace("dd",j.substring(6,8));c=c.replace("HH",j.substring(8,10)==""?"00":j.substring(8,10));c=c.replace("mm",j.substring(10,12)==""?"00":j.substring(10,12));c=c.replace("ss",j.substring(12,14)==""?"00":j.substring(12,14))}}if(m.indexOf("###,###")!=-1){var e=j.split(".")[0].split("");var k=[];a.each(e,function(){if(this!=""){k.push(this)}});k.reverse();e=[];a.each(k,function(o){if(o!=0&&o%3==0){e.push(this+",")}else{e.push(this)}});e.reverse();var n="";a.each(e,function(){n+=this});c=c.replace("###,###",n)}if(m.indexOf(".#")!=-1){var l=m.split(".")[1];var g=l.lastIndexOf("#")+1;if(j.split(".")[1]){if(j.split(".")[1].length>=g){c=c.replace(l.substring(0,g),j.split(".")[1].substring(0,g))}else{c=c.replace(l.substring(0,g),j.split(".")[1]);var h="";for(var f=0;f<(g-j.split(".")[1].length);f++){h+="0"}c+=h}}else{var h="";for(var f=0;f<g;f++){h+="0"}c=c.replace(l.substring(0,g),h)}}a(this).val(c)}})}})(jQuery);