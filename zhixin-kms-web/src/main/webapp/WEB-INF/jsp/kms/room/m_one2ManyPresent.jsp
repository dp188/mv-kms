
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Mosaddek">
    <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.png">
     <jsp:include page="../include/styles.jsp"></jsp:include>
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/lib/kurento/kurento.css" />
     <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/lib/app/css/broadcast.css"/>
    <title>视频直播</title>
    <style type="text/css">
    	#videoBig{
    		height: auto !important;
    		min-height: 750px;    	
    	}
    	
    	.top_left{
    		    width: 150px;
			    display: block;
			    position: absolute;
			    top: 30px;
			    right: 0px;
			    z-index: 10;
			    text-align: right;
    	}
    	.participant video{
    		width:100% !important;    		
    	}
    	.participant span{
    		display: none;
    	}
 	    body{  
		    overflow-x: hidden;  
		    overflow-y: hidden;  
		}  
    </style>
   
  </head>
  <body>
	<div class="row" style="margin-left: 0px;margin-right: 0px;"> 		
		 <div class="col-md-12" style="padding-left: 0px;padding-right: 0px;">
			<div id="videoBig" class="vid-wrapper">
				<video id="video" autoplay controls="controls"></video>
				<div class="caption"></div>
			</div>
		</div>
		<!-- -----------音频--------------- -->
		<div id="room"  class="top_left">
			<div>
				<input type="button" class="btn" value="退出房间" onclick="stop()" style="display: none;">			 				
			</div>
			<div id="participants"></div>				
		</div>
		<!-- ----------- 音频 ------------- -->
		<div class="col-md-12" style="display:none;">
			<label>控制台输出</label>
			<div id="console" class="democonsole">
				<ul></ul>
			</div>
		</div>
	</div> 
 	<jsp:include page="../include/script.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/console.js"></script>
  	<script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/kurento-utils.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/kurento/adapter.js"></script>
	<script type="text/javascript">
	/*
	 * (C) Copyright 2014 Kurento (http://kurento.org/)
	 *
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the GNU Lesser General Public License
	 * (LGPL) version 2.1 which accompanies this distribution, and is available at
	 * http://www.gnu.org/licenses/lgpl-2.1.html
	 *
	 * This library is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
	 * Lesser General Public License for more details.
	 *
	 */

	const PARTICIPANT_MAIN_CLASS = 'participant main';
	const PARTICIPANT_CLASS = 'participant';
	var VIDEO_DEVICE_OPTIONAL = undefined;
	 
	var ws = new WebSocket( (location.href.startsWith("https")? "wss://":"ws://" ) + location.host + '${pageContext.request.contextPath}/one2Many');
	var video;
	var webRtcPeer;
	
	//此处是调整视频宽度,即是质量
	var maxWidth = ${maxWidth};
	var minWidth = ${minWidth};
	
	//每秒视频采集帧数
	var maxFrameRate = ${maxframeRate};
	var minFrameRate = ${minframeRate};
	
	//调整视频比例.
	var minAspectRatio =  ($(window).width()/$(window).height()).toFixed(2);
	var maxAspectRatio =  ($(window).width()/$(window).height()).toFixed(2);
	var roomId = "${roomId}";
	var my_name=roomId+"_"+CodeRand();
	window.onload = function() {
		console = new Console();
		video = document.getElementById('video');
		instruction.instruction(function(msg){
			console.info(msg);
		var data = JSON.parse(msg);
		switch(data.id){
			case 'userLeave':
				stop();
				break;
			case 'caption':
				uploadcaption(data.text);
				break;
			default:
				console.error('Unrecognized message', data);
		}
		});
		
		var message = {
                id : 'userData',
                userName : '?',
                userType : '?'
        };
        var jsString = JSON.stringify(message);
        instruction.sendmsg(jsString);
	}

	window.onbeforeunload = function() {
		dispose(); 
	}

	ws.onopen=function(even){//连接				
		presenter();//连接成功加入观看
	}
	ws.onclose=function(even)
	{
		
	}
	
	ws.onmessage = function(message) {
		var parsedMessage = JSON.parse(message.data);
		console.info('Received message: ' + message.data);

		switch (parsedMessage.id) {
		case 'presenterResponse':
			presenterResponse(parsedMessage);
			break;
		case 'viewerResponse':
			viewerResponse(parsedMessage);
			break;
		case 'iceCandidate':
			webRtcPeer.addIceCandidate(parsedMessage.candidate, function(error) {
				if (error)
					return console.error('Error adding candidate: ' + error);
			});
			break;
		case 'stopCommunication':
			dispose();
			break;
		case "roomisnull":
			showError("该房间不存在");
			break;			
		case "updateCaption":
			doUpdateCaption(parsedMessage);
			break;
			////////////音频通道	
		case 'existingParticipants':
			onExistingParticipants(parsedMessage);
			break;
		case 'newParticipantArrived':
			onNewParticipant(parsedMessage);
			break;
		case 'receiveVideoAnswer':
			receiveVideoResponse(parsedMessage);
			break;
		case 'rejectStartAudio':
			showError(parsedMessage.message);
			break;
		case 'participantLeft':
			onParticipantLeft(parsedMessage);
			break;
		case 'iceCandidate_Audio':
			participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
		        if (error) {
			      console.error("Error adding candidate: " + error);
			      return;
		        }
		    });
			break;
		case 'closeRoom_Audio':
			disposeAudioresource();
			break;
			////////////
		default:
			console.error('Unrecognized message', parsedMessage);
		}
	}
	
	function uploadcaption(text)
	{
		var message = {
				id : 'caption',
				text: text,
				append : false
			}
		sendMessage(message);
	}
	
	function doUpdateCaption(message){
		var _name = message.name;
		var _text = message.text;
		var _append = message.message
		updateCaption("#videoBig",_text,_append)
	}

	function presenterResponse(message) {
		if (message.response != 'accepted') {
			var errorMsg = message.message ? message.message : 'Unknow error';
			console.info('Call not accepted for the following reason: ' + errorMsg);
			dispose();
		} else {
			roomId  = message.roomId;
			webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
				if (error)
					return console.error(error);
			});
		}
	}

	function viewerResponse(message) {
		if (message.response != 'accepted') {
			var errorMsg = message.message ? message.message : 'Unknow error';
			console.info('Call not accepted for the following reason: ' + errorMsg);
			dispose();
		} else {
			webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
				if (error)
					return console.error(error);
			});
		}
	}

	function presenter() {
		//获取用户摄像头数.
		var mediaTrack = GetMediaTrack(function(mediaTrack){
    		if(mediaTrack && mediaTrack.video && mediaTrack.video.length > 1){    			
    			if(true/*confirm('检测到您有多个摄像头,是否选择第二个(可能是后置)摄像头')*/){
    				//选择第二个
    				VIDEO_DEVICE_OPTIONAL = [{
    					sourceId : mediaTrack.video[1].id
    				}];
    			}else{
    				//VIDEO_DEVICE_OPTIONAL = [{
    				//	sourceId : mediaTrack.video[0].id
    				//}];
    			}
    		} 
    		if (!webRtcPeer) {
    			showSpinner(video);
    			var options = {
    				localVideo : video,
    				mediaConstraints:{
    					video:{
    						mandatory:{
    							maxWidth : maxWidth,							
    							maxFrameRate : maxFrameRate,
    							minFrameRate : minFrameRate,
    							minAspectRatio: minAspectRatio,
    							maxAspectRatio: maxAspectRatio
    						}
    					}					
    				},				
    				onicecandidate : onIceCandidate
    			}
    			if(VIDEO_DEVICE_OPTIONAL){
    				options.mediaConstraints.video.optional = VIDEO_DEVICE_OPTIONAL;
    			}
    			webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
 					function(error) {
 						if (error) {
 							return console.error(error);
 						}
 						webRtcPeer.generateOffer(onOfferPresenter);
 					});
    		}
		}); 
	}

	function onOfferPresenter(error, offerSdp) {
		if (error)
			return console.error('Error generating the offer');
		console.info('Invoking SDP offer callback function ' + location.host);
		var message = {
			id : 'presenter',
			starttime: new  Date().getTime(),
			name: my_name,
			sdpOffer : offerSdp
		}
		sendMessage(message);
	}

	function viewer() {
		if (!webRtcPeer) {
			showSpinner(video);

			var options = {
				remoteVideo : video,
				onicecandidate : onIceCandidate
			}
			webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
					function(error) {
						if (error) {
							return console.error(error);
						}
						this.generateOffer(onOfferViewer);
					});
		}
	}

	function onOfferViewer(error, offerSdp) {
		if (error)
			return console.error('Error generating the offer');
		console.info('Invoking SDP offer callback function ' + location.host);
		var message = {
			id : 'viewer',
			sdpOffer : offerSdp
		}
		sendMessage(message);
	}

	function onIceCandidate(candidate) {
		console.log("Local candidate" + JSON.stringify(candidate));

		var message = {
			id : 'onIceCandidate',
			candidate : candidate
		};
		sendMessage(message);
	}

	function stop() {
		var message = {
			id : 'stop'
		}
		sendMessage(message);
		dispose();
	}

	function dispose() {
		if (webRtcPeer) {
			webRtcPeer.dispose();
			webRtcPeer = null;
		}
		if(ws != null){
			ws.close();
		} 
		hideSpinner(video);
		document.getElementById('video').style.display = 'none';
	}

	function sendMessage(message) {
		message.roomId = roomId;
		message.roomName="视频会议名称";
		
		var jsonMessage = JSON.stringify(message);
		console.log('Senging message: ' + jsonMessage);
		ws.send(jsonMessage);
	}
	

	function showSpinner() {
		for (var i = 0; i < arguments.length; i++) {
			arguments[i].poster = './img/transparent-1px.png';
			arguments[i].style.background = 'center transparent url("./img/spinner.gif") no-repeat';
		}
	}

	function hideSpinner() {
		for (var i = 0; i < arguments.length; i++) {
			arguments[i].src = '';
			arguments[i].poster = './img/webrtc.png';
			arguments[i].style.background = '';
		}
	}
	
////////////////////////////////////
	function sendMessage_Audio(message) {
		var jsonMessage = JSON.stringify(message);
		console.log('Senging message: ' + jsonMessage);
		ws.send(jsonMessage);
	}
	//开启音频通道
	function startAudio()
	{
		var message = {
				id : 'startAudio',
				name: my_name,
				roomId: roomId
			}
		sendMessage_Audio(message);
	}
	//停止
	function stopAudio()
	{
		var message = {
				id : 'stopAudio',
				name: my_name,
				roomId: roomId
			}
		sendMessage_Audio(message);
		disposeAudioresource();
	}
	//释放音频涉及的资源
	function disposeAudioresource()
	{
		for ( var key in participants) {
			if(participants[key]){
				participants[key].dispose();
			}		
		}
	}
	//接收SDP
	function receiveVideoResponse(result) {
		participants[result.name].rtcPeer.processAnswer(result.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}
	//存在的
	function onExistingParticipants(msg) {
		var constraints = {
			audio : true,
			video : false
			
		};
		console.log(my_name + " registered in room " + room);
		var participant = new Participant(my_name);
		participants[my_name] = participant;
		var video = participant.getVideoElement();
	
		var options = {
		      localVideo: video,
		      mediaConstraints: constraints,
		      receiveVideo:false,
		      onicecandidate: participant.onIceCandidate.bind(participant)
		    }
		participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
			function (error) {
			  if(error) {
				  if(error) {
						showError('无法打开视频设备,请检查授权');
						return console.error(error);
				  }
				  return console.error(error);
			  }
			  this.generateOffer(participant.offerToReceiveVideo.bind(participant));
		});
	
		msg.data.forEach(receiveVideo);
	}
	
	//有新的音频通道开启
	function onNewParticipant(request) {
		receiveVideo(request.name);
	}
	function receiveVideo(sender) {
		var participant = new Participant(sender);
		participants[sender] = participant;
		var video = participant.getVideoElement();
	
		var options = {
	      remoteVideo: video,
	      receiveVideo: false,
	      onicecandidate: participant.onIceCandidate.bind(participant)
	    }
	
		participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function (error) {
				if(error) {
					showError('无法打开视频设备,请检查授权');
					return console.error(error);
				}
				this.generateOffer (participant.offerToReceiveVideo.bind(participant));
			});
	}
	//音频通道关闭
	function onParticipantLeft(request) {
		console.log('Participant ' + request.name + ' left');
		var participant = participants[request.name];
		participant.dispose();
		delete participants[request.name];
	}
	
	function Participant(name) {
		this.name = name;
		var container = document.createElement('div');
		container.className = isPresentMainParticipant() ? PARTICIPANT_CLASS : PARTICIPANT_MAIN_CLASS;
		container.id = name;
		var span = document.createElement('span');
		var video = document.createElement('video');
		var caption = document.createElement('div');
		caption.className = "caption";
		var rtcPeer;

		container.appendChild(video);
		container.appendChild(span);
		container.appendChild(caption);
		container.onclick = switchContainerClass;
		document.getElementById('participants').appendChild(container);

		span.appendChild(document.createTextNode(name));

		video.id = 'video-' + name;
		video.autoplay = true;
		video.controls = false;
		video.poster = '${pageContext.request.contextPath}/images/voice.png';


		this.getElement = function() {
			return container;
		}

		this.getVideoElement = function() {
			return video;
		}

		function switchContainerClass() {
			if (container.className === PARTICIPANT_CLASS) {
				var elements = Array.prototype.slice.call(document.getElementsByClassName(PARTICIPANT_MAIN_CLASS));
				elements.forEach(function(item) {
						item.className = PARTICIPANT_CLASS;
					});

					container.className = PARTICIPANT_MAIN_CLASS;
				} else {
				container.className = PARTICIPANT_CLASS;
			}
		}

		function isPresentMainParticipant() {
			return ((document.getElementsByClassName(PARTICIPANT_MAIN_CLASS)).length != 0);
		}

		this.offerToReceiveVideo = function(error, offerSdp, wp){
			if (error) return console.error ("sdp offer error")
			console.log('Invoking SDP offer callback function');
			var msg =  { id : "receiveVideoFrom",
					sender : this.name,
					sdpOffer : offerSdp,
					name:  my_name,
				};
			sendMessage_Audio(msg);
		}


		this.onIceCandidate = function (candidate, wp) {
			  console.log("Local candidate" + JSON.stringify(candidate));

			  var message = {
			    id: 'onIceCandidate_Audio',
			    candidate: candidate,
			    name: this.name
			  };
			  sendMessage_Audio(message);
		}

		Object.defineProperty(this, 'rtcPeer', { writable: true});

		this.dispose = function() {
			console.log('Disposing participant ' + this.name);
			this.rtcPeer.dispose();		
			container.parentNode.removeChild(container);
		};
	}	

	////////////////////////////////////////
	/**
	* 展示错误信息
	*/
	function showError(msg){
		alert(msg);
	}
	/**
	 * Lightbox utility (to display media pipeline image in a modal dialog)
	 */
	$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
		event.preventDefault();
		$(this).ekkoLightbox();
	});
	</script> 
	 <script type="text/javascript">
	 $(function(){
 		var d=($("#videoBig").width()/$("#videoBig").height());
 		$("#videoBig").css(
 			"min-height",($(window).height()+15)+"px"
 		); 
 		$("#videoBig").css(
    			"min-width",($(window).width()+(15*d))+"px"
    		); 
 		window.scrollTo((15/2)*d,15/2);
 	});
    </script>
  </body>
</html>



