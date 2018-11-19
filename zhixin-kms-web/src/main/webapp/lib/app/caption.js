/*******************************************************************************
 * @param videoContainer
 *            对应视频容器
 * @param text
 *            要更新的文本
 * @param append
 *            是否再原来的基础上追加
 */
function updateCaption(videoContainer, text, append) {
	var _caption = $(videoContainer).find(".caption");
	if (_caption.size() > 0) {
		if (!append) {
			_caption.empty();
		}
		$("<span/>").text(text).appendTo(_caption);
	}
}

function CodeRand() {
	var Num = "";
	for (var i = 0; i < 6; i++) {
		Num += Math.floor(Math.random() * 10);
	}
	return Num;
}