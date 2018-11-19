const PARTICIPANT_MAIN_CLASS = 'participant main';
const PARTICIPANT_CLASS = 'participant';
/**
 * Creates a video element for a new participant
 *
 * @param {String} name - the name of the new participant, to be used as tag
 *                        name of the video element.
 *                        The tag of the new element will be 'video<name>'
 * @return
 */
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
	video.poster = 'http://7xj61g.com1.z0.glb.clouddn.com/18a066e2-0c51-4455-8e72-69dfdd615a78.jpg';


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
				sender : name,
				sdpOffer : offerSdp
			};
		anchor.sendMessage(msg);
	}


	this.onIceCandidate = function (candidate, wp) {
		  console.log("Local candidate" + JSON.stringify(candidate));

		  var message = {
		    id: 'onIceCandidate',
		    candidate: candidate,
		    name: name
		  };
		  anchor.sendMessage(message);
	}

	Object.defineProperty(this, 'rtcPeer', { writable: true});

	this.dispose = function() {
		console.log('Disposing participant ' + this.name);
		this.rtcPeer.dispose();
		container.parentNode.removeChild(container);
	};
}	


function initAnchor(config){
	var ws = new WebSocket(config.ws_url);
	var destroy = false;
	window.onbeforeunload=function(){
		destroy = true;
		if(ws){			
			ws.close();
		}		
	};
	ws.onopen=function(even){//连接				
		joinRoomDerect(config);//连接成功加入回话
	}
	ws.onclose=function(even){
		if(!destroy){			
			if(confirm('与服务通讯出现问题,是否刷新页面')){
				window.location.reload();
			}
		}
	}
	ws.onerror=function(even){
		log.error("WebSocket 通信出现异常",even);
	}
	ws.onmessage = function(message){
		var parsedMessage = JSON.parse(message.data);
		console.info('Received message: ' + message.data);
	
		switch (parsedMessage.id) {
		case 'existingParticipants':
			onExistingParticipants(parsedMessage);
			break;
		case 'newParticipantArrived':
			onNewParticipant(parsedMessage);
			break;
		case 'participantLeft':
			onParticipantLeft(parsedMessage);
			break;
		case 'receiveVideoAnswer':
			receiveVideoResponse(parsedMessage);
			break;
		case 'closeRoom':
			for ( var key in participants) {
				if(participants[key]){
					participants[key].dispose();
				}
			}
			document.getElementById('join').style.display = 'block';
			document.getElementById('room').style.display = 'none';
			ws.close();
			break;
		case 'iceCandidate':
			participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
		        if (error) {
			      console.error("Error adding candidate: " + error);
			      return;
		        }
		    });
		    break;
		case "roomisnull":
			showError("该房间不存在");
		default:
			console.error('Unrecognized message', parsedMessage);
		}
	}
	
	function joinRoomDerect(config) {
		var message = {
			id : 'joinRoom',
			name : config.name,
			roomName : config.roomName,
			roomId: config.roomId
		}			
		sendMessage(message);
	}
		
	function onNewParticipant(request) {
		receiveVideo(request.name);
	}
		
	function receiveVideoResponse(result) {
		participants[result.name].rtcPeer.processAnswer(result.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}
		
	function callResponse(message) {
		if (message.response != 'accepted') {
			console.info('Call not accepted by peer. Closing call');
			stop();
		} else {
			webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
				if (error) return console.error (error);
			});
		}
	}
		
	function onExistingParticipants(msg) {
		var constraints = {
			audio : true,
			video : {
				mandatory : {
				maxWidth : config.maxWidth,
				minWidth : config.minWidth,
				maxFrameRate : config.maxFrameRate,
				minFrameRate : config.minFrameRate
				}
			}
		};
		console.log(config.name + " registered in room " + room);
		var participant = new Participant(config.name);
		participants[config.name] = participant;
		var video = participant.getVideoElement();
	
		var options = {
		      localVideo: video,
		      mediaConstraints: constraints,
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
		
	function leaveRoom() {
		sendMessage({
			id : 'leaveRoom'
		});
	
		for ( var key in participants) {
			participants[key].dispose();
		}
		document.getElementById('join').style.display = 'block';
		document.getElementById('room').style.display = 'none';	
		destroy=true;
		ws.close();
	}
		
	function receiveVideo(sender) {
		var participant = new Participant(sender);
		participants[sender] = participant;
		var video = participant.getVideoElement();
	
		var options = {
	      remoteVideo: video,
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
		
	function onParticipantLeft(request) {
		console.log('Participant ' + request.name + ' left');
		var participant = participants[request.name];
		participant.dispose();
		delete participants[request.name];
	}
		
	function sendMessage(message) {
		var jsonMessage = JSON.stringify(message);
		ws.send(jsonMessage);
		console.log(config.ws_url)
		console.log('Senging message: ' + jsonMessage);
	}
	
	/**
	* 展示错误信息
	*/
	function showError(msg){
		alert(msg);
	}	
	this.sendMessage = sendMessage;
	this.leaveRoom = leaveRoom;
}