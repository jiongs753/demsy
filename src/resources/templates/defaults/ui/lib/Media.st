{if $data.img != null}
{isimg assign="isimg" bean=$data.img}
{if $isimg}
{if $data.href !=null }{import file="a.st" href=$data.href target=$data.target title=$data.title img=$data.img}
{else}{import file="img.st" img=$data.img}{/if}
{else}
<div id="media{$ui.id}"></div>
<script>
$(document).ready(function(){
    $('#media{$ui.id}').flash(
    	{
            src: '{tostring bean=$data  prop="img"}',
            width: {if $block.imageWidth == null}{$block.position.width}{else}{$block.imageWidth}{/if},
            height: {if $block.imageHeight == null}{$block.position.height}{else}{$block.imageHeight}{/if}
        },{
            expressInstall: true
        }
    );
});
</script>
{/if}
{/if}