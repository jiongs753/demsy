(function() {
	
	jQuery.fn.wordWrap = function(action) {
		
		if( action != 'on' && action != 'off' ) action = 'on';
		
		jQuery(this).each( function() {
			
			var el = jQuery(this);
			
			switch( action ) {
				
				case 'on':
					
					if( jQuery.browser.msie ) {
						el.attr('wrap', 'soft');
					} else {
						var text = el.val();
						el.clone(true).attr('wrap', 'on').val(text).insertAfter(el);
						el.remove();
					}
					
				break;
				
				case 'off':
					
					if( jQuery.browser.msie ) {
						el.attr('wrap', 'off');
					} else {
						var text = el.val();
						el.clone(true).attr('wrap', 'off').val(text).insertAfter(el);
						el.remove();
					}
					
				break;
				
			}
			
		});
		
		return jQuery(this);
		
	}
  
})();