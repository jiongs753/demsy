/*
 * 字符串函数扩展
 */
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, '');
}
String.prototype.toProp = function() {
	var idx = this.indexOf("-");
	if (idx > -1) {
		var str = this.substring(0, idx) + this.substring(idx + 1, idx + 2).toUpperCase() + this.substring(idx + 2);
		return str.toProp();
	}

	return this;
}
/*
 * 扩展后支持参数传递的的定时调度函数
 */
var __sti = setInterval;
window.setInterval = function(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	__sti(_cb, timeout);
}

var __sto = setTimeout;
window.setTimeout = function(callback, timeout, param) {
	var args = Array.prototype.slice.call(arguments, 2);
	var _cb = function() {
		callback.apply(null, args);
	}
	__sto(_cb, timeout);
}
