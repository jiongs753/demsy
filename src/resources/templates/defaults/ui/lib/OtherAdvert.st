<div id="media{$ui.id}"></div>
<script>
$(document).ready(function(){
    $('#media{$ui.id}').flash(
    	{
            src: '{tostring bean=$data  prop="img"}'
            ,width: {$block.imageWidth}
            ,height: {$block.imageHeight}
        },{
            expressInstall: true
        }
    );
});
</script>
