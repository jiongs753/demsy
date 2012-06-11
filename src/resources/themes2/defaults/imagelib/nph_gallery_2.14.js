(function(window, undefined){
	  
if (window.nph){
	return; 
}

var NTES = window.NTES,

	nph = {},
	
	rfocusable = /^(?:button|input|object|select|textarea)$/i;
	
function addStylesheetRules (rules) {   
	var style = document.createElement('style');   
	
	NTES.one('body').appendChild(style);   
	if (!window.createPopup) { /* For Safari */  
	   style.appendChild(document.createTextNode(''));   
	}
	var s = document.styleSheets[document.styleSheets.length - 1];   
	for (var selector in rules) {   
		if (s.insertRule) {   
			s.insertRule(selector + '{' + rules[selector] + '}', s.cssRules.length);   
		}   
		else { /* IE */  
			s.addRule(selector, rules[selector], -1);   
		}   
	}   
}  
	
var photoViewMode = {
	0: function (index) {
		var t = this;
		return {
			prev: index > 0 ? '#p=' + t.photoInfo[index - 1].id : '#p=' + t.photoInfo[t.size - 1].id,
			next: index < t.size - 1 ? '#p=' + t.photoInfo[index + 1].id : '#p=' + t.photoInfo[0].id
		};
	},
	
	1: function (index) {
		var t = this;
		return {
			prev: index > 0 ? '#p=' + t.photoInfo[index - 1].id : t.$prevSet.href,
			next: index < t.size - 1 ? '#p=' + t.photoInfo[index + 1].id : t.$nextSet.href
		};
	},
	
	2: function (index) {
		var t = this;
		return {
			prev: index > 0 ? '#p=' + t.photoInfo[index - 1].id : '#p=' + t.photoInfo[t.size - 1].id,
			next: index < t.size - 1 ? '#p=' + t.photoInfo[index + 1].id : 'javascript:nph.show(\'#' + t.$photoLayout.id + '\')'
		};
	}
};

nph.show = function (el) {
	NTES(el).addCss({ display: 'block' });
}

nph.hide = function (el) {
	NTES(el).addCss({ display: 'none' });
}

nph.Gallery = function (options) { 
	var t = this,
		sn = options.sn || '',
		ids = ['gallery', 'galleryTpl', 'galleryRelat', 'modePhoto', 'modeStream', 'modeSearch', 'setInfo', 'photoType', 'streamType', 'viewOrig', 'viewStream', 'viewPhoto', 'scrl', 'bar', 'thumb', 'scrlPrev', 'scrlNext', 'photo', 'photoIndex', 'photoDesc', 'photoView', 'photoPrev', 'photoNext', 'photoLoading', 'stream', 'search' ,'searchInput', 'searchPhoto', 'searchTheme', 'prevSet', 'nextSet', 'photoList', 'photoLayout'],
		i = ids.length;
	
	t.turn = photoViewMode[isNaN(options.photoViewMode) ? 0 : options.photoViewMode];
	
	while(--i >= 0){
		t['$' + ids[i]] = NTES('#' + ids[i] + sn);
	}
	
	t.photoIndex = {};
	t.photoInfo = [];
	t.streamSize = {};
	
	t.$thumb.attr('innerHTML', t.$photoList.value);
	t.$thumbs = t.$thumb.NTES('> li');
	t.size = t.$thumbs.length;
	
	var pWidth = t.$photoView.offsetWidth,
		pHeight = t.$photoView.offsetHeight;
		uWidth = 106,
		tWidth = uWidth * t.size,
		tCntWidth = t.$thumb.parentNode.offsetWidth,
		tNum = Math.floor(tCntWidth / uWidth);
	
	tCntWidth = uWidth * tNum;
	t.$photoView.addCss({ width: pWidth + 'px' });
	t.$thumb.addCss({width: tWidth + 'px'});
	t.$scrl.addCss({ width: (tCntWidth + 60) + 'px' });
	
	var bCntWidth = t.$bar.parentNode.offsetWidth,
		bWidth = Math.max(36, Math.min(tCntWidth * bCntWidth / tWidth, bCntWidth));
	
	t.$bar.addCss({width: bWidth + 'px'});
	
	t.$thumbs.each(function(i) {
		var self = this,
			id = t.parseObj(NTES.one('> a', self).href).p,
			mult = Math.max(0, Math.min(i - Math.floor((tNum + 1) / 2) + 1, t.size - tNum));
			
		t.photoInfo.push({
			id: id,
			title: NTES.one('> h2', self).attr('innerHTML'),
			desc: NTES.one('> p', self).attr('innerHTML'),
			img: NTES.one('> i[title=img]', self).attr('innerHTML'),
			timg: NTES.one('> i[title=timg]', self).attr('innerHTML'),
			aimg: NTES.one('> i[title=aimg]', self) ? NTES.one('> i[title=aimg]', self).attr('innerHTML') : '',
			pos: uWidth * mult
		});
		t.photoIndex[id] = i;
	}); 
	
	t.bar = new NTES.ui.Scroll(t.$thumb, 'x', t.$bar);
	
	NTES([t.$scrlPrev, t.$scrlNext, t.$bar]).addEvent('click', function(ev){ ev.preventDefault(); });
	t.$scrlPrev.addEvent('mousedown', t.bar.start.bind(t.bar, 'forward')).addEvent('mouseup,mouseout', t.bar.stop.bind(t.bar));
	t.$scrlNext.addEvent('mousedown', t.bar.start.bind(t.bar, 'backward')).addEvent('mouseup,mouseout', t.bar.stop.bind(t.bar));
	
	NTES([t.$photoPrev, t.$photoNext].concat(t.$thumbs.NTES('> a'))).addEvent('click', function(ev){
		var oHash = t.parseObj(this.href);
		
		if(oHash.p){
			ev.preventDefault();	
			
			t.showPhoto(oHash.p).changeHash('p=' + oHash.p).stats();
		}
	});
	
	!sn && NTES(document).addEvent('keydown', function (ev) {
		if (!rfocusable.test(ev.target.nodeName)) {
			switch (ev.keyCode) {
				case 37:
					var href = t.$photoPrev.attr('href'),
						oHash = t.parseObj(href);
					
					if (oHash.p) {
						t.showPhoto(oHash.p).changeHash('p=' + oHash.p).stats();
					} else {
						window.location.href = href;
					}
					break;
				case 39:
					var href = t.$photoNext.attr('href'),
						oHash = t.parseObj(href);
					
					if (oHash.p) {
						t.showPhoto(oHash.p).changeHash('p=' + oHash.p).stats();
					} else {
						window.location.href = href;
					}
					break;
			}
		}
	});
			
	t.$photo.addEvent('load', function(){
		'string' !== typeof this.style.maxWidth && t.resize(this, { width: pWidth, height: pHeight });
		t.$photoLoading.addCss('hidden');
		
		var oHash = t.parseObj(t.$photoNext.href);

		if(oHash.p){
			(new Image()).src = t.photoInfo[t.photoIndex[oHash.p]].img;
		}
	});
	
	if(t.$searchPhoto || t.$viewStream){
		var sHeight = pHeight + 130,
			rules = {};
	
		t.streamSize.cols = Math.floor(pWidth / 180);
		t.streamSize.rows = Math.floor(sHeight / 215);
		t.streamSize.size = t.streamSize.cols * t.streamSize.rows;
		t.total = Math.ceil(t.size / t.streamSize.size);
	
		rules[String.format('#gallery%1 .nph_list_stream', sn)] = String.format('width:%1px;height:%2px;', pWidth, sHeight);
		rules[String.format('#gallery%1 .nph_list_stream li', sn)] = String.format('width:%1px;height:%2px;', Math.floor(pWidth / t.streamSize.cols), Math.floor(sHeight / t.streamSize.rows));
		addStylesheetRules(rules);
		
		NTES.ui.Template.load(t.$galleryTpl.NTES('> div'));
		
		t.$viewStream && t.$viewStream.addEvent('click', function(ev){ ev.preventDefault(); t.changeMode('stream').changeHash('q=1').stats(); });
		
		if(t.$searchPhoto){
			var $label = NTES.one('> label', t.$searchInput.parentNode);
			
			t.$searchInput.addEvent('focus', function(){  $label.addCss('hidden'); }).addEvent('blur', function(){ !this.value && $label.removeCss('hidden'); });
			t.$searchPhoto.addEvent('submit', function(ev){
				ev.preventDefault();
				var q = this['q'].value;
				q && t.changeMode('search', q).changeHash('s=' + q + '&q=1').stats();
			});
			t.$viewPhoto.addEvent('click', function(ev){ ev.preventDefault(); t.changeMode('photo').changeHash('p=' + t.photoInfo[0].id).stats(); });
		}
	}
	
	if (t.$photoLayout) {
		t.$photoLayout.NTES('.nph_layout_close').addEvent('click', function (ev) {
			ev.preventDefault();
			
			nph.hide(t.$photoLayout);
		});
		t.$photoLayout.NTES('.nph_btn_again').addEvent('click', function (ev) {
			ev.preventDefault();
			
			nph.hide(t.$photoLayout);
			t.showPhoto().changeHash('p=' + t.photoInfo[0].id).stats();
		});
	}
	
	var oHash = t.parseObj(window.location.hash);
	
	oHash.s && t.$searchPhoto ? t.changeMode('search', oHash.s) : oHash.q && t.$viewStream ? t.changeMode('stream', +oHash.q) : t.changeMode('photo', oHash.p);
};

nph.Gallery.prototype = {
	showPhoto: function(p){
		var t = this,
			index = p && t.photoIndex[p] ? t.photoIndex[p] : 0,
			info = t.photoInfo[index],
			turn = t.turn(index);

		if(info.img != t.$photo.src){
			t.$photoLoading.removeCss('hidden');
			t.$photoIndex.attr('innerHTML', index + 1);
			t.$photo.src = info.aimg ? info.aimg : info.img;
			t.$photoDesc.attr('innerHTML', String.format('<h2>%1</h2>', info.title) + (info.desc && String.format('<p>%1</p>', info.desc)));
			t.$photoPrev.href = turn.prev;
			t.$photoNext.href = turn.next;
			t.bar.onStart = function(){
				t.$thumb.NTES('> li.nph_list_active').removeCss('nph_list_active');
				t.$thumbs.NTES(index).addCss('nph_list_active');
			}; 
			t.bar.scrollTo(info.pos);
			t.$viewOrig && (t.$viewOrig.href = info.img);
		}
		
		t.$photoLayout && nph.hide(t.$photoLayout);

		return t;
	},
	
	changeMode: function(mode, value){
		var	t = this;
		
		if (mode == 'photo') {
			NTES([t.$modePhoto, t.$photoType, t.$setInfo, t.$galleryRelat]).removeCss('hidden');
			NTES([t.$modeStream, t.$streamType, t.$modeSearch, t.$searchTheme]).addCss('hidden');
			return t.showPhoto(value);
		}
		
		if (mode == 'stream') {
			NTES([t.$modeStream, t.$streamType, t.$setInfo, t.$galleryRelat]).removeCss('hidden');
			NTES([t.$modePhoto, t.$photoType, t.$modeSearch, t.$searchTheme]).addCss('hidden');
			return t.showStream(value);
		}
		
		if (mode == 'search') {
			NTES([t.$modeSearch, t.$searchTheme]).removeCss('hidden');
			NTES([t.$modePhoto, t.$modeStream, t.$setInfo, t.$galleryRelat]).addCss('hidden');
			return t.showSearch(value);
		}
	},
	
	resize: function($img, size){
		$img.removeAttribute('width');
		$img.removeAttribute('height');
		size = size || {};
		
		var rw = size.width ? $img.width / size.width : 0,
			rh = size.height ? $img.height / size.height : 0;
			
		if(rw > 1 || rh > 1){
			rw > rh ? $img.width = size.width : $img.height = size.height;
		}
	},
	
	parseObj: function (hash) {
		var rhash = /[#&]([^&=]+)=([^&=]+)/ig,
			a = rhash.exec(hash),
			o = {};
		
		while (a) {
			o[a[1]] = a[2];
			a = rhash.exec(hash);
		}
		
		return o;
	},
	
	changeHash: function (hash) {
		window.location.hash = hash;
		
		return this;
	},
	
	stats: function(){
		'function' === typeof vjEventTrack && vjEventTrack();
		'function' === typeof neteaseTracker && neteaseTracker();
	}
}

window.nph = nph;

})(window);