<form id="form_{$ui.uiid}" action="/ui/{tostring bean=$block.link.id}" method="post" onsubmit="if(this.elements['keywords'].value.length==0){
    alert('请输入检索关键字'); return false;
    }" >
<input name="keywords" class="ir input" type="text" /><input type="submit" class="submitButton" value="检索" />
</form>